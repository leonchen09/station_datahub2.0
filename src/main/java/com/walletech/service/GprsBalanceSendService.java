package com.walletech.service;

import com.walletech.dao.mapper.GprsBalanceSendInfoMapper;
import com.walletech.po.GprsBalanceSendInfo;
import com.walletech.queue.exception.QueueException;
import com.walletech.queue.message.BalanceCMDACKMessage;
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

import java.util.Date;
import java.util.List;

@Service
public class GprsBalanceSendService {

    private static final Logger logger = LoggerFactory.getLogger(GprsBalanceSendInfo.class);
    @Autowired
    private GprsBalanceSendInfoMapper gprsBalanceSendInfoMapper;
    @Value("${server.num}")
    private Integer serverNum;
    @Value("${request.timeOut}")
    private Integer timeOut;

    public void doService(BalanceCMDACKMessage message) throws QueueException {
        try {
            GprsBalanceSendInfo info = message.getGprsBalanceSendInfo();
            String gprsId = info.getGprsId();
            gprsBalanceSendInfoMapper.updateGprsBalanceSendInfo(info);
            CacheUtil.removeBalanceSendInfo(gprsId);
        }catch (Exception e){
            throw new QueueException(e.getMessage(), e);
        }
    }

    /**
     * 轮询数据库 发送均衡指令
     */
    public void pollingBalanceSend(){
        checkTimeOut();
//        sendCloseBalanceCMD();
        List<GprsBalanceSendInfo> balanceSendInfos = gprsBalanceSendInfoMapper.pollingBalanceSend(serverNum);
        if (CollectionUtils.isEmpty(balanceSendInfos)){
            return;
        }
        for(GprsBalanceSendInfo info : balanceSendInfos){
            String gprsId = info.getGprsId();
            Date now = new Date();
            info.setSendTime(now);
            if (sendBalanceCMD(gprsId,info)){
                info.setSendDone((byte) 1);
                gprsBalanceSendInfoMapper.updateGprsBalanceSendInfo(info);
                CacheUtil.putBalanceSendInfo(gprsId,info);
            } else {
                info.setSendDone((byte) 4);
                gprsBalanceSendInfoMapper.updateGprsBalanceSendInfo(info);
            }
        }

    }

    /**
     * 检查超时的均衡指令
     */
    private void checkTimeOut(){
        if (CollectionUtils.isEmpty(CacheUtil.getBalanceSendMap())){
            return;
        }
        for(GprsBalanceSendInfo info : CacheUtil.getBalanceSendMap().values()){
            if (info.getSendDone()!=1){
                continue;
            }
            Date now = new Date();
            if (now.getTime() - info.getSendTime().getTime() >= timeOut * 60 * 1000){
                if (gprsBalanceSendInfoMapper.checkTimeOut(info)==null){
                    CacheUtil.removeBalanceSendInfo(info.getGprsId());
                    continue;
                }
                info.setSendDone((byte) 6);
                logger.warn("[{}]均衡指令应答超时",info.getGprsId());
                gprsBalanceSendInfoMapper.updateGprsBalanceSendInfo(info);
                CacheUtil.removeBalanceSendInfo(info.getGprsId());
            }
        }
    }

    /**
     * 发送均衡指令
     */
    private boolean sendBalanceCMD(String gprsId,GprsBalanceSendInfo info){
        try {
            Channel channel = CacheUtil.getGprsChannelMap().get(gprsId);
            if (channel == null){
                logger.info("设备[{}]已离线，均衡指令发送失败",gprsId);
                return false;
            }
            byte[] request = new byte[13];
            request[3] = (byte) 0x16;
            request[4] = (byte) (info.getCell1() | (info.getCell2() << 2) | (info.getCell3() << 4) | (info.getCell4() << 6));
            request[5] = (byte) (info.getCell5() | (info.getCell6() << 2) | (info.getCell7() << 4) | (info.getCell8() << 6));
            request[6] = (byte) (info.getCell9() | (info.getCell10() << 2) | (info.getCell11() << 4) | (info.getCell12() << 6));
            request[7] = (byte) (info.getCell13() | (info.getCell14() << 2) | (info.getCell15() << 4) | (info.getCell16() << 6));
            request[8] = (byte) (info.getCell17() | (info.getCell18() << 2) | (info.getCell19() << 4) | (info.getCell20() << 6));
            request[9] = (byte) (info.getCell21() | (info.getCell22() << 2) | (info.getCell23() << 4) | (info.getCell24() << 6));
            byte[] bytes = ByteExchangeUtil.intToUInt16Bytes(info.getDuration());
            bytes[0] = (byte) (bytes[0] | (info.getMode() << 7));
            System.arraycopy(bytes,0,request,10,2);
            request = ProtocolUtil.beforeSend(request);
            ByteBuf buf = channel.alloc().directBuffer(13);
            buf.writeBytes(request);
            channel.writeAndFlush(buf);
            logger.info("设备[{}]均衡指令发送成功，[{}]",gprsId,StringUtil.toHexString(request));
            return true;
        } catch (Exception e){
            logger.error("设备[{}]均衡指令发送失败",gprsId,e);
            return false;
        }
    }


