package com.walletech.task;

import com.walletech.service.*;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 轮询脉冲测试命令任务类
 */
@Component
public class PollingTask {
    private HashedWheelTimer timer = new HashedWheelTimer();
    @Value("${task.check.cycle}")
    private Integer checkTime ;
    @Value("${mq.flag}")
    private Integer mqFlag;
    @Autowired
    private PulseCMDService pulseCMDService;
    @Autowired
    private GprsDeviceSendService gprsDeviceSendService;
    @Autowired
    private GprsDeviceReadService gprsDeviceReadService;
    @Autowired
    private GprsBalanceSendService gprsBalanceSendService;
    @Autowired
    private ModifyCapacityService modifyCapacityService;
    @Autowired
    private ModifyBalanceStrategyService modifyBalanceStrategyService;
    @Autowired
    private ModifyGprsIdService modifyGprsIdService;
    @Autowired
    private ModifyIPPortService modifyIPPortService;
    @Autowired
    private ModifyConnectAddrService modifyConnectAddrService;
    @Autowired
    private ConnectAddrInfoService connectAddrInfoService;
    @Autowired
    private ResistanceTypeSendService resistanceTypeSendService;
    @Autowired
    private ResistanceTypeReadService resistanceTypeReadService;
    @Autowired
    private SubBalanceConfigSendService subBalanceConfigSendService;
    @Autowired
    private SubBalanceControlService subBalanceControlService;
    @Autowired
    private GprsRestartService gprsRestartService;
    @Autowired
    private VerifyCapacityService verifyCapacityService;

    public void start(){
        if (checkTime <= 0){
            return;
        }
        TimerTask task = new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                addTask(this);
                pulseCMDService.checkTimeOut(); //脉冲超时检查
                modifyCapacityService.checkTimeOut(); //容量修改超时检查
                modifyGprsIdService.checkTimeOut(); //修改ID超时检查
                subBalanceConfigSendService.checkTimeOut(); //均衡从机均衡参数配置超时检查
                subBalanceControlService.checkTimeOut(); //均衡从机状态控制超时检查
                verifyCapacityService.checkTimeOut();//核容指令超时检查
                if (mqFlag!=1){
                    pulseCMDService.pollingPulseCMD(); //脉冲命令
                    gprsDeviceSendService.pollingGprsDeviceSendInfo(); //参数配置命令
//                    gprsBalanceSendService.pollingBalanceSend(); //均衡命令
//                    modifyBalanceStrategyService.pollingModifyBalanceStrategy(); //修改均衡策略命令
                    modifyCapacityService.pollingModifyCapacity(); //修改电池额定容量命令
                    modifyGprsIdService.pollingModifyGprsId(); //修改设备(主机/从机)ID命令
                    subBalanceConfigSendService.pollingSubBalanceConfigSend(); //均衡从机均衡参数配置
                    subBalanceControlService.pollingSubBalanceStatusSend(); //均衡从机状态控制
//                    if(System.currentTimeMillis() < 1579743604921l)
                        verifyCapacityService.pollingVerifyCapacity();//核容指令
                }
                gprsDeviceReadService.pollingGprsDeviceReadInfo(); //参数读取命令
//                modifyIPPortService.pollingModifyIPPort(); //修改连接IP&Port命令
                modifyConnectAddrService.pollingModifyConnectAddr(); //修改网络地址配置命令
                connectAddrInfoService.pollingConnectAddrInfoRead(); //网络地址读取命令
                resistanceTypeReadService.pollingResistanceTypeRead(); //读取内阻计算参数
                resistanceTypeSendService.pollingResistanceTypeSend(); //配置内阻计算参数
                gprsRestartService.pollingGprsRestartInfo();
            }
        };
        addTask(task);
    }

    private void addTask(TimerTask task){
        timer.newTimeout(task,checkTime,TimeUnit.MILLISECONDS);
    }
}
