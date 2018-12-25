package com.walletech.service;

import com.walletech.dao.mapper.GprsDeviceSendInfoMapper;
import com.walletech.po.GprsDeviceSendInfo;
import com.walletech.queue.exception.QueueException;
import com.walletech.queue.message.GprsDeviceSendACKMessage;
import com.walletech.util.ByteExchangeUtil;
import com.walletech.util.CacheUtil;
import com.walletech.util.ProtocolUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

@Service
public class GprsDeviceSendService {
    private static final Logger logger = LoggerFactory.getLogger(GprsDeviceSendService.class);
    @Autowired
    private GprsDeviceSendInfoMapper gprsDeviceSendInfoMapper;
    @Value("${server.num}")
    private Integer serverNum;
    private static final BigDecimal DECIMAL_1000 = new BigDecimal(1000);
    private static final BigDecimal DECIMAL_100 = new BigDecimal(100);
    private static final BigDecimal DECIMAL_10 = new BigDecimal(10);

    /**
     * 轮询数据库进行参数配置
     */
    public void pollingGprsDeviceSendInfo(){
        List<GprsDeviceSendInfo> sendInfos = gprsDeviceSendInfoMapper.pollingGprsDeviceSend(serverNum);
        if (CollectionUtils.isEmpty(sendInfos)){
            return;
        }
        for (GprsDeviceSendInfo info : sendInfos){
            doSendModifyConfig(info);
        }
    }

    public void doSendModifyConfig(GprsDeviceSendInfo info) {
        String gprsId = info.getGprsId();
        if (!sendGprsDeviceSendInfo(info,gprsId)){
            info.setSendDone(3);
        } else {
            info.setSendDone(1);
        }
        gprsDeviceSendInfoMapper.updateByPrimaryKey(info);
    }

    /**
     * 参数配置应答处理
     * @param message
     */
    public void doService(GprsDeviceSendACKMessage message) throws QueueException {
        String gprsId = message.getGprsId();
        try {
            if (message.isSuccess()){
                gprsDeviceSendInfoMapper.updateSuccessByGprsId(gprsId);
            }else {
                gprsDeviceSendInfoMapper.updateFailedByGprsId(gprsId);
            }
        }catch (Exception e){
            throw new QueueException(e.getMessage(), e);
        }
        // 参数配置状态返回，再次读取配置更新配置表。
        readGprsConfig(gprsId);
    }

    /**
     * 读取参数配置
     */
    private void readGprsConfig(String gprsId){
        Channel channel = CacheUtil.getGprsChannelMap().get(gprsId);
        if (channel==null){
            logger.warn("[{}]已掉线",gprsId);
            return;
        }
        byte[] request = new byte[5];
        request[3] = (byte) 0x11;
        request = ProtocolUtil.beforeSend(request);
        ByteBuf buf = channel.alloc().directBuffer(5);
        buf.writeBytes(request);
        channel.writeAndFlush(buf);
    }

    /**
     * 参数配置命令积压超时处理
     */
    public void sendMsgTimeOut(int id){
        gprsDeviceSendInfoMapper.sendMsgTimeOut(id);
    }

    /**
     * 发送配置
     */
    private boolean sendGprsDeviceSendInfo(GprsDeviceSendInfo sendInfo,String gprsId){
        try {
            Channel channel = CacheUtil.getGprsChannelMap().get(gprsId);
            if (channel == null){
                logger.warn("设备[{}]已离线，配置发送失败",gprsId);
                return false;
            }
            byte[] request = getSend(sendInfo);
            ByteBuf buf = channel.alloc().directBuffer(50);
            buf.writeBytes(request);
            channel.writeAndFlush(buf);
            logger.info("设备[{}]参数配置指令发送成功，[{}]",gprsId,StringUtil.toHexString(request));
            return true;
        } catch (Exception e){
            logger.error("设备[{}]配置发送失败",gprsId,e);
            return false;
        }
    }

