package com.walletech.service;

import com.walletech.dao.mapper.SubBalanceStatusSendMapper;
import com.walletech.po.SubBalanceConfigSend;
import com.walletech.po.SubBalanceStatusSend;
import com.walletech.queue.exception.QueueException;
import com.walletech.queue.message.SubBalanceStatusACKMessage;
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
public class SubBalanceControlService {

    private static final Logger logger = LoggerFactory.getLogger(SubBalanceControlService.class);

    @Value("${server.num}")
    private Integer serverNum;
    @Value("${request.timeOut}")
    private Integer timeOut;

    @Autowired
    private SubBalanceStatusSendMapper subBalanceStatusSendMapper;

    /**
     * 轮询数据库，发送均衡从机状态修改
     */
    public void pollingSubBalanceStatusSend(){
        List<SubBalanceStatusSend> sends = subBalanceStatusSendMapper.pollingSubBalanceStatusSend(serverNum);
        if (CollectionUtils.isEmpty(sends)){
            return;
        }
        for (SubBalanceStatusSend send : sends){
            if (CacheUtil.getSubBalanceStatusSend(send.getGprsId())!=null){
                logger.debug("[{}] 有均衡从机状态修改正在处理,等待下次轮询",send.getGprsId());
                return;
            }
            doSendSubBalanceStatusCMD(send);
        }
    }

    /**
     * 发送均衡从机状态修改接口
     * @param send
     */
    public void doSendSubBalanceStatusCMD(SubBalanceStatusSend send){
        String gprsId = send.getGprsId();
        send.setInsertTime(new Date());
        if (!sendSubBalanceStatus(send,gprsId)){
            send.setSendDone(4);
        }else {
            send.setSendDone(1);
        }
        subBalanceStatusSendMapper.updateSubBalanceStatusSend(send);
    }

    /**
     * 发送均衡从机状态修改
     * @param send
     * @param gprsId
     * @return
     */
    private boolean sendSubBalanceStatus(SubBalanceStatusSend send , String gprsId){
        try {
            Channel channel = CacheUtil.getGprsChannelMap().get(gprsId);
            if (channel == null){
                logger.warn("设备[{}]已离线，均衡从机状态修改发送失败",gprsId);
                return false;
            }
            byte[] request = getSendBytes(send);
            ByteBuf buf = channel.alloc().directBuffer(7);
            buf.writeBytes(request);
            channel.writeAndFlush(buf);
            logger.info("设备[{}] [{}] 均衡从机状态修改发送成功，[{}]",gprsId,send.getBalanceSubdeviceCode(),
                    StringUtil.toHexString(request));
            CacheUtil.putSubBalanceStatusSend(gprsId,send);
            return true;
        } catch (Exception e){
            logger.error("设备[{}] [{}] 均衡从机均衡参数发送失败",gprsId,send.getBalanceSubdeviceCode(),e);
            return false;
        }
    }

    /**
     * 获取发送均衡从机状态修改的byte[]
     * @param send
     * @return
     */
    private byte[] getSendBytes(SubBalanceStatusSend send){
        byte[] request = new byte[7];
        request[3] = (byte) 0x32;
        request[4] = send.getBalanceSubdeviceCode();
        request[5] = Integer.valueOf(Integer.toBinaryString(send.getDownBalance()) + Integer.toBinaryString(send.getUpBalance()),2).byteValue();
        request = ProtocolUtil.beforeSend(request);
        return request;
    }

    /**
     * 处理均衡从机状态修改ACK
     * @param message
     * @throws QueueException
     */
    public void doService(SubBalanceStatusACKMessage message) throws QueueException {
        try {
            SubBalanceStatusSend send = message.getSend();
            subBalanceStatusSendMapper.updateSubBalanceStatusSend(send);
            CacheUtil.removeSubBalanceStatusSend(send.getGprsId());

            if (send.getSendDone() == 2) readRealTimeInfo(send.getGprsId());
        } catch (Exception e){
            throw new QueueException(e.getMessage(), e);
        }
    }

    /**
     * 检查超时未响应的均衡从机状态修改
     */
    public void checkTimeOut(){
        if (CollectionUtils.isEmpty(CacheUtil.getSubBalanceStatusSendMap().values())){
            return;
        }
        Date now = new Date();
        for(SubBalanceStatusSend info : CacheUtil.getSubBalanceStatusSendMap().values()){
            if (now.getTime() - info.getInsertTime().getTime() >= timeOut * 60 * 1000){
                if (subBalanceStatusSendMapper.checkTimeOut(info)==null){
                    CacheUtil.getSubBalanceStatusSendMap().remove(info.getGprsId());
                    continue;
                }
                logger.warn("设备[{}] [{}]均衡从机状态修改响应超时",info.getGprsId(),info.getBalanceSubdeviceCode());
                info.setSendDone(6);
                info.setInsertTime(now);
                subBalanceStatusSendMapper.updateSubBalanceStatusSend(info);
                CacheUtil.removeSubBalanceStatusSend(info.getGprsId());
            }
        }
    }

    /**
     * 均衡从机状态修改成功后,请求设备返回实时信息
     * @param gprsId
     */
    private void readRealTimeInfo(String gprsId){
        Channel channel = CacheUtil.getGprsChannelMap().get(gprsId);
        if (channel == null){
            logger.warn("设备[{}]已离线，请求设备返回实时信息失败",gprsId);
            return;
        }
        byte[] request = new byte[6];
        request[3] = (byte) 0x2f;
        request[4] = (byte) 0xff;
        request = ProtocolUtil.beforeSend(request);
        ByteBuf buf = channel.alloc().directBuffer(6);
        buf.writeBytes(request);
        channel.writeAndFlush(buf);
        logger.info("设备[{}]请求设备返回实时信息成功 [{}]",gprsId,StringUtil.toHexString(request));
    }

}
