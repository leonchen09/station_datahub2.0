package com.walletech.datahub;

import com.walletech.po.PackDataInfo;
import com.walletech.queue.message.DeviceStatusMessage;
import com.walletech.server.NettyServer;
import com.walletech.service.DeviceStatusService;
import com.walletech.service.SystemStartService;
import com.walletech.service.VerifyCapacityService;
import com.walletech.task.GprsStateSnapshotTask;
import com.walletech.task.PollingTask;
import com.walletech.task.ResendMqMessageTask;
import com.walletech.util.ByteExchangeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

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

    public static void main1(String[] argv){
        byte[] data = new byte[]{0x38,0x39,0x38,0x36,0x30,37,62,36,31,39,31,38,63,30,30,36,30,39,35,36};
        StringBuffer sb = new StringBuffer();
        for( int i = 0; i < data.length && i < 20; i ++){
            char c = (char)(data[i]);
            System.out.print(c);
            sb.append(c);
        }
        System.out.println();
        System.out.println(sb.toString());
        System.out.println("byte:"+data[0]);
        System.out.println("byte1:" + (data[0] & 0xff));
    }

    private static void testPollVerify(){
        VerifyCapacityService verifyCapacityService = context.getBean(VerifyCapacityService.class);
        verifyCapacityService.pollingVerifyCapacity();
        System.out.println(Integer.toBinaryString(9));
        System.out.println(Integer.parseInt("01011",2));
    }

    private static void testDeviceStatus(){
        try{
            PackDataInfo info = new PackDataInfo();
            info.setbGenVol(new BigDecimal(9));
            info.setcGenCur(new BigDecimal(9));
            info.setaContactStatus((byte)9);
            info.setcError((byte)9);
            info.setbMode((byte)9);
            info.setaReset((byte)1);
            info.setGprsId("T0E999888");
            info.setRcvTime(new Date());
            info.setGenVol(new BigDecimal(49.99));
            info.setState("测试");

            DeviceStatusMessage message = new DeviceStatusMessage();
            message.setPackDataInfo(info);

            DeviceStatusService service = context.getBean(DeviceStatusService.class);
            service.doService(message);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
}
