package com.walletech.service;

import com.alibaba.fastjson.JSON;
import com.walletech.dao.mapper.ModifyGprsIdInfoMapper;
import com.walletech.po.ModifyGprsIdInfo;
import com.walletech.queue.exception.QueueException;
import com.walletech.queue.message.ModifyGprsIdACKMessage;
import com.walletech.util.CacheUtil;
import com.walletech.util.ProtocolUtil;
import com.walletech.util.RabbitUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
public class ModifyGprsIdService {
    private static final Logger logger = LoggerFactory.getLogger(ModifyGprsIdService.class);
    @Autowired
    private ModifyGprsIdInfoMapper modifyGprsIdInfoMapper;
    @Value("${server.num}")
    private Integer serverNum;
    @Value("${request.timeOut}")
    private Integer timeOut;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String ROUTING_KEY = "resultOfUpdateGprsId";

    public void doService(ModifyGprsIdACKMessage message) throws QueueException {
        try {
            ModifyGprsIdInfo info = message.getModifyGprsIdInfo();
            modifyGprsIdInfoMapper.updateModifyGprsIdInfo(info);
            CacheUtil.removeModifyGprsIdInfo(info.getGprsId());
            try {
                info.setState((byte) 0);
                RabbitUtil.sendResultOfModifyGprsIdMessage2Mq(rabbitTemplate,JSON.toJSONString(info).getBytes(),ROUTING_KEY);
            }catch (Exception e){
                logger.error("",e);

                CacheUtil.SEND_MODIFYGPRSID_ACK_FAILED_LIST.add(info);
            }
        }catch (Exception e){
            throw new QueueException(e.getMessage(), e);
        }
    }

    /**
     * 轮询GprsId修改命令
     */
    public void pollingModifyGprsId(){
        List<ModifyGprsIdInfo> infos = modifyGprsIdInfoMapper.pollingModifyGprsId(serverNum);
        if (CollectionUtils.isEmpty(infos)){
            return;
        }
        for (ModifyGprsIdInfo info : infos){
            String gprsId = info.getGprsId();
            if (CacheUtil.getModifyGprsIdInfo(gprsId)!=null){
                logger.debug("设备[{}]有GprsId修改指令正在处理,等待下次轮询",gprsId);
                continue;
            }
            beginSendModifyGprsIdCMD(info, gprsId);
        }
    }

    public void beginSendModifyGprsIdCMD(ModifyGprsIdInfo info, String gprsId) {
        Date now = new Date();
        info.setTime(now);
        if (sendModifyGprsIdCMD(gprsId,info)){
            info.setSendDone((byte) 1);
            modifyGprsIdInfoMapper.updateModifyGprsIdInfo(info);
            CacheUtil.putModifyGprsIdInfo(gprsId,info);
        }else {
            info.setSendDone((byte) 4);
            modifyGprsIdInfoMapper.updateModifyGprsIdInfo(info);
            RabbitUtil.sendResultOfModifyGprsIdMessage2Mq(rabbitTemplate,JSON.toJSONString(info).getBytes(),ROUTING_KEY);
        }
    }

    /**
     * 发送GprsId修改指令
     */
    private boolean sendModifyGprsIdCMD(String gprsId,ModifyGprsIdInfo info){
        try {
            Channel channel = CacheUtil.getGprsChannelMap().get(gprsId);
            if (channel == null){
                logger.info("设备[{}]已离线，GprsId修改指令发送失败",gprsId);
                return false;
            }
            byte[] request;
            if (info.getType() == 1){
                request = getModifyHostIdBytes(info);
            }else if (info.getType() == 2){
                request = getModifySlaveIdBytes(info);
            }else {
                logger.warn("设备[{}]GprsId修改指令类型错误",gprsId);
                return false;
            }
            if (request == null){
                logger.warn("设备[{}]GprsId修改指令参数错误",gprsId);
                return false;
            }
            ByteBuf buf = channel.alloc().directBuffer(request.length);
            buf.writeBytes(request);
            channel.writeAndFlush(buf);
            if (info.getType() == 1){
                logger.info("设备[{}]修改主机ID指令发送成功,[{}]修改为：[{}],[{}]",
                        gprsId,info.getOuterId(),info.getInnerId(),StringUtil.toHexString(request));
            }else {
                logger.info("设备[{}]修改从机ID指令发送成功,[{}]修改为：[{}],[{}]",
                        gprsId,info.getOuterId(),info.getInnerId(),StringUtil.toHexString(request));
            }
            return true;
        }catch (Exception e){
            logger.error("设备[{}]GprsId修改指令发送失败",gprsId,e);
            return false;
        }
    }

    /**
     * 获取修改主机ID修改命令
     * @param info
     * @return
     */
    private byte[] getModifyHostIdBytes(ModifyGprsIdInfo info){
        byte[] request = new byte[17];
        request[3] = (byte) 0x1a;
        byte[] oldId = ProtocolUtil.getGprsIdBytes(info.getOuterId());
        byte[] newId = ProtocolUtil.getGprsIdBytes(info.getInnerId());
        if (oldId == null || newId == null){
            return null;
        }
        System.arraycopy(oldId,0,request,4,6);
        System.arraycopy(newId,0,request,10,6);
        request = ProtocolUtil.beforeSend(request);
        return request;
    }

    /**
     * 获取从机ID修改命令
     * @param info
     * @return
     */
    private byte[] getModifySlaveIdBytes(ModifyGprsIdInfo info){
        byte[] request = new byte[13];
        request[3] = (byte) 0x1b;
        byte[] oldId = ProtocolUtil.getSlaveIdBytes(info.getOuterId());
        byte[] newId = ProtocolUtil.getSlaveIdBytes(info.getInnerId());
        if (oldId == null || newId == null){
            return null;
        }
        System.arraycopy(oldId,0,request,4,4);
        System.arraycopy(newId,0,request,8,4);
        request = ProtocolUtil.beforeSend(request);
        return request;
    }

    /**
     * 检查超时的GprsId修改指令
     */
    public void checkTimeOut(){
        if (CollectionUtils.isEmpty(CacheUtil.getModifyGprsidMap())){
            return;
        }
        for(ModifyGprsIdInfo info : CacheUtil.getModifyGprsidMap().values()){
            Date now = new Date();
            if (now.getTime() - info.getTime().getTime() >= timeOut * 60 * 1000){
                if (modifyGprsIdInfoMapper.checkTimeOut(info)==null){
                    CacheUtil.getModifyGprsidMap().remove(info.getGprsId());
                    continue;
                }
                logger.warn("[{}]GprsId修改应答超时",info.getGprsId());
                info.setSendDone((byte) 6);
                modifyGprsIdInfoMapper.updateModifyGprsIdInfo(info);
                CacheUtil.removeModifyGprsIdInfo(info.getGprsId());
                RabbitUtil.sendResultOfModifyGprsIdMessage2Mq(rabbitTemplate,JSON.toJSONString(info).getBytes(),ROUTING_KEY);
            }
        }
    }
}
