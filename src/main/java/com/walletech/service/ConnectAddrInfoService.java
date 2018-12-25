package com.walletech.service;

import com.walletech.dao.mapper.ConnectAddrInfoMapper;
import com.walletech.po.ConnectAddrInfo;
import com.walletech.queue.exception.QueueException;
import com.walletech.queue.message.ConnectAddrReadMessage;
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
public class ConnectAddrInfoService {
    private static final Logger logger = LoggerFactory.getLogger(ConnectAddrInfoService.class);
    @Autowired
    private ConnectAddrInfoMapper connectAddrInfoMapper;
    @Value("${server.num}")
    private Integer serverNum;

    /**
     * 处理读取网络地址配置命令应答
     * @param message
     */
    public void doService(ConnectAddrReadMessage message) throws QueueException {
        try {
            ConnectAddrInfo info = message.getConnectAddrInfo();
            connectAddrInfoMapper.replaceConnectAddrInfo(info);
        }catch (Exception e){
            throw new QueueException(e.getMessage(), e);
        }
    }

    /**
     * 轮询数据库，发送读取网络地址配置命令
     */
    public void pollingConnectAddrInfoRead(){
        List<ConnectAddrInfo> infos = connectAddrInfoMapper.pollingConnectAddrInfoRead(serverNum);
        if (CollectionUtils.isEmpty(infos)){
            return;
        }
        for (ConnectAddrInfo info : infos){
            String gprsId = info.getGprsId();
            Date now = new Date();
            info.setSendTime(now);
            if (sendConnectAddrInfoReadCMD(gprsId)){
                info.setSendDone(1);
            }else {
                info.setSendDone(4);
            }
            connectAddrInfoMapper.updateConnectAddrInfo(info);
        }
    }

    /**
     * 发送读取网络地址配置命令
     * @param gprsId
     * @return
     */
    private boolean sendConnectAddrInfoReadCMD(String gprsId){
        try {
            Channel channel = CacheUtil.getGprsChannelMap().get(gprsId);
            if (channel == null){
                logger.info("设备[{}]已离线，网络地址读取指令命令发送失败",gprsId);
                return false;
            }
            byte[] request = new byte[5];
            request[3] = (byte) 0x26;
            request = ProtocolUtil.beforeSend(request);
            ByteBuf buf = channel.alloc().directBuffer(5);
            buf.writeBytes(request);
            channel.writeAndFlush(buf);
            logger.info("设备[{}]网络地址读取指令发送成功，[{}]",gprsId,StringUtil.toHexString(request));
            return true;
        }catch (Exception e){
            logger.error("设备[{}]网络地址读取指令命令发送失败",gprsId,e);
            return false;
        }
    }
}
