package com.walletech.service;

import com.walletech.dao.mapper.PulseDischargeCMDMapper;
import com.walletech.po.PulseCMDInfo;
import com.walletech.queue.exception.QueueException;
import com.walletech.queue.message.PulseCMDMessage;
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
public class PulseCMDService {
    private static final Logger logger = LoggerFactory.getLogger(PulseCMDService.class);
    @Autowired
    private PulseDischargeCMDMapper pulseDischargeCMDMapper;
    @Value("${pulse.request.timeOut}")
    private Integer requestTimeOut;
    @Value("${server.num}")
    private Integer serverNum;
//    @Value("${pulse.maxNum}")
//    private Integer maxPulseNum;
    @Value("${pulse.timeOut}")
    private Integer pulseTimeOut;

    public void doService(PulseCMDMessage message) throws QueueException {
        try {
            PulseCMDInfo pulseCMDInfo = message.getPulseCMDInfo();
            if (pulseCMDInfo.getSendDone() != 1) {
                String gprsId = pulseCMDInfo.getGprsId();
                CacheUtil.removePulseCMDInfo(gprsId);
            }
            pulseDischargeCMDMapper.updatePulseCMDStatus(pulseCMDInfo);
        }catch (Exception e){
            throw new QueueException(e.getMessage(), e);
        }
    }

    public void pollingPulseCMD(){
        //获得脉冲命令
        List<PulseCMDInfo> pulseCMDInfos = pulseDischargeCMDMapper.pollingPulseCMD(serverNum);
        if (CollectionUtils.isEmpty(pulseCMDInfos)){
            return;
        }

        //脉冲命令处理
        for(PulseCMDInfo pulseCMDInfo : pulseCMDInfos){
            String gprsId = pulseCMDInfo.getGprsId();
            if (CacheUtil.getPulseCMDInfo(gprsId) != null)
            {
                logger.debug("[{}]有脉冲请求正在处理,等待下次轮询",gprsId);
                continue;
            }
            /**
             * 脉冲前不进行关闭均衡 由硬件控制
             */
//            Date now = new Date();
//            Date sendTime = CacheUtil.getCloseBalanceTime(gprsId);
//            if (sendTime!=null){
//                if (now.getTime() - sendTime.getTime() < 90000){
//                    logger.debug("[{}]已发送过脉冲命令前关闭均衡指令,等待发送脉冲命令",gprsId);
//                    continue;
//                }
                logger.info("[{}]开始发送脉冲命令",gprsId);
//                CacheUtil.removeCloseBalanceTime(gprsId);
                beginSendPulseCMD(pulseCMDInfo, gprsId);
//            }else {
//                logger.info("[{}]发送脉冲命令前关闭均衡指令",gprsId);
//                if(closeBalance(gprsId)){
//                    CacheUtil.putCloseBalanceTime(gprsId,now);
//                }
//            }
        }
    }

    public void beginSendPulseCMD(PulseCMDInfo pulseCMDInfo, String gprsId) {
        //发送脉冲命令
        pulseCMDInfo.setEndTime(new Date());
        if (sendPulseCMD(pulseCMDInfo,gprsId)){
            pulseDischargeCMDMapper.updatePulseCMDStatus(pulseCMDInfo);
            CacheUtil.putPulseCMDInfo(gprsId,pulseCMDInfo);
        }else {
            pulseCMDInfo.setSendDone(4);
            pulseDischargeCMDMapper.updatePulseCMDStatus(pulseCMDInfo);
        }
    }

    /**
     * 关闭均衡
     * @param gprsId
     */
    private boolean closeBalance(String gprsId){
        try {
            Channel channel = CacheUtil.getGprsChannelMap().get(gprsId);
            if (channel == null) {
                logger.info("设备[{}]已离线，关闭均衡指令发送失败", gprsId);
                return false;
            }
            byte[] request = new byte[13];
            request[3] = (byte) 0x16;
            request = ProtocolUtil.beforeSend(request);
            ByteBuf buf = channel.alloc().directBuffer(13);
            buf.writeBytes(request);
            channel.writeAndFlush(buf);
            logger.info("设备[{}]关闭均衡指令发送成功，[{}]", gprsId, StringUtil.toHexString(request));
            return true;
        }catch (Exception e){
            logger.error("设备[{}]关闭均衡指令发送失败",gprsId,e);
            return false;
        }
    }

