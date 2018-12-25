package com.walletech.rabbit;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.walletech.po.GprsStateSnapshot;
import com.walletech.po.PulseCMDInfo;
import com.walletech.queue.message.PulseCMDMessage;
import com.walletech.service.GprsBalanceSendService;
import com.walletech.service.PulseCMDService;
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

public class PulseDischargeListener implements ChannelAwareMessageListener {

    private static final Logger logger = LoggerFactory.getLogger(PulseDischargeListener.class);

    @Value("${server.num}")
    private Integer serverNum;
    @Value("${rabbit.retry.time}")
    private Integer retryTime;
    @Value("${rabbit.delayMsg.timeOut}")
    private Integer msgTimeOut;
    @Autowired
    private PulseCMDService pulseCMDService;
    @Autowired
    private RabbitTemplate template;

    @Override
    public void onMessage(Message message, Channel channel) {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            String json = new String(message.getBody(),"UTF-8");
            logger.debug("收到脉冲命令："+json);
            PulseCMDInfo info = JSON.parseObject(json,PulseCMDInfo.class);
            GprsStateSnapshot gprsStateSnapshot = ProtocolUtil.getStateSnapshotByStationId(info.getStationId());

            if (info.getMqRcvTime() == null){
                info.setMqRcvTime(new Date());
            }

            if (new Date().getTime() - info.getMqRcvTime().getTime() > msgTimeOut*60*60*1000){
                logger.warn("命令积压超时,脉冲命令丢弃:[{}]",json);
                info.setSendDone(3);
                info.setResultDescribe(7);
                info.setEndTime(new Date());
                PulseCMDMessage pulseCMDMessage = new PulseCMDMessage();
                pulseCMDMessage.setPulseCMDInfo(info);
                pulseCMDService.doService(pulseCMDMessage);
                channel.basicAck(deliveryTag,false);
                return;
            }

            if (gprsStateSnapshot == null ){
                logger.warn("[{}]所绑设备未连接过网关,请检查命令,消息丢弃[{}]",info.getStationId(),json);
                channel.basicAck(deliveryTag,false);
                return;
            }

            String gprsId = gprsStateSnapshot.getGprsId();
            info.setGprsId(gprsId);

            if (!gprsStateSnapshot.isLinkStatus()){
                logger.debug("[{}]不在线,延迟进行脉冲测试",gprsId);
                delayPulseDischarge(info);
                channel.basicAck(deliveryTag,false);
                return;
            }

            if (!"浮充".equals(gprsStateSnapshot.getState())){
                logger.debug("[{}]处于[{}]状态,延迟进行脉冲测试",gprsId,gprsStateSnapshot.getState());
                delayPulseDischarge(info);
                channel.basicAck(deliveryTag,false);
                return;
            }

            if (CacheUtil.getPulseCMDInfo(gprsId) != null) {
                logger.debug("[{}]有脉冲请求正在处理,延迟进行脉冲测试",gprsId);
                delayPulseDischarge(info);
                channel.basicAck(deliveryTag,false);
                return;
            }

            if (CacheUtil.getGprsMap().get(gprsId) == null){
                if (!serverNum.equals(gprsStateSnapshot.getServerNum())){
                    logger.debug("设备[{}]未连接本机,不处理其脉冲请求",gprsId);
                    channel.basicAck(deliveryTag,false);
                }else {
                    logger.debug("设备[{}]与本机断连,延时等待刷新快照后处理");
                    delayPulseDischarge(info);
                    channel.basicAck(deliveryTag,false);
                }
                return;
            }

            pulseCMDService.beginSendPulseCMD(info,gprsId);
            channel.basicAck(deliveryTag,false);
        }catch (Exception e){
            logger.warn("脉冲命令处理失败",e);
            try {
                channel.basicAck(deliveryTag,false);
            } catch (IOException e1) {
                logger.error("",e1);
            }
        }
    }

    /**
     * 延迟重发
     * @param info
     */
    private void delayPulseDischarge(PulseCMDInfo info){
        RabbitUtil.sendDelayMessage2Mq(template,JSON.toJSONString(info).getBytes(),
                RabbitMqCommon.DELAY_ROUTING_KEY_PULSE_DISCHARGE + serverNum,retryTime);
    }


    /**
     * 发送关闭均衡指令后,将消息重新投入集群RoutingKey中,等待发送脉冲命令
     */
    private void waitForSendPulseCMD(PulseCMDInfo info){
        RabbitUtil.sendDelayMessage2Mq(template,JSON.toJSONString(info).getBytes(),
                "pulseDischarge",90000);
    }


}
