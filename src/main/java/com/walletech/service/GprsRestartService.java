package com.walletech.service;

import com.walletech.dao.mapper.GprsRestartInfoMapper;
import com.walletech.po.GprsRestartInfo;
import com.walletech.queue.exception.QueueException;
import com.walletech.queue.message.GprsRestartACKMessage;
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
public class GprsRestartService {

    private static final Logger logger = LoggerFactory.getLogger(GprsRestartService.class);

    @Autowired
    private GprsRestartInfoMapper gprsRestartInfoMapper;
    @Value("${server.num}")
    private Integer serverNum;

    public void doService(GprsRestartACKMessage message) throws QueueException {
        try {
            GprsRestartInfo info = message.getInfo();
            gprsRestartInfoMapper.updateGprsRestartACK(info);
        }catch (Exception e){
            throw new QueueException(e.getMessage(), e);
        }
    }


    public void pollingGprsRestartInfo(){
        List<GprsRestartInfo> infos = gprsRestartInfoMapper.pollingGprsRestartInfo(serverNum);
        if (CollectionUtils.isEmpty(infos)){
            return;
        }
        Date now = new Date();
        for (GprsRestartInfo info:infos){
            info.setSendTime(now);
            if (sendGprsRestartCMD(info,info.getGprsId())){
                info.setSendDone(1);
            }else {
                info.setSendDone(4);
            }
            gprsRestartInfoMapper.updateGprsRestartSendStatus(info);
        }
    }

    /**
     * 发送设备重启命令
     * @param info
     * @param gprsId
     */
    public void beginSendGprsRestartCMD(GprsRestartInfo info,String gprsId){
        Date now = new Date();
        info.setSendTime(now);
        if (sendGprsRestartCMD(info,gprsId)){
            info.setSendDone(1);
        }else {
            info.setSendDone(4);
        }
        gprsRestartInfoMapper.insertGprsRestartInfo(info);
    }


    private boolean sendGprsRestartCMD(GprsRestartInfo info, String gprsId){
        try {
            Channel channel = CacheUtil.getGprsChannelMap().get(gprsId);
            if (channel == null){
                logger.warn("设备[{}]已离线，设备重启命令发送失败",gprsId);
                return false;
            }

            byte[] request = new byte[6];
            request[3] = (byte) 0x17;
            String cellIndexStr = ProtocolUtil.supplementZero(Integer.toBinaryString(info.getCellIndex()),7);
            String data = info.getType() + cellIndexStr;
            request[4] = Integer.valueOf(data,2).byteValue();
            request = ProtocolUtil.beforeSend(request);

            ByteBuf buf = channel.alloc().directBuffer(6);
            buf.writeBytes(request);
            channel.writeAndFlush(buf);
            logger.info("设备[{}] [{}]重启命令发送成功，[{}]",gprsId,info.getCellIndex(),StringUtil.toHexString(request));
            return true;
        }catch (Exception e){
            logger.info("设备[{}] [{}]重启命令发送失败",gprsId,info.getCellIndex(),e);
            return false;
        }
    }



}
