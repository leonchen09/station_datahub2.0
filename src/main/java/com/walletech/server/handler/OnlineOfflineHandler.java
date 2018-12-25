package com.walletech.server.handler;

import com.walletech.analysis.DataType;
import com.walletech.po.GprsConnectionInfo;
import com.walletech.queue.Sender;
import com.walletech.queue.message.OnlineOfflineMessage;
import com.walletech.util.CacheUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@ChannelHandler.Sharable
public class OnlineOfflineHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(OnlineOfflineHandler.class);
    @Value("${server.num}")
    private Integer serverNum;
    @Value("${netty.port}")
    private Integer serverPort;
    @Autowired
    private Sender sender;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String gprsId = (String) msg;
        Date now = new Date();
        GprsConnectionInfo gprs = CacheUtil.getGprsMap().get(gprsId);
        if (gprs!=null){
            logger.debug("设备[{}]已上线,更新上次活跃时间",gprsId);
            gprs.setLastActiveTime(now);
            Channel oldChannel = gprs.getChannel();
            if (oldChannel.id()!=ctx.channel().id()){
                oldChannel.close();
                gprs.setChannel(ctx.channel());
                CacheUtil.getGprsChannelMap().put(gprsId,ctx.channel());
                ctx.channel().attr(CacheUtil.GPRS).set(gprsId);
                logger.info("[{}] 切换channel",gprsId);
            }
            return;
        }
        //设备上线
        logger.info("设备[{}]连接本机,服务器编号[{}],端口[{}]",gprsId,serverNum,serverPort);
        ctx.channel().attr(CacheUtil.GPRS).set(gprsId);
        gprs = new GprsConnectionInfo();
        gprs.setChannel(ctx.channel());
        gprs.setLastActiveTime(now);
        gprs.setLinkStatus(true);
        gprs.setGprsId(gprsId);
        gprs.setServerNum(serverNum);
        gprs.setPort(serverPort);

        OnlineOfflineMessage message = new OnlineOfflineMessage();
        message.setGprsConnectionInfo(gprs);
        message.setType(DataType.ONLINEOFFLINE_TYPE);
        sender.saveMessage(message);
    }

    /**
     * 客户端超时触发离线
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        logger.info("客户端超时触发离线");
        if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE){
                offline(ctx);
            }
        }
    }

    /**
     * 设备断开连接触发离线
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("客户端channel断开触发离线");
        offline(ctx);
        super.channelInactive(ctx);
    }

    /**
     * 离线处理
     * @param ctx
     */
    private void offline(ChannelHandlerContext ctx){
        String gprsId = ctx.channel().attr(CacheUtil.GPRS).get();
        if (gprsId == null){
            logger.info("未知设备离线");
            return;
        }
        GprsConnectionInfo gprsInfo = CacheUtil.getGprsMap().get(gprsId);
        if (gprsInfo == null){
            return;
        }
        Channel cacheChannel = gprsInfo.getChannel();
        if (ctx.channel().id()!=cacheChannel.id()) {
            logger.info("[{}]离线channel为无效channel,不进行处理",gprsId);
            return;
        }
        logger.info("设备[{}]从本机离线",gprsId);
        gprsInfo.setLinkStatus(false);
        OnlineOfflineMessage message = new OnlineOfflineMessage();
        message.setType(DataType.ONLINEOFFLINE_TYPE);
        message.setGprsConnectionInfo(gprsInfo);
        sender.saveMessage(message);
    }

}
