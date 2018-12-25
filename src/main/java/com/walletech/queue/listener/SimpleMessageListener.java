package com.walletech.queue.listener;


import com.walletech.analysis.DataType;
import com.walletech.queue.Reciver;
import com.walletech.queue.exception.QueueException;
import com.walletech.queue.message.*;
import com.walletech.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Executor;

/**
 * 根据获取的数据类型调用不同的Service
 *
 * @author
 * @version 1.0
 * @description
 * @since
 */
public class SimpleMessageListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(SimpleMessageListener.class);

    @Autowired
    private Reciver reciver;
    @Autowired
    private Executor executor;
    @Autowired
    private DeviceStatusService deviceStatusService;
    @Autowired
    private WarningInfoService warningInfoService;
    @Autowired
    private OnlineOfflineService onlineOfflineService;
    @Autowired
    private PulseCMDService pulseCMDService;
    @Autowired
    private PulseDischargeService pulseDischargeService;
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
    private VersionBalanceService versionBalanceService;
    @Autowired
    private SalveVersionService salveVersionService;
    @Autowired
    private ConnectFailedAddrService connectFailedAddrService;
    @Autowired
    private GprsRestartService gprsRestartService;
    @Autowired
    private SubBalanceBaseStatusService subBalanceBaseStatusService;
    @Autowired
    private SubBalanceConfigReadService subBalanceConfigReadService;
    @Autowired
    private SubBalanceConfigSendService subBalanceConfigSendService;
    @Autowired
    private SubBalanceSupportService subBalanceSupportService;
    @Autowired
    private SubBalanceControlService subBalanceControlService;

    @Override
    public void onMessage(final Message message) {
        logger.debug("收到[{}]消息,已处理次数[{}]", message.getType(), message.getDealTimes());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    switch (message.getType()) {
                        case DataType.DEVICE_STATUS_TYPE:
                            DeviceStatusMessage deviceMessage = (DeviceStatusMessage) message;
                            deviceStatusService.doService(deviceMessage);
                            break;
                        case DataType.WARNING_INFO_TYPE:
                            WarningInfoMessage warningInfoMessage = (WarningInfoMessage) message;
                            warningInfoService.doService(warningInfoMessage);
                            break;
                        case DataType.ONLINEOFFLINE_TYPE:
                            OnlineOfflineMessage onlineOfflineMessage = (OnlineOfflineMessage) message;
                            onlineOfflineService.doService(onlineOfflineMessage);
                            break;
                        case DataType.PULSE_CMD:
                            PulseCMDMessage pulseCMDMessage = (PulseCMDMessage) message;
                            pulseCMDService.doService(pulseCMDMessage);
                            break;
                        case DataType.PULSE_DISCHARGE_TYPE:
                            PulseDischargeMessage pulseDischargeMessage = (PulseDischargeMessage) message;
                            pulseDischargeService.doService(pulseDischargeMessage);
                            break;
                        case DataType.GPRS_DEVICE_SEND_ACK:
                            GprsDeviceSendACKMessage gprsDeviceSendACKMessage = (GprsDeviceSendACKMessage) message;
                            gprsDeviceSendService.doService(gprsDeviceSendACKMessage);
                            break;
                        case DataType.GPRS_DEVICE_READ:
                            GprsDeviceReadMessage gprsDeviceReadMessage = (GprsDeviceReadMessage) message;
                            gprsDeviceReadService.doService(gprsDeviceReadMessage);
                            break;
                        case DataType.BALANCE_CMD_ACK:
                            BalanceCMDACKMessage balanceCMDACKMessage = (BalanceCMDACKMessage) message;
                            gprsBalanceSendService.doService(balanceCMDACKMessage);
                            break;
                        case DataType.MODIFY_CAPACITY_ACK:
                            ModifyCapacityACKMessage modifyCapacityACKMessage = (ModifyCapacityACKMessage) message;
                            modifyCapacityService.doService(modifyCapacityACKMessage);
                            break;
                        case DataType.MODIFY_BALANCE_STRATEGY_ACK:
                            ModifyBalanceStrategyACKMessage ackMessage = (ModifyBalanceStrategyACKMessage) message;
                            modifyBalanceStrategyService.doService(ackMessage);
                            break;
                        case DataType.MODIFY_GPRSID_ACK:
                            ModifyGprsIdACKMessage modifyGprsIdACKMessage = (ModifyGprsIdACKMessage) message;
                            modifyGprsIdService.doService(modifyGprsIdACKMessage);
                            break;
                        case DataType.MODIFY_IP_PORT_ACK:
                            ModifyIPPortACKMessage modifyIPPortACKMessage = (ModifyIPPortACKMessage) message;
                            modifyIPPortService.doService(modifyIPPortACKMessage);
                            break;
                        case DataType.MODIFY_CONNECT_ADDR_ACK:
                            ModifyConnectAddrACKMessage connectAddrACKMessage = (ModifyConnectAddrACKMessage) message;
                            modifyConnectAddrService.doService(connectAddrACKMessage);
                            break;
                        case DataType.CONNECT_ADDR_READ:
                            ConnectAddrReadMessage connectAddrReadMessage = (ConnectAddrReadMessage) message;
                            connectAddrInfoService.doService(connectAddrReadMessage);
                            break;
                        case DataType.RESISTANCE_TYPE_SEND_ACK:
                            ResistanceTypeSendACKMessage sendACKMessage = (ResistanceTypeSendACKMessage) message;
                            resistanceTypeSendService.doService(sendACKMessage);
                            break;
                        case DataType.RESISTANCE_TYPE_READ:
                            ResistanceTypeReadMessage readMessage = (ResistanceTypeReadMessage) message;
                            resistanceTypeReadService.doService(readMessage);
                            break;
                        case DataType.VERSION_BALANCE_READ:
                            VersionBalanceMessage versionBalanceMessage = (VersionBalanceMessage) message;
                            versionBalanceService.doService(versionBalanceMessage);
                            break;
                        case DataType.SALVE_VERSION_READ:
                            SalveVersionMessage salveVersionMessage = (SalveVersionMessage) message;
                            salveVersionService.doService(salveVersionMessage);
                            break;
                        case DataType.ERROR_CONNECT_ADDR:
                            ConnectFailedAddrMessage connectFailedAddrMessage = (ConnectFailedAddrMessage) message;
                            connectFailedAddrService.doService(connectFailedAddrMessage);
                            break;
                        case DataType.GPRS_RESTART_ACK:
                            GprsRestartACKMessage gprsRestartACKMessage = (GprsRestartACKMessage) message;
                            gprsRestartService.doService(gprsRestartACKMessage);
                            break;
                        case DataType.SUB_BALANCE_BASE_STATUS:
                            SubBalanceBaseStatusMessage subBalanceBaseStatusMessage = (SubBalanceBaseStatusMessage) message;
                            subBalanceBaseStatusService.doService(subBalanceBaseStatusMessage);
                            break;
                        case DataType.SUB_BALANCE_CONFIG:
                            SubBalanceConfigMessage subBalanceConfigMessage = (SubBalanceConfigMessage) message;
                            subBalanceConfigReadService.doService(subBalanceConfigMessage);
                            break;
                        case DataType.SUB_BALANCE_CONFIG_ACK:
                            SubBalanceConfigACKMessage subBalanceConfigACKMessage = (SubBalanceConfigACKMessage) message;
                            subBalanceConfigSendService.doService(subBalanceConfigACKMessage);
                            break;
                        case DataType.SUB_BALANCE_SUPPORT:
                            SubBalanceSupportMessage subBalanceSupportMessage = (SubBalanceSupportMessage) message;
                            subBalanceSupportService.doService(subBalanceSupportMessage);
                            break;
                        case DataType.SUB_BALANCE_STATUS_ACK:
                            SubBalanceStatusACKMessage subBalanceStatusACKMessage = (SubBalanceStatusACKMessage) message;
                            subBalanceControlService.doService(subBalanceStatusACKMessage);
                            break;
                        default:
                            logger.warn("消息类型不匹配!");
                    }
                } catch (QueueException e) {
                    dealFail(e, message);
                }
            }
        });
    }
    /**
     * 数据在service层处理失败后，再次入队重新处理，直到处理次数达到最大处理次数
     *
     * @param exception
     * @param message
     */
    private void dealFail(QueueException exception, Message message) {
        if (message != null && message.getDealTimes() < reciver.getMaxDealTimes()) {
            logger.error("数据处理异常,数据将重入队列，重新消费.当前处理次数[{}].", message.getDealTimes() + 1, exception);
            message.setDealTimes(message.getDealTimes() + 1);
            reciver.getQueue().offer(message);
        } else if (message != null && message.getDealTimes() >= reciver.getMaxDealTimes()) {
            logger.error("数据处理异常,数据当前处理次数[{}],丢弃.", message.getDealTimes(), exception);
        }
    }


}
