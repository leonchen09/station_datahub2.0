package com.walletech.service;

import com.walletech.dao.mapper.ModifyBalanceStrategyInfoMapper;
import com.walletech.po.GprsBalanceSendInfo;
import com.walletech.po.ModifyBalanceStrategyInfo;
import com.walletech.queue.exception.QueueException;
import com.walletech.queue.message.ModifyBalanceStrategyACKMessage;
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
public class ModifyBalanceStrategyService {
    private static final Logger logger = LoggerFactory.getLogger(ModifyBalanceStrategyService.class);
    @Autowired
    private ModifyBalanceStrategyInfoMapper modifyBalanceStrategyInfoMapper;
    @Value("${server.num}")
    private Integer serverNum;
    @Value("${request.timeOut}")
    private Integer timeOut;

    public void doService(ModifyBalanceStrategyACKMessage message) throws QueueException {
        try {
            modifyBalanceStrategyInfoMapper.updateModifyBalanceStrategyInfo(message.getModifyBalanceStrategyInfo());
            CacheUtil.removeBalanceStrategyInfo(message.getModifyBalanceStrategyInfo().getGprsId());
        }catch (Exception e){
            throw new QueueException(e.getMessage(), e);
        }
    }

    public void pollingModifyBalanceStrategy(){
        checkTimeOut();
        List<ModifyBalanceStrategyInfo> infos = modifyBalanceStrategyInfoMapper.pollingModifyBalanceStrategy(serverNum);
        if (CollectionUtils.isEmpty(infos)){
            return;
        }
        for (ModifyBalanceStrategyInfo info : infos){
            String gprsId = info.getGprsId();
            if (CacheUtil.getBalanceStrategyInfo(gprsId)!=null){
                logger.debug("设备[{}]有修改均衡策略指令正在处理,等待下次轮询",gprsId);
                continue;
            }
            Date now = new Date();
            info.setSendTime(now);
            if (sendModifyBalanceStrategyCMD(gprsId,info)){
                info.setSendDone((byte) 1);
                modifyBalanceStrategyInfoMapper.updateModifyBalanceStrategyInfo(info);
                CacheUtil.putBalanceStrategyInfo(gprsId,info);
            }else {
                info.setSendDone((byte) 4);
                modifyBalanceStrategyInfoMapper.updateModifyBalanceStrategyInfo(info);
            }
        }
    }

    /**
     * 发送修改均衡策略指令
     */
    private boolean sendModifyBalanceStrategyCMD(String gprsId,ModifyBalanceStrategyInfo info){
        try {
            Channel channel = CacheUtil.getGprsChannelMap().get(gprsId);
            if (channel == null){
                logger.warn("设备[{}]已离线，修改均衡策略发送失败",gprsId);
                return false;
            }
            byte[] request = new byte[13];
            request[3] = (byte) 0x22;
            request[4] = info.getPara1().byteValue();
            request[5] = info.getPara2().byteValue();
            request[6] = info.getPara3().byteValue();
            request[7] = info.getPara4().byteValue();
            request[8] = info.getPara5().byteValue();
            request[9] = (byte) (info.getPara6()+10);
            byte[] bytes = ByteExchangeUtil.intToUInt16Bytes(info.getPara7());
            System.arraycopy(bytes,0,request,10,2);
            request = ProtocolUtil.beforeSend(request);
            ByteBuf buf = channel.alloc().directBuffer(13);
            buf.writeBytes(request);
            channel.writeAndFlush(buf);
            logger.info("设备[{}]修改均衡策略发送成功，[{}]",gprsId,StringUtil.toHexString(request));
            return true;
        }catch (Exception e){
            logger.error("设备[{}]修改均衡策略发送失败",gprsId,e);
            return false;
        }
    }

    /**
     * 检查超时的均衡指令
     */
    private void checkTimeOut(){
        if (CollectionUtils.isEmpty(CacheUtil.getModifyBalanceStrategyMap())){
            return;
        }
        for(ModifyBalanceStrategyInfo info : CacheUtil.getModifyBalanceStrategyMap().values()){
            Date now = new Date();
            if (now.getTime() - info.getSendTime().getTime() >= timeOut * 60 * 1000){
                if (modifyBalanceStrategyInfoMapper.checkTimeOut(info)==null){
                    CacheUtil.removeBalanceStrategyInfo(info.getGprsId());
                    continue;
                }
                logger.warn("[{}]均衡策略修改 应答超时",info.getGprsId());
                info.setSendDone((byte) 6);
                modifyBalanceStrategyInfoMapper.updateModifyBalanceStrategyInfo(info);
                CacheUtil.removeBalanceStrategyInfo(info.getGprsId());
            }
        }
    }
}
