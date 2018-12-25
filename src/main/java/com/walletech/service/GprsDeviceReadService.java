package com.walletech.service;

import com.walletech.dao.mapper.GprsDeviceInfoMapper;
import com.walletech.dao.mapper.GprsDeviceReadInfoMapper;
import com.walletech.po.GprsDeviceReadInfo;
import com.walletech.queue.exception.QueueException;
import com.walletech.queue.message.GprsDeviceReadMessage;
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

import java.util.List;

@Service
public class GprsDeviceReadService {

    private static final Logger logger = LoggerFactory.getLogger(GprsDeviceReadService.class);
    @Autowired
    private GprsDeviceReadInfoMapper gprsDeviceReadInfoMapper;
    @Autowired
    private GprsDeviceInfoMapper gprsDeviceInfoMapper;
    @Value("${server.num}")
    private Integer serverNum;

    public void doService(GprsDeviceReadMessage message) throws QueueException {
        try {
            gprsDeviceInfoMapper.insertOrUpdateGprsDeviceInfo(message.getGprsDeviceInfo());
        }catch (Exception e){
            throw new QueueException(e.getMessage(), e);
        }
    }

    public void pollingGprsDeviceReadInfo(){
        List<GprsDeviceReadInfo> readInfos = gprsDeviceReadInfoMapper.pollingGprsDeviceRead(serverNum);
        if (CollectionUtils.isEmpty(readInfos)){
            return;
        }
        for (GprsDeviceReadInfo info : readInfos){
            if (sendGprsDeviceReadCMD(info.getGprsId())){
                info.setReadDone(true);
                gprsDeviceReadInfoMapper.updateByPrimaryKeySelective(info);
            }
        }
    }

    /**
     * 发送参数查询指令
     */
    private boolean sendGprsDeviceReadCMD(String gprsId){
        try {
            Channel channel = CacheUtil.getGprsChannelMap().get(gprsId);
            if (channel==null){
                logger.warn("[{}]已掉线,无法发送参数查询指令",gprsId);
                return false;
            }
            byte[] request = new byte[5];
            request[3] = (byte) 0x11;
            request = ProtocolUtil.beforeSend(request);
            ByteBuf buf = channel.alloc().directBuffer(5);
            buf.writeBytes(request);
            channel.writeAndFlush(buf);
            logger.info("设备[{}]参数查询指令发送成功，[{}]",gprsId,StringUtil.toHexString(request));
            return true;
        } catch (Exception e){
            logger.error("设备[{}]参数查询指令发送失败",gprsId,e);
            return false;
        }
    }
}
