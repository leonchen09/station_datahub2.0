package com.walletech.rabbit;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.walletech.po.GprsDeviceSendInfo;
import com.walletech.po.GprsStateSnapshot;
import com.walletech.service.GprsDeviceSendService;
import com.walletech.util.CacheUtil;
import com.walletech.util.ProtocolUtil;
import com.walletech.util.RabbitMqCommon;
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

public class ParameterConfigListener implements ChannelAwareMessageListener {

    private static final Logger logger = LoggerFactory.getLogger(ParameterConfigListener.class);

    @Value("${server.num}")
    private Integer serverNum;
    @Value("${rabbit.retry.time}")
    private Integer retryTime;
    @Value("${rabbit.delayMsg.timeOut}")
    private Integer msgTimeOut;
    @Autowired
    private GprsDeviceSendService gprsDeviceSendService;
    @Autowired
    private RabbitTemplate template;
    //按照GPRSID保存收到的最后应发送命令的时间
    private HashMap<String,Date> rcvTimeMap = new HashMap<>();

    @Override
    public void onMessage(Message message, Channel channel)  {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            String json = new String(message.getBody(),"UTF-8");
            logger.info("收到参数配置命令："+json);
            GprsDeviceSendInfo info = JSON.parseObject(json, GprsDeviceSendInfo.class);
            String gprsId = info.getGprsId();
            GprsStateSnapshot gprsStateSnapshot = ProtocolUtil.getStateSnapshotByGprsId(gprsId);

            //第一次收到此条命令
            if (info.getMqRcvTime() == null){
                Date now = new Date();
                info.setMqRcvTime(now);
            }

            if (new Date().getTime() - info.getMqRcvTime().getTime() > msgTimeOut*60*60*1000){
                logger.warn("命令积压超时,参数配置命令丢弃:[{}]",json);
                gprsDeviceSendService.sendMsgTimeOut(info.getId());
                channel.basicAck(deliveryTag,false);
                return;
            }

            if (gprsStateSnapshot == null ){
                logger.warn("[{}]未连接过网关,请检查命令,丢弃参数配置命令[{}]",gprsId,json);
                channel.basicAck(deliveryTag,false);
                return;
            }

            String routingKey = RabbitMqCommon.DELAY_ROUTING_KEY_PARAMETER_CONFIG + serverNum;

            if (!gprsStateSnapshot.isLinkStatus()){
                logger.info("[{}]不在线，延迟处理参数配置命令",gprsId);
                rcvTimeMap.put(gprsId,info.getMqRcvTime());
                RabbitUtil.sendDelayMessage2Mq(template,JSON.toJSONString(info).getBytes(),routingKey,retryTime);
                channel.basicAck(deliveryTag,false);
                return;
            }

            if (CacheUtil.getGprsMap().get(gprsId) == null){
                if (!serverNum.equals(gprsStateSnapshot.getServerNum())){
                    logger.debug("设备[{}]未连接本机,不处理其参数配置命令",gprsId);
                    channel.basicAck(deliveryTag,false);
                }else {
                    logger.debug("设备[{}]与本机断连,延时等待刷新快照后处理");
                    rcvTimeMap.put(gprsId,info.getMqRcvTime());
                    RabbitUtil.sendDelayMessage2Mq(template,JSON.toJSONString(info).getBytes(),routingKey,retryTime);
                    channel.basicAck(deliveryTag,false);
                }
                return;
            }

            //没有积压消息时，直接发送
            if (rcvTimeMap.get(gprsId) == null){
                gprsDeviceSendService.doSendModifyConfig(info);
                channel.basicAck(deliveryTag,false);
                return;
            }

            //有积压消息时，保证最后一条命令最后发送
            if (info.getMqRcvTime().before(rcvTimeMap.get(gprsId))) {
                gprsDeviceSendService.doSendModifyConfig(info);
            } else if (info.getMqRcvTime().equals(rcvTimeMap.get(gprsId))){
                gprsDeviceSendService.doSendModifyConfig(info);
                rcvTimeMap.remove(gprsId);
            } else {
                rcvTimeMap.put(gprsId,info.getMqRcvTime());
                logger.debug("此条命令之前存在未发送的命令,等待重试, [{}]",json);
                RabbitUtil.sendDelayMessage2Mq(template,JSON.toJSONString(info).getBytes(),routingKey,retryTime);
            }

            channel.basicAck(deliveryTag,false);

        } catch (Exception e) {
            logger.warn("参数配置处理失败",e);
            try {
                channel.basicAck(deliveryTag,false);
            } catch (IOException e1) {
                logger.error("",e1);
            }
        }
    }

}
