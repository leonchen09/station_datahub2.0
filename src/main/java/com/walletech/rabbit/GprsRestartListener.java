package com.walletech.rabbit;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.walletech.po.GprsRestartInfo;
import com.walletech.po.GprsStateSnapshot;
import com.walletech.service.GprsRestartService;
import com.walletech.util.CacheUtil;
import com.walletech.util.ProtocolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;


public class GprsRestartListener implements ChannelAwareMessageListener {

    private static final Logger logger = LoggerFactory.getLogger(GprsRestartListener.class);

    @Value("${server.num}")
    private Integer serverNum;
    @Autowired
    private GprsRestartService gprsRestartService;

    @Override
    public void onMessage(Message message, Channel channel) {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            String json = new String(message.getBody(),"UTF-8");
            logger.info("收到设备重启命令："+json);
            GprsRestartInfo info = JSON.parseObject(json, GprsRestartInfo.class);
            String gprsId = info.getGprsId();
            GprsStateSnapshot gprsStateSnapshot = ProtocolUtil.getStateSnapshotByGprsId(gprsId);

            if (CacheUtil.getGprsMap().get(gprsId) == null || gprsStateSnapshot == null || !gprsStateSnapshot.isLinkStatus()){
                logger.info("设备[{}]未连接本机或已离线,不处理其设备重启命令",gprsId);
                channel.basicAck(deliveryTag,false);
                return;
            }

            gprsRestartService.beginSendGprsRestartCMD(info,gprsId);
            channel.basicAck(deliveryTag,false);

        }catch (Exception e){
            logger.warn("设备重启命令处理失败",e);
            try {
                channel.basicAck(deliveryTag,false);
            } catch (IOException e1) {
                logger.error("",e1);
            }
        }
    }
}