    /**
     * 发送脉冲命令
     */
    private boolean sendPulseCMD(PulseCMDInfo pulseCMDInfo,String gprsId){
        try{
            Channel channel = CacheUtil.getGprsChannelMap().get(gprsId);
            if (channel == null){
                logger.info("设备[{}]已离线，脉冲命令发送失败",gprsId);
                return false;
            }
            byte[] request = new byte[9];
            request[3] = (byte) 0x14;
            request[4] = pulseCMDInfo.getPulseCell().byteValue();
            String byteStr = "00"
             + ProtocolUtil.supplementZero(Integer.toBinaryString(checkValue(pulseCMDInfo.getDischargeTime())),2)
             + ProtocolUtil.supplementZero(Integer.toBinaryString(checkValue(pulseCMDInfo.getSlowSampleInterval())),2)
             + ProtocolUtil.supplementZero(Integer.toBinaryString(checkValue(pulseCMDInfo.getFastSampleInterval())),2);
            request[5] = (byte) Integer.parseInt(byteStr,2);
            System.arraycopy(ByteExchangeUtil.intToUInt16Bytes(pulseCMDInfo.getSlowSampleTime()),
                    0,request,6,2);
            request = ProtocolUtil.beforeSend(request);
            ByteBuf buf = channel.alloc().directBuffer(9);
            buf.writeBytes(request);
            channel.writeAndFlush(buf);
            logger.info("设备[{}] [{}]脉冲命令发送成功，[{}]",gprsId,pulseCMDInfo.getPulseCell(),StringUtil.toHexString(request));
            return true;
        } catch (Exception e){
            logger.error("设备[{}] [{}]脉冲命令发送失败",gprsId,pulseCMDInfo.getPulseCell(),e);
            return false;
        }
    }


    /**
     * 检查数据
     */
    private Integer checkValue(Integer value){
        if (value > 3){
            value = 3;
        }else if (value < 0){
            value = 0;
        }
        return value;
    }
    /**
     * 检查是否有超时的脉冲请求超时
     */
    public void checkTimeOut(){
        Date now = new Date();
        if (CollectionUtils.isEmpty(CacheUtil.getPulseCmdMap().values())){
            return;
        }
        for (PulseCMDInfo pulseCMDInfo : CacheUtil.getPulseCmdMap().values()){
            Integer status = pulseCMDInfo.getSendDone();
            Date endTime = pulseCMDInfo.getEndTime();
            //单次脉冲请求超过10min未收到响应，则视为脉冲请求发送失败
            if (status == 0){
                if (now.getTime() - endTime.getTime() >= requestTimeOut * 60 * 1000){
                    PulseCMDInfo nowCMDInfo = pulseDischargeCMDMapper.checkTimeOut(pulseCMDInfo);
                    if(nowCMDInfo == null){//数据被删除了
                        CacheUtil.getPulseCmdMap().remove(pulseCMDInfo.getGprsId());
                        continue;
                    }
                    Integer nowStatus = nowCMDInfo.getSendDone();
                    // 请求应答其他机器，且脉冲未完成的情况下
                    if (nowStatus == 1){
                        pulseCMDInfo.setSendDone(1);
                        pulseCMDInfo.setEndTime(nowCMDInfo.getEndTime());
                        continue;
                    }else if (nowStatus == 0){
                        pulseCMDInfo.setSendDone(4);
                        pulseCMDInfo.setEndTime(now);
                        pulseDischargeCMDMapper.updatePulseCMDStatus(pulseCMDInfo);
                        CacheUtil.removePulseCMDInfo(pulseCMDInfo.getGprsId());
                        logger.warn("[{}] [{}]脉冲请求发送超时",pulseCMDInfo.getGprsId(),pulseCMDInfo.getPulseCell());
                    }else {
                        // 脉冲在其他机器处理完毕
                        CacheUtil.getPulseCmdMap().remove(pulseCMDInfo.getGprsId());
                    }
                }
            }
            //脉冲放电请求已成功，但超过设置时间未完成，则视为放电超时
            if (status == 1){
                if (now.getTime() - endTime.getTime() >= pulseTimeOut * 60 * 1000){
                    if (pulseDischargeCMDMapper.checkTimeOut(pulseCMDInfo)==null){
                        CacheUtil.getPulseCmdMap().remove(pulseCMDInfo.getGprsId());
                        continue;
                    }
                    pulseCMDInfo.setSendDone(6);
                    pulseCMDInfo.setEndTime(now);
                    pulseDischargeCMDMapper.updatePulseCMDStatus(pulseCMDInfo);
                    CacheUtil.removePulseCMDInfo(pulseCMDInfo.getGprsId());
                    logger.warn("[{}] [{}]脉冲放电超时",pulseCMDInfo.getGprsId(),pulseCMDInfo.getPulseCell());
                }
            }
        }
    }
}
