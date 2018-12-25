package com.walletech.rabbit;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.walletech.po.GprsStateSnapshot;
import com.walletech.po.SubBalanceConfigSend;
import com.walletech.queue.message.SubBalanceConfigACKMessage;
import com.walletech.service.SubBalanceConfigSendService;
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

public class BalanceConfigListener implements ChannelAwareMessageListener {

    private static final Logger logger = LoggerFactory.getLogger(BalanceConfigListener.class);

    @Value("${server.num}")
    private Integer serverNum;
    @Value("${rabbit.retry.time}")
    private Integer retryTime;
    @Value("${rabbit.delayMsg.timeOut}")
    private Integer msgTimeOut;
    @Autowired
    private SubBalanceConfigSendService subBalanceConfigSendService;
    @Autowired
    private RabbitTemplate template;

    //按照GPRSID保存收到的最后应发送命令的时间
    private HashMap<String,Date> rcvTimeMap = new HashMap<>();

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            String json = new String(message.getBody(),"UTF-8");
            logger.info("收到均衡从机参数配置命令："+json);
            SubBalanceConfigSend send = JSON.parseObject(json,SubBalanceConfigSend.class);
            String gprsId = send.getGprsId();
            GprsStateSnapshot gprsStateSnapshot = ProtocolUtil.getStateSnapshotByGprsId(gprsId);

            if (send.getMqRcvTime() == null){
                send.setMqRcvTime(new Date());
            }

            if (new Date().getTime() - send.getMqRcvTime().getTime() > msgTimeOut*60*60*1000){
                logger.warn("命令积压超时,均衡从机参数配置命令丢弃:[{}]",json);
                SubBalanceConfigACKMessage subBalanceConfigACKMessage = new SubBalanceConfigACKMessage();
                send.setSendDone(7);
                send.setEndTime(new Date());
                subBalanceConfigACKMessage.setSend(send);
                subBalanceConfigSendService.doService(subBalanceConfigACKMessage);
                channel.basicAck(deliveryTag,false);
                return;
            }

            if (gprsStateSnapshot == null ){
                logger.warn("[{}]未连接过网关,请检查命令,丢弃均衡从机参数配置命令[{}]",gprsId,json);
                channel.basicAck(deliveryTag,false);
                return;
            }

            String routingKey = RabbitMqCommon.DELAY_ROUTING_KEY_SUB_BALANCE_CONFIG + serverNum;

            if (!gprsStateSnapshot.isLinkStatus()){
                logger.info("[{}]不在线，延迟处理均衡从机参数配置命令",gprsId);
                rcvTimeMap.put(gprsId,send.getMqRcvTime());
                RabbitUtil.sendDelayMessage2Mq(template,JSON.toJSONString(send).getBytes(),routingKey,retryTime);
                channel.basicAck(deliveryTag,false);
                return;
            }

            if (CacheUtil.getSubBalanceConfigSend(gprsId)!=null){
                logger.info("设备[{}]有正在处理的均衡从机参数配置命令,延迟处理",gprsId);
                RabbitUtil.sendDelayMessage2Mq(template,JSON.toJSONString(send).getBytes(),routingKey,retryTime);
                channel.basicAck(deliveryTag,false);
                return;
            }

            if (CacheUtil.getGprsMap().get(gprsId) == null){
                if (!serverNum.equals(gprsStateSnapshot.getServerNum())){
                    logger.debug("设备[{}]未连接本机,不处理其均衡从机参数配置命令",gprsId);
                    channel.basicAck(deliveryTag,false);
                }else {
                    logger.debug("设备[{}]与本机断连,延时等待刷新快照后处理");
                    rcvTimeMap.put(gprsId,send.getMqRcvTime());
                    RabbitUtil.sendDelayMessage2Mq(template,JSON.toJSONString(send).getBytes(),routingKey,retryTime);
                    channel.basicAck(deliveryTag,false);
                }
                return;
            }

            //没有积压消息时，直接发送
            if (rcvTimeMap.get(gprsId) == null){
                subBalanceConfigSendService.doSendSubBalanceConfig(send);
                channel.basicAck(deliveryTag,false);
                return;
            }

            //有积压消息时，保证最后一条命令最后发送
            if (send.getMqRcvTime().before(rcvTimeMap.get(gprsId))) {
                subBalanceConfigSendService.doSendSubBalanceConfig(send);
            } else if (send.getMqRcvTime().equals(rcvTimeMap.get(gprsId))){
                subBalanceConfigSendService.doSendSubBalanceConfig(send);
                rcvTimeMap.remove(gprsId);
            } else {
                rcvTimeMap.put(gprsId,send.getMqRcvTime());
                logger.debug("此条命令之前存在未发送的命令,等待重试, [{}]",json);
                RabbitUtil.sendDelayMessage2Mq(template,JSON.toJSONString(send).getBytes(),routingKey,retryTime);
            }

            channel.basicAck(deliveryTag,false);
        } catch (Exception e) {
            logger.warn("均衡从机参数配置处理失败",e);
            try {
                channel.basicAck(deliveryTag,false);
            } catch (IOException e1) {
                logger.error("",e1);
            }
        }

    }
}
