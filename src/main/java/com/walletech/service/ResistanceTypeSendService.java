package com.walletech.service;

import com.walletech.dao.mapper.ResistanceTypeReadInfoMapper;
import com.walletech.dao.mapper.ResistanceTypeSendInfoMapper;
import com.walletech.po.ResistanceTypeReadInfo;
import com.walletech.po.ResistanceTypeSendInfo;
import com.walletech.queue.exception.QueueException;
import com.walletech.queue.message.ResistanceTypeSendACKMessage;
import com.walletech.util.ByteExchangeUtil;
import com.walletech.util.CacheUtil;
import com.walletech.util.ProtocolUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
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
public class ResistanceTypeSendService {
    private static final Logger logger = LoggerFactory.getLogger(ResistanceTypeSendService.class);
    @Autowired
    private ResistanceTypeSendInfoMapper resistanceTypeSendInfoMapper;
    @Autowired
    private ResistanceTypeReadInfoMapper resistanceTypeReadInfoMapper;
    @Value("${server.num}")
    private Integer serverNum;

    /**
     * 处理响应
     * @param message
     */
    public void doService(ResistanceTypeSendACKMessage message) throws QueueException {
        try {
            ResistanceTypeSendInfo info = message.getResistanceTypeSendInfo();
            resistanceTypeSendInfoMapper.updateResistanceTypeSendACK(info);
        }catch (Exception e){
            throw new QueueException(e.getMessage(), e);
        }
    }

    /**
     * 轮询数据库，发送配置
     */
    public void pollingResistanceTypeSend(){
        List<ResistanceTypeSendInfo> infos = resistanceTypeSendInfoMapper.pollingResistanceTypeSend(serverNum);
        if (CollectionUtils.isEmpty(infos)){
            return;
        }
        for (ResistanceTypeSendInfo info : infos){
            String gprsId = info.getGprsId();
            Date now = new Date();
            info.setSendTime(now);
            if (sendModifyResistanceTypeCMD(gprsId,info)){
                info.setSendDone((byte) 1);
                resistanceTypeSendInfoMapper.updateResistanceTypeSendInfo(info);
                //重新读取内阻计算参数 将命令插入read表中
                ResistanceTypeReadInfo readInfo = new ResistanceTypeReadInfo();
                readInfo.setGprsId(gprsId);
                readInfo.setSendDone((byte) 0);
                readInfo.setSubNum(info.getSubNum());
                resistanceTypeReadInfoMapper.insertSelective(readInfo);
            }else {
                info.setSendDone((byte) 4);
                resistanceTypeSendInfoMapper.updateResistanceTypeSendInfo(info);
            }
        }
    }

    /**
     * 发送配置内阻计算参数命令
     * @param gprsId
     * @param info
     * @return
     */
    private boolean sendModifyResistanceTypeCMD(String gprsId ,ResistanceTypeSendInfo info){
        try {
            Channel channel = CacheUtil.getGprsChannelMap().get(gprsId);
            if (channel == null){
                logger.info("设备[{}]已离线，配置内阻计算参数命令发送失败",gprsId);
                return false;
            }
            byte[] request = new byte[11];
            request[3] = (byte) 0x2a;
            request[4] = info.getSubNum();
            request[5] = info.getType();
            System.arraycopy(ByteExchangeUtil.intToUInt16Bytes(info.getMaxVolPoint()),0,request,6,2);
            request[8] = info.getMinVolPoint().byteValue();
            request[9] = info.getAveCurPoint().byteValue();
            request = ProtocolUtil.beforeSend(request);
            ByteBuf buf = channel.alloc().directBuffer(11);
            buf.writeBytes(request);
            channel.writeAndFlush(buf);
            logger.info("设备[{}]配置内阻计算参数命令发送成功，[{}]",gprsId,StringUtil.toHexString(request));
            return true;
        }catch (Exception e){
            logger.error("设备[{}]配置内阻计算参数命令发送失败",gprsId,e);
            return false;
        }
    }

}
