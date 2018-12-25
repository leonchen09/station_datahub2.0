package com.walletech.service;

import com.walletech.dao.mapper.ModifyIPPortInfoMapper;
import com.walletech.po.ModifyIPPortInfo;
import com.walletech.queue.exception.QueueException;
import com.walletech.queue.message.ModifyIPPortACKMessage;
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
public class ModifyIPPortService {
    private static final Logger logger = LoggerFactory.getLogger(ModifyIPPortService.class);
    @Autowired
    private ModifyIPPortInfoMapper modifyIPPortInfoMapper;
    @Value("${server.num}")
    private Integer serverNum;

    /**
     * 处理修改IP&Port命令的响应
     * @param message
     * @throws QueueException
     */
    public void doService(ModifyIPPortACKMessage message) throws QueueException {
        try {
            ModifyIPPortInfo info = message.getModifyIPPortInfo();
            modifyIPPortInfoMapper.updateModifyIPPortInfoACK(info);
        }catch (Exception e){
            throw new QueueException(e.getMessage(), e);
        }
    }

    /**
     * 轮询数据库，发送修改IP&Port命令
     */
    public void pollingModifyIPPort(){
        List<ModifyIPPortInfo> infos = modifyIPPortInfoMapper.pollingModifyIPPort(serverNum);
        if (CollectionUtils.isEmpty(infos)){
            return;
        }
        for (ModifyIPPortInfo info : infos){
            String gprsId = info.getGprsId();
            Date now = new Date();
            info.setSendTime(now);
            if (sendModifyIPPortCMD(gprsId,info)){
                info.setSendDone(1);
                modifyIPPortInfoMapper.updateModifyIPPortInfo(info);
            }else {
                info.setSendDone(4);
                modifyIPPortInfoMapper.updateModifyIPPortInfo(info);
            }
        }
    }

    /**
     * 发送修改IP&Port命令
     * @param gprsId
     * @param info
     * @return
     */
    private boolean sendModifyIPPortCMD(String gprsId,ModifyIPPortInfo info){
        try {
            Channel channel = CacheUtil.getGprsChannelMap().get(gprsId);
            if (channel == null){
                logger.info("设备[{}]已离线，IP&Port修改指令发送失败",gprsId);
                return false;
            }
            String[] ips = info.getIpNew().split("\\.");
            if (ips.length != 4){
                logger.warn("[{}]ip设置错误",gprsId);
                return false;
            }
            byte[] request = new byte[11];
            request[3] = (byte) 0x18;
            request[4] = Integer.valueOf(ips[0]).byteValue();
            request[5] = Integer.valueOf(ips[1]).byteValue();
            request[6] = Integer.valueOf(ips[2]).byteValue();
            request[7] = Integer.valueOf(ips[3]).byteValue();
            System.arraycopy(ByteExchangeUtil.intToUInt16Bytes(info.getPortNew()),0,request,8,2);
            request = ProtocolUtil.beforeSend(request);
            ByteBuf buf = channel.alloc().directBuffer(11);
            buf.writeBytes(request);
            channel.writeAndFlush(buf);
            logger.info("设备[{}]IP&Port修改指令发送成功，[{}]",gprsId,StringUtil.toHexString(request));
            return true;
        }catch (Exception e){
            logger.error("[{}]IP&Port修改指令发送失败",gprsId,e);
            return false;
        }
    }
}
