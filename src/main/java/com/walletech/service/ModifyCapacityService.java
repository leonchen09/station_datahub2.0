package com.walletech.service;

import com.walletech.dao.mapper.ModifyCapacityInfoMapper;
import com.walletech.po.ModifyCapacityInfo;
import com.walletech.queue.exception.QueueException;
import com.walletech.queue.message.ModifyCapacityACKMessage;
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
public class ModifyCapacityService {
    private static final Logger logger = LoggerFactory.getLogger(ModifyCapacityService.class);
    @Autowired
    private ModifyCapacityInfoMapper modifyCapacityInfoMapper;
    @Value("${server.num}")
    private Integer serverNum;
    @Value("${request.timeOut}")
    private Integer timeOut;

    public void doService(ModifyCapacityACKMessage message) throws QueueException {
        try {
            modifyCapacityInfoMapper.updateModifyCapacityInfo(message.getModifyCapacityInfo());
            CacheUtil.removeModifyCapacityInfo(message.getModifyCapacityInfo().getGprsId());
        }catch (Exception e){
            throw new QueueException(e.getMessage(), e);
        }
    }

    public void pollingModifyCapacity(){
        List<ModifyCapacityInfo> infos = modifyCapacityInfoMapper.pollingModifyCapacity(serverNum);
        if (CollectionUtils.isEmpty(infos)){
            return;
        }
        for(ModifyCapacityInfo info : infos){
            String gprsId = info.getGprsId();
            if (CacheUtil.getModifyCapacityInfo(gprsId)!=null){
                logger.debug("设备[{}]有修改额定容量指令正在处理,等待下次轮询",gprsId);
                continue;
            }
            doSendModifyCapacityInfo(info, gprsId);
        }
    }

    public void doSendModifyCapacityInfo(ModifyCapacityInfo info, String gprsId) {
        Date now = new Date();
        if (sendModifyCapacityCMD(gprsId,info)){
            info.setSendTime(now);
            info.setSendDone((byte) 1);
            modifyCapacityInfoMapper.updateModifyCapacityInfo(info);
            CacheUtil.putModifyCapacityInfo(gprsId,info);
        }else {
            info.setSendDone((byte) 4);
            info.setSendTime(now);
            modifyCapacityInfoMapper.updateModifyCapacityInfo(info);
        }
    }

    /**
     * 发送修改电池容量命令
     */
    private boolean sendModifyCapacityCMD(String gprsId, ModifyCapacityInfo info){
        try {
            Channel channel = CacheUtil.getGprsChannelMap().get(gprsId);
            if (channel == null){
                logger.warn("设备[{}]已离线，修改电池容量命令发送失败",gprsId);
                return false;
            }
            byte[] request = new byte[7];
            request[3] = (byte) 0x21;
            byte[] capacity = ByteExchangeUtil.intToUInt16Bytes(info.getCapacity() * 100);
            System.arraycopy(capacity,0,request,4,2);
            request = ProtocolUtil.beforeSend(request);
            ByteBuf buf = channel.alloc().directBuffer(7);
            buf.writeBytes(request);
            channel.writeAndFlush(buf);
            logger.info("设备[{}]修改电池容量命令发送成功，[{}]",gprsId,StringUtil.toHexString(request));
            return true;
        } catch (Exception e){
            logger.error("设备[{}]修改电池容量命令发送失败",gprsId,e);
            return false;
        }
    }

    /**
     * 检查超时的容量修改指令
     */
    public void checkTimeOut(){
        if (CollectionUtils.isEmpty(CacheUtil.getModifyCapacityMap())){
            return;
        }
        for(ModifyCapacityInfo info : CacheUtil.getModifyCapacityMap().values()){
            Date now = new Date();
            if (now.getTime() - info.getSendTime().getTime() >= timeOut * 60 * 1000){
                if (modifyCapacityInfoMapper.checkTimeOut(info)==null){
                    CacheUtil.getModifyCapacityMap().remove(info.getGprsId());
                    continue;
                }
                logger.warn("[{}]额定容量修改应答超时",info.getGprsId());
                info.setSendDone((byte) 6);
                modifyCapacityInfoMapper.updateModifyCapacityInfo(info);
                CacheUtil.removeModifyCapacityInfo(info.getGprsId());
            }
        }
    }
}
