package com.walletech.util;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.IOException;

public class RabbitUtil {

    private static final String EXCHANGE = "datahub.topic";

    private static final String MODIFY_GPRSID_EXCHANGE = "datahub.direct";

    private static final Logger logger = LoggerFactory.getLogger(RabbitUtil.class);

    /**
     * 发送mq延迟消息
     */
    public static void sendDelayMessage2Mq(RabbitTemplate template, byte[] body, String routingKey, Integer delayTime) {
        MessageProperties properties = new MessageProperties();
        properties.setDelay(delayTime);
        Message message = new Message(body, properties);
        template.send(EXCHANGE, routingKey, message);
    }

    /**
     * 发送mq消息返回修改GPRSID的结果
     */
    public static void sendResultOfModifyGprsIdMessage2Mq(RabbitTemplate template, byte[] body, String routingKey) {
        Message message = new Message(body, new MessageProperties());
        template.send(MODIFY_GPRSID_EXCHANGE, routingKey, message);
    }


    /**
     * 判断是否为ACK/NACK消息,并处理
     *
     * @param channel
     * @param data
     * @param mqTagKey
     * @param tag
     * @param debug
     * @return
     * @throws IOException
     */

    public static boolean isACKMsg(Channel channel, String data, String mqTagKey, Long tag, String debug) throws IOException {
        String flag = data.substring(0, 1);
        String gprsId;
        if (RabbitMqCommon.MQ_ACK.equals(flag)) {
            logger.info("收到[{}]ACK消息[{}]", debug, data);
            gprsId = data.substring(1, data.length());
            Long msgTag = CacheUtil.MQ_DELIVERY_TAG_MAP.get(mqTagKey + gprsId);
            if (msgTag == null) {
                channel.basicAck(tag, false);
                return true;
            }
            channel.basicAck(msgTag, false);
            channel.basicAck(tag, false);
            CacheUtil.MQ_DELIVERY_TAG_MAP.remove(mqTagKey + gprsId);
            return true;
        } else if (RabbitMqCommon.MQ_NACK.equals(flag)) {
            logger.info("收到[{}]NACK消息[{}]", debug, data);
            gprsId = data.substring(1, data.length());
            Long msgTag = CacheUtil.MQ_DELIVERY_TAG_MAP.get(mqTagKey + gprsId);
            if (msgTag == null) {
                channel.basicAck(tag, false);
                return true;
            }
            channel.basicNack(msgTag, false, true);
            channel.basicAck(tag, false);
            CacheUtil.MQ_DELIVERY_TAG_MAP.remove(mqTagKey + gprsId);
            return true;
        }
        return false;
    }


}