    /**
     * 获取参数配置请求byte[]
     */
    private byte[] getSend(GprsDeviceSendInfo sendInfo){
        byte[] request = new byte[50];
        request[3] = (byte) 0x12;
        String HeartbeatInterval = Integer.toBinaryString(sendInfo.getHeartbeatInterval()/30);
        String data1 = (sendInfo.getConnectionType() == 1 ? "01" : "10" ) +
                (HeartbeatInterval.length() <6 ? "000000".substring(HeartbeatInterval.length())+HeartbeatInterval : HeartbeatInterval);
        request[4] = (byte) Integer.parseInt(data1,2);
        //浮充状态下传状态帧输间隔
        byte[] floatInterval = ByteExchangeUtil.intToUInt16Bytes(sendInfo.getFloatInterval());
        System.arraycopy(floatInterval,0,request,5,2);
        //充电状态下传状态帧输间隔
        byte[] chargeInterval = ByteExchangeUtil.intToUInt16Bytes(sendInfo.getChargeInterval());
        System.arraycopy(chargeInterval,0,request,7,2);
        //放电判断阀值
        byte[] dischargeThreshold =
                ByteExchangeUtil.intToUInt16Bytes(30000-sendInfo.getDischargeThreshold().multiply(DECIMAL_100).intValue());
        System.arraycopy(dischargeThreshold,0,request,9,2);
        //充电判断阀值
        byte[] chargeThreshold =
                ByteExchangeUtil.intToUInt16Bytes(30000-sendInfo.getChargeThreshold().multiply(DECIMAL_100).intValue());
        System.arraycopy(chargeThreshold,0,request,11,2);
        //电池额定容量
        byte[] nominalCapacity =
                ByteExchangeUtil.intToUInt16Bytes(sendInfo.getNominalCapacity().multiply(DECIMAL_10).intValue());
        System.arraycopy(nominalCapacity,0,request,13,2);
        //电池当前容量
        byte[] currentCapacity =
                ByteExchangeUtil.intToUInt16Bytes(sendInfo.getCurrentCapacity().multiply(DECIMAL_10).intValue());
        System.arraycopy(currentCapacity,0,request,15,2);
        //总压过高告警阀值
        byte[] volHighWarningThreshold =
                ByteExchangeUtil.intToUInt16Bytes(sendInfo.getVolHighWarningThreshold().multiply(DECIMAL_100).intValue());
        System.arraycopy(volHighWarningThreshold,0,request,17,2);
        //总电压过低告警阀值
        byte[] volLowWarningThreshold =
                ByteExchangeUtil.intToUInt16Bytes(sendInfo.getVolLowWarningThreshold().multiply(DECIMAL_100).intValue());
        System.arraycopy(volLowWarningThreshold,0,request,19,2);
        //单体温度过高告警阀值
        request[21] = (byte) (sendInfo.getTemHighWarningThreshold()+50);
        //单体温度过低告警阀值
        request[22] = (byte) (sendInfo.getTemLowWarningThreshold()+50);
        //SOC过低告警阈值
        request[23] = sendInfo.getSocLowWarningThreshold().byteValue();
        //放电状态下状态帧传输间隔
        byte[] dischargeTimeInterval =
                ByteExchangeUtil.intToUInt16Bytes(sendInfo.getDischargeInterval());
        System.arraycopy(dischargeTimeInterval,0,request,24,2);
        //电流异常告警上限
        byte[] currentWarningTopLimit =
                ByteExchangeUtil.intToUInt16Bytes((3000-sendInfo.getCurrentWarningToplimit().multiply(DECIMAL_10).intValue()));
        System.arraycopy(currentWarningTopLimit,0,request,26,2);
        //电流异常告警下限
        byte[] currentWarningLowerLimit =
                ByteExchangeUtil.intToUInt16Bytes((3000-sendInfo.getCurrentWarningLowerlimit().multiply(DECIMAL_10).intValue()));
        System.arraycopy(currentWarningLowerLimit,0,request,28,2);
        //单体过压告警阈值
        byte[] singleHighVolThreshold =
                ByteExchangeUtil.intToUInt16Bytes(sendInfo.getSingleHighVolThreshold().multiply(DECIMAL_1000).intValue());
        System.arraycopy(singleHighVolThreshold,0,request,30,2);
        //单体欠压告警阈值
        byte[] singleLowVolThreshold =
                ByteExchangeUtil.intToUInt16Bytes(sendInfo.getSingleLowVolThreshold().multiply(DECIMAL_1000).intValue());
        System.arraycopy(singleLowVolThreshold,0,request,32,2);
        //总电压过压恢复点
        byte[] highVolRecover =
                ByteExchangeUtil.intToUInt16Bytes(sendInfo.getHighVolRecover().multiply(DECIMAL_100).intValue());
        System.arraycopy(highVolRecover,0,request,34,2);
        //总电压欠压恢复点
        byte[] lowVolRecover =
                ByteExchangeUtil.intToUInt16Bytes(sendInfo.getLowVolRecover().multiply(DECIMAL_100).intValue());
        System.arraycopy(lowVolRecover,0,request,36,2);
        //单体过压恢复点
        byte[] singleHighVolRecover =
                ByteExchangeUtil.intToUInt16Bytes(sendInfo.getSingleHighVolRecover().multiply(DECIMAL_1000).intValue());
        System.arraycopy(singleHighVolRecover,0,request,38,2);
        //单体欠压恢复点
        byte[] singleLowVolRecover =
                ByteExchangeUtil.intToUInt16Bytes(sendInfo.getSingleLowVolRecover().multiply(DECIMAL_1000).intValue());
        System.arraycopy(singleLowVolRecover,0,request,40,2);
        //环境高温告警阈值
        request[42] = (byte) (sendInfo.getHighSurroundingtemWarningThreshold()+50);
        //环境低温告警阈值
        request[43] = (byte) (sendInfo.getLowSurroundingtemWarningThreshold()+50);
        //环境高温告警恢复点
        request[44] = (byte) (sendInfo.getHighSurroundingtemWarningRecover()+50);
        //环境低温告警恢复点
        request[45] = (byte) (sendInfo.getLowSurroundingtemWarningRecover()+50);
        //单体高温告警恢复点
        request[46] = (byte) (sendInfo.getHightemWarningRecover()+50);
        //单体低温告警恢复点
        request[47] = (byte) (sendInfo.getLowtemWarningRecover()+50);
        //电量低告警恢复点
        request[48] = sendInfo.getLowSocRecover().byteValue();

        request = ProtocolUtil.beforeSend(request);
        return request;
    }
}
