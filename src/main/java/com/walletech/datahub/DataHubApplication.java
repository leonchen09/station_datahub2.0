package com.walletech.datahub;

import com.walletech.server.NettyServer;
import com.walletech.service.SystemStartService;
import com.walletech.task.GprsStateSnapshotTask;
import com.walletech.task.PollingTask;
import com.walletech.task.ResendMqMessageTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 程序启动类
 * 启动Spring容器
 */
public class DataHubApplication {

    public static ApplicationContext context;

    private static final Logger logger = LoggerFactory.getLogger(DataHubApplication.class);

    public static void main(String[] args) throws InterruptedException {
        context = new ClassPathXmlApplicationContext("classpath:config/main.xml");
        //初始化设备连接信息
        context.getBean(SystemStartService.class).systemStart();
        //启动轮询任务
        context.getBean(PollingTask.class).start();
        //启动设备状态快照任务
        context.getBean(GprsStateSnapshotTask.class).start();
        //启动mq发送失败重试任务
        context.getBean(ResendMqMessageTask.class).start();
        //启动数据网关
        NettyServer server = context.getBean(NettyServer.class);
        server.start();
    }

}