    /**
     * 发送关闭均衡指令
     */
    public void sendCloseBalanceCMD(String gprsId){
        try {
            Channel channel = CacheUtil.getGprsChannelMap().get(gprsId);
            if (channel == null){
                logger.info("设备[{}]已离线，均衡指令发送失败",gprsId);
                return;
            }
            byte[] request = new byte[13];
            request[3] = (byte) 0x16;
            request[4] = (byte) 0x00;
            request[5] = (byte) 0x00;
            request[6] = (byte) 0x00;
            request[7] = (byte) 0x00;
            request[8] = (byte) 0x00;
            request[9] = (byte) 0x00;
            byte[] bytes = ByteExchangeUtil.intToUInt16Bytes(120); //持续时间120分钟
            bytes[0] = (byte) (bytes[0] | (1 << 7)); //均衡模式为强制执行 : 1
            System.arraycopy(bytes,0,request,10,2);
            request = ProtocolUtil.beforeSend(request);
            ByteBuf buf = channel.alloc().directBuffer(13);
            buf.writeBytes(request);
            channel.writeAndFlush(buf);
            logger.info("设备[{}]关闭均衡指令发送成功，[{}]",gprsId,StringUtil.toHexString(request));
        } catch (Exception e){
            logger.error("设备[{}]关闭均衡指令发送失败",gprsId,e);
        }
    }



//    2.0版本中，数据网关不再需要主动发送关闭均衡命令， 均衡到设定时间后，设备主机会自行处理关闭均衡命令。
//    /**
//     * 主动发送关闭指令 只针对T0B000000~T0B002000
//     */
//    private void sendCloseBalanceCMD(){
//        if (CollectionUtils.isEmpty(CacheUtil.getBalanceSendMap())){
//            return;
//        }
//        for(GprsBalanceSendInfo info : CacheUtil.getBalanceSendMap().values()){
//            String gprsId = info.getGprsId();
//            if (gprsId.compareTo("T0B000000")<0 && gprsId.compareTo("T0B002000")>0){
//                continue;
//            }
//            Date now = new Date();
//            //达到均衡时间且未发送过关闭指令时，发送关闭指令
//            if (now.getTime() - info.getSendTime().getTime() >= info.getDuration() * 60 * 1000
//                    && info.getCloseTime() == 0 ){
//                info.setCell1((byte) 0);
//                info.setCell2((byte) 0);
//                info.setCell3((byte) 0);
//                info.setCell4((byte) 0);
//                info.setCell5((byte) 0);
//                info.setCell6((byte) 0);
//                info.setCell7((byte) 0);
//                info.setCell8((byte) 0);
//                info.setCell9((byte) 0);
//                info.setCell10((byte) 0);
//                info.setCell11((byte) 0);
//                info.setCell12((byte) 0);
//                info.setCell13((byte) 0);
//                info.setCell14((byte) 0);
//                info.setCell15((byte) 0);
//                info.setCell16((byte) 0);
//                info.setCell17((byte) 0);
//                info.setCell18((byte) 0);
//                info.setCell19((byte) 0);
//                info.setCell20((byte) 0);
//                info.setCell21((byte) 0);
//                info.setCell22((byte) 0);
//                info.setCell23((byte) 0);
//                info.setCell24((byte) 0);
//                sendBalanceCMD(gprsId,info);
//                info.setCloseTime(1);
//                info.setSendTime(now);
//                CacheUtil.putBalanceSendInfo(gprsId,info);
//            }
//            //已发送过一次关闭指令一分钟后，再次发送
//            if (now.getTime() - info.getSendTime().getTime() >= 60 * 1000 && info.getCloseTime()==1){
//                sendBalanceCMD(gprsId,info);
//                info.setCloseTime(2);
//                CacheUtil.putBalanceSendInfo(gprsId,info);
//            }
//        }
//    }
}
