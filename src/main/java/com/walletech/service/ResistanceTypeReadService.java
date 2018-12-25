package com.walletech.service;

import com.walletech.dao.mapper.ResistanceTypeReadInfoMapper;
import com.walletech.po.ResistanceTypeReadInfo;
import com.walletech.queue.exception.QueueException;
import com.walletech.queue.message.ResistanceTypeReadMessage;
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
public class ResistanceTypeReadService {
    private static final Logger logger = LoggerFactory.getLogger(ResistanceTypeReadService.class);
    @Autowired
    private ResistanceTypeReadInfoMapper resistanceTypeReadInfoMapper;
    @Value("${server.num}")
    private Integer serverNum;

    /**
     * 处理设备返回的内阻计算参数
     * @param message
     */
    public void doService(ResistanceTypeReadMessage message) throws QueueException {
        try {
            ResistanceTypeReadInfo info = message.getResistanceTypeReadInfo();
            resistanceTypeReadInfoMapper.updateResistanceTypeReceive(info);
        }catch (Exception e){
            throw new QueueException(e.getMessage(), e);
        }
    }

    /**
     * 轮询数据库，发送读取内阻计算参数命令
     */
    public void pollingResistanceTypeRead(){
        List<ResistanceTypeReadInfo> infos = resistanceTypeReadInfoMapper.pollingResistanceTypeRead(serverNum);
        if (CollectionUtils.isEmpty(infos)){
            return;
        }
        for (ResistanceTypeReadInfo info : infos){
            String gprsId = info.getGprsId();
            Date now = new Date();
            info.setSendTime(now);
            if (sendResistanceTypeReadCMD(gprsId,info)){
                info.setSendDone((byte) 1);
            }else {
                info.setSendDone((byte) 4);
            }
            resistanceTypeReadInfoMapper.updateResistanceTypeReadInfo(info);
        }
    }

    /**
     * 发送读取内阻计算参数命令
     */
    private boolean sendResistanceTypeReadCMD(String gprsId, ResistanceTypeReadInfo info){
        try {
            Channel channel = CacheUtil.getGprsChannelMap().get(gprsId);
            if (channel == null){
                logger.info("设备[{}]已离线，读取内阻计算参数命令发送失败",gprsId);
                return false;
            }
            byte[] request = new byte[6];
            request[3] = (byte) 0x2b;
            request[4] = info.getSubNum();
            request = ProtocolUtil.beforeSend(request);
            ByteBuf buf = channel.alloc().directBuffer(6);
            buf.writeBytes(request);
            channel.writeAndFlush(buf);
            logger.debug("设备[{}]读取内阻计算参数命令发送成功,[{}]",gprsId,StringUtil.toHexString(request));
            return true;
        }catch (Exception e){
            logger.error("设备[{}]读取内阻计算参数命令发送失败",gprsId,e);
            return false;
        }
    }
}
