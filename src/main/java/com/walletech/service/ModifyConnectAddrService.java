package com.walletech.service;

import com.walletech.dao.mapper.ModifyConnectAddrInfoMapper;
import com.walletech.po.ModifyConnectAddrInfo;
import com.walletech.queue.exception.QueueException;
import com.walletech.queue.message.ModifyConnectAddrACKMessage;
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
public class ModifyConnectAddrService {
    private static final Logger logger = LoggerFactory.getLogger(ModifyConnectAddrService.class);
    @Autowired
    private ModifyConnectAddrInfoMapper modifyConnectAddrInfoMapper;
    @Value("${server.num}")
    private Integer serverNum;

    public void doService(ModifyConnectAddrACKMessage message) throws QueueException {
        try {
            ModifyConnectAddrInfo info = message.getModifyConnectAddrInfo();
            modifyConnectAddrInfoMapper.updateModifyConnectAddrInfoACK(info);
        }catch (Exception e){
            throw new QueueException(e.getMessage(), e);
        }
    }

    /**
     * 轮询数据库,发送网络地址配置
     */
    public void pollingModifyConnectAddr(){
        List<ModifyConnectAddrInfo> infos = modifyConnectAddrInfoMapper.pollingModifyConnectAddr(serverNum);
        if (CollectionUtils.isEmpty(infos)){
            return;
        }
        for (ModifyConnectAddrInfo info : infos){
            String gprsId = info.getGprsId();
            Date now = new Date();
            info.setSendTime(now);
            if (sendModifyConnectAddrCMD(gprsId,info)){
                info.setSendDone(1);
                modifyConnectAddrInfoMapper.updateModifyConnectAddrInfo(info);
            }else {
                info.setSendDone(4);
                modifyConnectAddrInfoMapper.updateModifyConnectAddrInfo(info);
            }
        }
    }

    /**
     * 发送网络地址配置命令
     * @param gprsId
     * @param info
     * @return
     */
    private boolean sendModifyConnectAddrCMD(String gprsId,ModifyConnectAddrInfo info){
        try {
            Channel channel = CacheUtil.getGprsChannelMap().get(gprsId);
            if (channel == null){
                logger.warn("设备[{}]已离线，网络地址配置发送失败",gprsId);
                return false;
            }
            byte[] addr = (info.getAdress1()+"#"+info.getAdress2()+"#"+info.getAdress3()+"#"+info.getAdress4()+"#").getBytes();
            byte[] request = new byte[addr.length+4+5];
            request[3] = (byte) 0x27;
            request[4] = info.getType1().byteValue();
            request[5] = info.getType2().byteValue();
            request[6] = info.getType3().byteValue();
            request[7] = info.getType4().byteValue();
            System.arraycopy(addr,0,request,8,addr.length);
            request = ProtocolUtil.beforeSend(request);
            ByteBuf buf = channel.alloc().directBuffer(request.length);
            buf.writeBytes(request);
            channel.writeAndFlush(buf);
            logger.info("设备[{}]网络地址配置指令发送成功，[{}]",gprsId,StringUtil.toHexString(request));
            return true;
        }catch (Exception e){
            logger.error("[{}]网络地址配置发送失败",gprsId,e);
            return false;
        }
    }
}
