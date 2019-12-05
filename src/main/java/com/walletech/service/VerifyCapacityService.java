package com.walletech.service;

import com.walletech.dao.mapper.VerifyCapacityMapper;
import com.walletech.po.VerifyCapacity;
import com.walletech.queue.exception.QueueException;
import com.walletech.queue.message.VerifyCapacityACKMessage;
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
public class VerifyCapacityService {
    private static final Logger logger = LoggerFactory.getLogger(VerifyCapacityService.class);
    @Autowired
    private VerifyCapacityMapper verifyCapacityMapper;
    @Value("${server.num}")
    private Integer serverNum;
    @Value("${request.timeOut}")
    private Integer timeOut;

    public void doService(VerifyCapacityACKMessage message) throws QueueException {
        try {
            verifyCapacityMapper.updateVerifyCapacity(message.getVerifyCapacity());
            CacheUtil.removeVerifyCapacity(message.getVerifyCapacity().getGprsId());
        }catch (Exception e){
            throw new QueueException(e.getMessage(), e);
        }
    }

    public void pollingVerifyCapacity(){
        List<VerifyCapacity> infos = verifyCapacityMapper.pollingVerifyCapacity(serverNum);
        if (CollectionUtils.isEmpty(infos)){
            return;
        }
        for(VerifyCapacity info : infos){
            String gprsId = info.getGprsId();
            if (CacheUtil.getVerifyCapacity(gprsId)!=null){
                logger.debug("设备[{}]有核容指令正在处理,等待下次轮询",gprsId);
                continue;
            }
            doSendVerifyCapacityInfo(info, gprsId);
        }
    }

    public void doSendVerifyCapacityInfo(VerifyCapacity info, String gprsId) {
        Date now = new Date();
        if (sendVerifyCapacityCMD(gprsId,info)){
            info.setSendTime(now);
            info.setSendDone((byte) 1);
            verifyCapacityMapper.updateVerifyCapacity(info);
            CacheUtil.putVerifyCapacity(gprsId,info);
        }else {
            info.setSendDone((byte) 4);
            info.setSendTime(now);
            verifyCapacityMapper.updateVerifyCapacity(info);
        }
    }

    /**
     * 发送命令
     */
    private boolean sendVerifyCapacityCMD(String gprsId, VerifyCapacity info){
        try {
            Channel channel = CacheUtil.getGprsChannelMap().get(gprsId);
            if (channel == null){
                logger.warn("设备[{}]已离线，核容指令发送失败",gprsId);
                return false;
            }
            byte[] request = new byte[5];
            request[3] = info.getCmd().byteValue();
            request = ProtocolUtil.beforeSend(request);
            ByteBuf buf = channel.alloc().directBuffer(5);
            buf.writeBytes(request);
            channel.writeAndFlush(buf);
            logger.info("设备[{}]核容指令发送成功，[{}]",gprsId,StringUtil.toHexString(request));
            return true;
        } catch (Exception e){
            logger.error("设备[{}]核容指令发送失败",gprsId,e);
            return false;
        }
    }

    /**
     * 检查超时的指令
     */
    public void checkTimeOut(){
        if (CollectionUtils.isEmpty(CacheUtil.getVerifyCapacityMap())){
            return;
        }
        for(VerifyCapacity info : CacheUtil.getVerifyCapacityMap().values()){
            Date now = new Date();
            if (now.getTime() - info.getSendTime().getTime() >= timeOut * 60 * 1000){
                if (verifyCapacityMapper.checkTimeOut(info)==null){
                    CacheUtil.getVerifyCapacityMap().remove(info.getGprsId());
                    continue;
                }
                logger.warn("[{}]核容应答超时",info.getGprsId());
                info.setSendDone((byte) 6);
                verifyCapacityMapper.updateVerifyCapacity(info);
                CacheUtil.removeVerifyCapacity(info.getGprsId());
            }
        }
    }
}
