package com.walletech.server;

import com.walletech.queue.Reciver;
import com.walletech.server.handler.OnlineOfflineHandler;
import com.walletech.server.handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * @author
 * @version 1.0
 * @description
 * @since
 */
public class NettyServer{
    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);
    private Channel channel;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private Integer backLog;
    private Integer port;
    private Integer readerIdleTime;
    private Integer bossGroupNum;
    private Integer workerGroupNum;

    @Autowired
    private ServerHandler serverHandler;
    @Autowired
    private OnlineOfflineHandler onlineOfflineHandler;
    @Autowired
    private Reciver reciver;

    /**
     * 启动Queue的接收端并启动netty服务
     * @throws InterruptedException
     */
    public void start() throws InterruptedException {
        //启动队列监听
        reciver.start();
        logger.info("启动队列监听,数据最大处理次数[{}]",reciver.getMaxDealTimes());
        logger.info("Begin to start DataHub server");
        bossGroup = new NioEventLoopGroup(bossGroupNum);
        workerGroup = new NioEventLoopGroup(workerGroupNum);
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, backLog)
                .option(ChannelOption.ALLOCATOR,PooledByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.ALLOCATOR,PooledByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_REUSEADDR, true)
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        channel.pipeline()
                        .addLast(new IdleStateHandler(readerIdleTime,0, 0,TimeUnit.MINUTES))
                        .addLast("Unpacking",new LengthFieldBasedFrameDecoder(256,
                                2, 1, 0, 0))
                        .addLast(serverHandler)
                        .addLast(onlineOfflineHandler);
                    }
                });

        channel = serverBootstrap.bind(port).sync().channel();
        logger.info("DataHub server listening on port " + port + " and ready for connections...");

        channel.closeFuture().sync();
    }

    public void stop() {
        logger.info("destroy server resources");
        if (null == channel) {
            logger.error("server channel is null");
        }
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        channel.closeFuture().syncUninterruptibly();
        bossGroup = null;
        workerGroup = null;
        channel = null;
    }

    public Integer getBackLog() {
        return backLog;
    }

    public void setBackLog(Integer backLog) {
        this.backLog = backLog;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getBossGroupNum() {
        return bossGroupNum;
    }

    public void setBossGroupNum(Integer bossGroupNum) {
        this.bossGroupNum = bossGroupNum;
    }

    public Integer getWorkerGroupNum() {
        return workerGroupNum;
    }

    public void setWorkerGroupNum(Integer workerGroupNum) {
        this.workerGroupNum = workerGroupNum;
    }

    public Integer getReaderIdleTime() {
        return readerIdleTime;
    }

    public void setReaderIdleTime(Integer readerIdleTime) {
        this.readerIdleTime = readerIdleTime;
    }
}
