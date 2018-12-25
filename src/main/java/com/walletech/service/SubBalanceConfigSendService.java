package com.walletech.service;

import com.walletech.dao.mapper.SubBalanceConfigSendMapper;
import com.walletech.po.SubBalanceConfigSend;
import com.walletech.queue.exception.QueueException;
import com.walletech.queue.message.SubBalanceConfigACKMessage;
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
import java.util.Date;
import java.util.List;

@Service
public class SubBalanceConfigSendService {

    private static final Logger logger = LoggerFactory.getLogger(SubBalanceConfigSendService.class);

    @Value("${server.num}")
    private Integer serverNum;
    @Value("${request.timeOut}")
    private Integer timeOut;

    @Autowired
    private SubBalanceConfigSendMapper subBalanceConfigSendMapper;

    private static final BigDecimal DECIMAL_100 = new BigDecimal(100);
    private static final BigDecimal DECIMAL_10 = new BigDecimal(10);
    private static final BigDecimal DECIMAL_1000 = new BigDecimal(1000);
    /**
     * 轮询数据库，发送均衡从机均衡参数配置
     */
    public void pollingSubBalanceConfigSend(){
        List<SubBalanceConfigSend> sends = subBalanceConfigSendMapper.pollingSubBalanceConfigSend(serverNum);
        if (CollectionUtils.isEmpty(sends)){
            return;
        }
        for (SubBalanceConfigSend send : sends){
            if (CacheUtil.getSubBalanceConfigSend(send.getGprsId())!=null){
                continue;
            }
            doSendSubBalanceConfig(send);
        }
    }

    /**
     * 处理均衡从机均衡参数配置应答
     * @param message
     * @throws QueueException
     */
    public void doService(SubBalanceConfigACKMessage message) throws QueueException {
        try {
            subBalanceConfigSendMapper.updateByPrimaryKey(message.getSend());
            CacheUtil.removeSubBalanceConfigSend(message.getSend().getGprsId());
        } catch (Exception e){
            throw new QueueException(e.getMessage(), e);
        }
    }

    /**
     * 发送均衡从机均衡参数接口
     * @param send
     */
    public void doSendSubBalanceConfig(SubBalanceConfigSend send){
        String gprsId = send.getGprsId();
        send.setEndTime(new Date());
        if (!sendSubBalanceConfig(send,gprsId)){
            send.setSendDone(4);
        } else {
            send.setSendDone(1);
        }
        subBalanceConfigSendMapper.updateByPrimaryKey(send);
    }

    /**
     * 发送均衡从机均衡参数
     * @param send
     * @param gprsId
     * @return
     */
    private boolean sendSubBalanceConfig(SubBalanceConfigSend send,String gprsId){
        try {
            Channel channel = CacheUtil.getGprsChannelMap().get(gprsId);
            if (channel == null){
                logger.warn("设备[{}]已离线，均衡从机均衡参数发送失败",gprsId);
                return false;
            }
            byte[] request = getSend(send);
            ByteBuf buf = channel.alloc().directBuffer(38);
            buf.writeBytes(request);
            channel.writeAndFlush(buf);
            logger.info("设备[{}]均衡从机均衡参数发送成功，[{}]",gprsId,StringUtil.toHexString(request));
            CacheUtil.putSubBalanceConfigSend(gprsId,send);
            return true;
        } catch (Exception e){
            logger.error("设备[{}]均衡从机均衡参数发送失败",gprsId,e);
            return false;
        }
    }

