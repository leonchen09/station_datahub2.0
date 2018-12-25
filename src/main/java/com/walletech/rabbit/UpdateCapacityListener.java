package com.walletech.rabbit;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.walletech.po.GprsStateSnapshot;
import com.walletech.po.ModifyCapacityInfo;
import com.walletech.queue.message.ModifyCapacityACKMessage;
import com.walletech.service.ModifyCapacityService;
import com.walletech.util.CacheUtil;
import com.walletech.util.RabbitMqCommon;
import com.walletech.util.ProtocolUtil;
import com.walletech.util.RabbitUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

public class UpdateCapacityListener implements ChannelAwareMessageListener {

    private static final Logger logger = LoggerFactory.getLogger(UpdateCapacityListener.class);
    @Value("${server.num}")
    private Integer serverNum;
    @Value("${rabbit.retry.time}")
    private Integer retryTime;
    @Value("${rabbit.delayMsg.timeOut}")
    private Integer msgTimeOut;
    @Autowired
    private ModifyCapacityService modifyCapacityService;
    @Autowired
    private RabbitTemplate template;
    //按照GPRSID保存收到的最后应发送命令的时间
    private HashMap<String,Date> rcvTimeMap = new HashMap<>();

    @Override
    public void onMessage(Message message, Channel channel){
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            String json = new String(message.getBody(),"UTF-8");
            logger.info("收到设备容量修改命令:"+json);
            ModifyCapacityInfo info = JSON.parseObject(json, ModifyCapacityInfo.class);
            String gprsId = info.getGprsId();

            GprsStateSnapshot gprsStateSnapshot = ProtocolUtil.getStateSnapshotByGprsId(gprsId);

            if (info.getMqRcvTime() == null){
                info.setMqRcvTime(new Date());
            }

            if (new Date().getTime() - info.getMqRcvTime().getTime() > msgTimeOut*60*60*1000){
                logger.warn("命令积压超时,容量修改命令丢弃:[{}]",json);
                ModifyCapacityACKMessage modifyCapacityACKMessage = new ModifyCapacityACKMessage();
                info.setSendDone((byte) 7);
                modifyCapacityACKMessage.setModifyCapacityInfo(info);
                modifyCapacityService.doService(modifyCapacityACKMessage);
                channel.basicAck(deliveryTag,false);
                return;
            }

            if (gprsStateSnapshot == null ){
                logger.warn("[{}]未连接过网关,请检查命令,丢弃容量修改命令[{}]",gprsId,json);
                channel.basicAck(deliveryTag,false);
                return;
            }

            if (!gprsStateSnapshot.isLinkStatus()){
                logger.info("设备[{}]不在线，延迟处理容量修改命令",gprsId);
                rcvTimeMap.put(gprsId,info.getMqRcvTime());
                delayUpdateCapacity(info);
                channel.basicAck(deliveryTag,false);
                return;
            }

            if (CacheUtil.getModifyCapacityInfo(gprsId)!=null){
                logger.debug("设备[{}]有修改额定容量指令正在处理,延迟进行处理",gprsId);
                delayUpdateCapacity(info);
                channel.basicAck(deliveryTag,false);
                return;
            }

            if (CacheUtil.getGprsMap().get(gprsId) == null){
                if (!serverNum.equals(gprsStateSnapshot.getServerNum())){
                    logger.debug("设备[{}]未连接本机,不处理其修改额定容量命令",gprsId);
                    channel.basicAck(deliveryTag,false);
                }else {
                    logger.debug("设备[{}]与本机断连,延时等待刷新快照后处理");
                    rcvTimeMap.put(gprsId,info.getMqRcvTime());
                    delayUpdateCapacity(info);
                    channel.basicAck(deliveryTag,false);
                }
                return;
            }

            //没有积压消息时，直接发送
            if (rcvTimeMap.get(gprsId) == null){
                modifyCapacityService.doSendModifyCapacityInfo(info,gprsId);
                channel.basicAck(deliveryTag,false);
                return;
            }

            //有积压消息时，保证最后一条命令最后发送
            if (info.getMqRcvTime().before(rcvTimeMap.get(gprsId))) {
                modifyCapacityService.doSendModifyCapacityInfo(info,gprsId);
            } else if (info.getMqRcvTime().equals(rcvTimeMap.get(gprsId))){
                modifyCapacityService.doSendModifyCapacityInfo(info,gprsId);
                rcvTimeMap.remove(gprsId);
            } else {
                rcvTimeMap.put(gprsId,info.getMqRcvTime());
                logger.debug("此条命令之前存在未发送的命令,等待重试, [{}]",json);
                delayUpdateCapacity(info);
            }

            channel.basicAck(deliveryTag,false);
        }catch (Exception e){
            logger.warn("容量修改处理失败",e);
            try {
                channel.basicAck(deliveryTag,false);
            } catch (IOException e1) {
                logger.error("",e1);
            }
        }
    }

    private void delayUpdateCapacity(ModifyCapacityInfo info){
        RabbitUtil.sendDelayMessage2Mq(template,JSON.toJSONString(info).getBytes(),
                RabbitMqCommon.DELAY_ROUTING_KEY_UPDATE_CAPACITY + serverNum,retryTime);
    }

}