    /**
     * 获取均衡从机均衡参数byte[]
     * @param send
     * @return
     */
    private byte[] getSend(SubBalanceConfigSend send){
        byte[] request = new byte[39];
        request[3] = (byte) 0x30;
        request[4] = send.getBalanceSubdeviceCode();
        //升压模式输出功率
        byte[] upPatternOutPower =
                ByteExchangeUtil.shortToByte(send.getUpPatternInPower().multiply(DECIMAL_10).shortValue());
        System.arraycopy(upPatternOutPower,0,request,5,2);
        //升压模式输出电压
        byte[] upPatternOutVol =
                ByteExchangeUtil.intToUInt16Bytes(send.getUpPatternOutVol().multiply(DECIMAL_100).intValue());
        System.arraycopy(upPatternOutVol,0,request,7,2);
        //降压模式三段式充电恒流值
        byte[] downPatternOutCur =
                ByteExchangeUtil.shortToByte(send.getDownPatternOutCur().multiply(DECIMAL_100).shortValue());
        System.arraycopy(downPatternOutCur,0,request,9,2);
        //降压模式三段式浮充电压值
        byte[] downPatternOutVol =
                ByteExchangeUtil.intToUInt16Bytes(send.getDownPatternOutVol().multiply(DECIMAL_100).intValue());
        System.arraycopy(downPatternOutVol,0,request,11,2);
        //升压模式低压侧欠压保护阈值
        byte[] upPatternLowVolThreshold =
                ByteExchangeUtil.intToUInt16Bytes(send.getUpPatternLowVolThreshold().multiply(DECIMAL_100).intValue());
        System.arraycopy(upPatternLowVolThreshold,0,request,13,2);
        //升压模式升压输出恒压值
        byte[] upPatternHighVolConstantVol =
                ByteExchangeUtil.intToUInt16Bytes(send.getUpPatternHighVolConstantVol().multiply(DECIMAL_100).intValue());
        System.arraycopy(upPatternHighVolConstantVol,0,request,15,2);
        //升压模式输出高压保护阈值
        byte[] upPatternHighVolThreshold =
                ByteExchangeUtil.intToUInt16Bytes(send.getUpPatternHighVolThreshold().multiply(DECIMAL_100).intValue());
        System.arraycopy(upPatternHighVolThreshold,0,request,17,2);
        //降压模式高压侧欠压保护阈值
        byte[] downPatternHighVolLowVolThreshold =
                ByteExchangeUtil.intToUInt16Bytes(send.getDownPatternHighVolLowVolThreshold().multiply(DECIMAL_100).intValue());
        System.arraycopy(downPatternHighVolLowVolThreshold,0,request,19,2);
        //降压模式三段式恒压电压值
        byte[] downPatternDownVolConstant =
                ByteExchangeUtil.intToUInt16Bytes(send.getDownPatternDownVolConstant().multiply(DECIMAL_100).intValue());
        System.arraycopy(downPatternDownVolConstant,0,request,21,2);
        //降压模式输出高压保护阈值
        byte[] downPatternHighVolThreshold =
                ByteExchangeUtil.intToUInt16Bytes(send.getDownPatternHighVolThreshold().multiply(DECIMAL_100).intValue());
        System.arraycopy(downPatternHighVolThreshold,0,request,23,2);
        //升压模式控制压差
        byte[] upPatternControlVolDiff =
                ByteExchangeUtil.intToUInt16Bytes(send.getUpBalanceTime().intValue());
        System.arraycopy(upPatternControlVolDiff,0,request,25,2);
        //单体最低放电电压阀值
        byte[] minDischargeVolThreshold =
                ByteExchangeUtil.intToUInt16Bytes(send.getMinDischargeVolThreshold().multiply(DECIMAL_1000).intValue());
        System.arraycopy(minDischargeVolThreshold,0,request,27,2);
        //低压侧浮充电压阀值
        byte[] lowFloatingDischargeVolThreshold =
                ByteExchangeUtil.intToUInt16Bytes(send.getLowFloatingDischargeVolThreshold().multiply(DECIMAL_1000).intValue());
        System.arraycopy(lowFloatingDischargeVolThreshold,0,request,29,2);
        //低压侧浮充电流阀值
        byte[] lowFloatingDischargeCurThreshold =
                ByteExchangeUtil.shortToByte(send.getLowFloatingDischargeCurThreshold().multiply(DECIMAL_100).shortValue());
        System.arraycopy(lowFloatingDischargeCurThreshold,0,request,31,2);
        //降压模式三段式均充转浮充电流
        byte[] downPatternThreeChangeFloatingCur =
                ByteExchangeUtil.intToUInt16Bytes(send.getDownPatternThreeChangeFloatingCur().multiply(DECIMAL_100).intValue());
        System.arraycopy(downPatternThreeChangeFloatingCur,0,request,33,2);
        //降压模式三段式浮充电流上限值
        byte[] downPatternThreeFloatingCurUpper =
                ByteExchangeUtil.intToUInt16Bytes(send.getDownPatternThreeFloatingCurUpper().multiply(DECIMAL_100).intValue());
        System.arraycopy(downPatternThreeFloatingCurUpper,0,request,35,2);
        //均衡从机连接方式
        request[37] = send.getBalanceLinkWay().byteValue();
        //bcc
        request = ProtocolUtil.beforeSend(request);
        return request;
    }


    /**
     * 检查超时未应答的均衡从机均衡参数配置
     */
    public void checkTimeOut(){
        if (CollectionUtils.isEmpty(CacheUtil.getSubBalanceConfigSendMap())){
            return;
        }
        Date now = new Date();
        for(SubBalanceConfigSend info : CacheUtil.getSubBalanceConfigSendMap().values()){
            if (now.getTime() - info.getEndTime().getTime() >= timeOut * 60 * 1000){
                if (subBalanceConfigSendMapper.checkTimeOut(info)==null){
                    CacheUtil.getSubBalanceConfigSendMap().remove(info.getGprsId());
                    continue;
                }
                logger.warn("设备[{}]均衡从机均衡参数应答超时",info.getGprsId());
                info.setSendDone(6);
                info.setEndTime(now);
                subBalanceConfigSendMapper.updateByPrimaryKey(info);
                CacheUtil.removeSubBalanceConfigSend(info.getGprsId());
            }
        }
    }

}
