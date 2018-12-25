package com.walletech.analysis;

import com.walletech.po.*;
import com.walletech.queue.Sender;
import com.walletech.queue.message.*;
import com.walletech.util.CacheUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * ACK应答帧处理类
 */
@Component
public class ACKAnalysis implements Analysis {

    private static final Logger logger = LoggerFactory.getLogger(ACKAnalysis.class);

    private static final Integer DATA_OFFSET = 10;
    @Autowired
    private Sender sender;

    @Override
    public void doAnalysis(byte[] data, String gprsId, ChannelHandlerContext ctx) {
        switch (data[DATA_OFFSET]){
            case (byte) 0x14: //脉冲放电请求应答帧
                doPulseACK(data,gprsId);
                break;
            case (byte) 0x12: //参数配置应答帧
                doConfigSetACK(data,gprsId);
                break;
            case (byte) 0x16: //均衡控制应答帧
                doBalanceACK(data,gprsId);
                break;
            case (byte) 0x21: //修改额定容量应答帧
                doModifyCapacity(data,gprsId);
                break;
            case (byte) 0x22: //修改均衡策略应答帧
                doModifyBalanceStrategy(data,gprsId);
                break;
            case (byte) 0x1a: //修改主机ID应答
                doModifyGprsHostId(data,gprsId);
                break;
            case (byte) 0x1b: //修改从机ID应答
                doModifyGprsSlaveId(data,gprsId);
                break;
            case (byte) 0x18: //修改IPPort应答
                doModifyIPPort(data,gprsId);
                break;
            case (byte) 0x27: //修改网络地址配置应答
                doModifyConnectAddr(data,gprsId);
                break;
            case (byte) 0x2a: //配置内阻计算参数应答
                doResistanceTypeSend(data,gprsId);
                break;
            case (byte) 0x17: //远程重启命令应答
                doGprsRestart(data,gprsId);
                break;
            case (byte) 0x30: //均衡从机均衡参数配置应答
                doSubBalanceConfig(data,gprsId);
                break;
            case (byte) 0x32: //均衡从机均衡开关应答
                doSubBalanceStatus(data,gprsId);
                break;
            default:
                logger.info("[{}]未知应答 [{}]",gprsId,StringUtil.toHexString(data));
        }
    }

    /**
     * 处理脉冲放电应答
     */
    private void doPulseACK(byte[] data,String gprsId){
        Date time = new Date();
        PulseCMDInfo pulseCMDInfo = CacheUtil.getPulseCMDInfo(gprsId);
        if (pulseCMDInfo == null){
            return;
        }
        pulseCMDInfo.setEndTime(time);
        Integer pulseCell = pulseCMDInfo.getPulseCell();
        switch (data[DATA_OFFSET+1]){
            case (byte) 0x80:
                logger.info("[{}]即将开始放电 电池序号[{}]",gprsId,pulseCell);
                pulseCMDInfo.setSendDone(1);
                break;
            case (byte) 0x01:
                logger.warn("[{}]脉冲请求 电池序号[{}] 数据长度错误",gprsId,pulseCell);
                pulseCMDInfo.setSendDone(3);
                pulseCMDInfo.setResultDescribe(1);
                break;
            case (byte) 0x02:
                logger.warn("[{}]脉冲请求 电池序号[{}] 电池序号错误",gprsId,pulseCell);
                pulseCMDInfo.setSendDone(3);
                pulseCMDInfo.setResultDescribe(2);
                break;
            case (byte) 0x03:
                logger.warn("[{}]脉冲请求 电池序号[{}] 区间5采样时间错误",gprsId,pulseCell);
                pulseCMDInfo.setSendDone(3);
                pulseCMDInfo.setResultDescribe(3);
                break;
            case (byte) 0x04:
                logger.warn("[{}]脉冲请求 电池序号[{}] 执行过程出错",gprsId,pulseCell);
                pulseCMDInfo.setSendDone(3);
                pulseCMDInfo.setResultDescribe(4);
                break;
            case (byte) 0x05:
                logger.warn("[{}]脉冲请求 电池序号[{}] 主从2.4G通讯故障",gprsId,pulseCell);
                pulseCMDInfo.setSendDone(3);
                pulseCMDInfo.setResultDescribe(5);
                break;
            case (byte) 0x06:
                logger.warn("[{}]脉冲请求 电池序号[{}] 失败 正处于均衡状态,不进行特征测试",gprsId,pulseCell);
                pulseCMDInfo.setSendDone(3);
                pulseCMDInfo.setResultDescribe(6);
                break;
            default:
                logger.warn("[{}]脉冲请求 电池序号[{}] 未知错误 应答：[{}]",gprsId,pulseCell,StringUtil.toHexString(data));
                pulseCMDInfo.setSendDone(3);
        }
        CacheUtil.putPulseCMDInfo(gprsId,pulseCMDInfo);
        PulseCMDMessage message = new PulseCMDMessage();
        message.setPulseCMDInfo(pulseCMDInfo);
        message.setType(DataType.PULSE_CMD);
        sender.saveMessage(message);
    }

    /**
     * 处理参数配置应答
     * @param data
     * @param gprsId
     */
    private void doConfigSetACK(byte[] data,String gprsId){
        GprsDeviceSendACKMessage message = new GprsDeviceSendACKMessage();
        message.setGprsId(gprsId);
        message.setType(DataType.GPRS_DEVICE_SEND_ACK);
        message.setSuccess(false);
        switch (data[DATA_OFFSET+1]){
            case (byte) 0x80:
                logger.info("[{}]参数配置成功",gprsId);
                message.setSuccess(true);
                break;
            case (byte) 0x01:
                logger.warn("[{}]参数配置数据长度错误",gprsId);
                break;
            case (byte) 0x02:
                logger.warn("[{}]参数配置网络连接参数错误",gprsId);
                break;
            case (byte) 0x03:
                logger.warn("[{}]参数配置帧传输间隔参数错误",gprsId);
                break;
            case (byte) 0x04:
                logger.warn("[{}]参数配置充放电阈值错误",gprsId);
                break;
            case (byte) 0x05:
                logger.warn("[{}]参数配置电池容量错误",gprsId);
                break;
            case (byte) 0x06:
                logger.warn("[{}]参数配置告警阈值错误",gprsId);
                break;
            case (byte) 0x07:
                logger.warn("[{}]参数配置flash错误，保存数据失败",gprsId);
                break;
            case (byte) 0x08:
                logger.warn("[{}]参数配置电流上下限参数错误",gprsId);
                break;
            case (byte) 0x09:
                logger.warn("[{}]参数配置恢复点参数错误",gprsId);
                break;
            default:
                logger.warn("[{}]参数配置未知错误 应答：[{}]",gprsId,StringUtil.toHexString(data));
        }
        sender.saveMessage(message);
    }

    /**
     * 处理均衡控制应答
     */
    private void doBalanceACK(byte[] data,String gprsId){
        GprsBalanceSendInfo info = CacheUtil.getBalanceSendInfo(gprsId);
        if (info ==null){
            return;
        }
        if (data[DATA_OFFSET] == (byte) 0x16){
            logger.info("[{}]均衡成功",gprsId);
            info.setSendDone((byte) 2);
        }else {
            logger.warn("[{}]均衡失败",gprsId);
            info.setSendDone((byte) 3);
        }
        BalanceCMDACKMessage message = new BalanceCMDACKMessage();
        message.setType(DataType.BALANCE_CMD_ACK);
        message.setGprsBalanceSendInfo(info);
        sender.saveMessage(message);
    }

    /**
     * 处理修改额定容量应答
     * @param data
     * @param gprsId
     */
    private void doModifyCapacity(byte[] data,String gprsId){
        ModifyCapacityInfo info = CacheUtil.getModifyCapacityInfo(gprsId);
        if (info ==null){
            return;
        }
        switch (data[DATA_OFFSET+1]){
            case (byte) 0x80:
                logger.info("[{}]修改额定容量成功",gprsId);
                info.setSendDone((byte) 2);
                break;
            case (byte) 0x01:
                logger.warn("[{}]修改额定容量失败",gprsId);
                info.setSendDone((byte) 3);
                break;
            default:
                info.setSendDone((byte) 3);
                logger.warn("未知错误 [{}]修改额定容量失败 应答：[{}]",gprsId,StringUtil.toHexString(data));
        }
        ModifyCapacityACKMessage message = new ModifyCapacityACKMessage();
        message.setModifyCapacityInfo(info);
        message.setType(DataType.MODIFY_CAPACITY_ACK);
        sender.saveMessage(message);
    }

    /**
     * 处理修改均衡策略应答
     */
    private void doModifyBalanceStrategy(byte[] data,String gprsId){
        ModifyBalanceStrategyInfo info = CacheUtil.getBalanceStrategyInfo(gprsId);
        if (info ==null){
            return;
        }
        switch (data[DATA_OFFSET+1]){
            case (byte) 0x80:
                logger.info("[{}]修改均衡策略成功",gprsId);
                info.setSendDone((byte) 2);
                break;
            case (byte) 0x01:
                logger.warn("[{}]修改均衡策略失败",gprsId);
                info.setSendDone((byte) 3);
                break;
            default:
                info.setSendDone((byte) 3);
                logger.warn("未知错误 [{}]修改均衡策略失败 应答：[{}]",StringUtil.toHexString(data));
        }
        ModifyBalanceStrategyACKMessage message = new ModifyBalanceStrategyACKMessage();
        message.setType(DataType.MODIFY_BALANCE_STRATEGY_ACK);
        message.setModifyBalanceStrategyInfo(info);
        sender.saveMessage(message);
    }

    /**
     * 处理修改Gprs主机Id应答
     */
    private void doModifyGprsHostId(byte[] data,String gprsId){
        ModifyGprsIdInfo info = CacheUtil.getModifyGprsIdInfo(gprsId);
        if (info == null || info.getType()!= 1){
            return;
        }
        switch (data[DATA_OFFSET+1]){
            case (byte) 0x80:
                logger.info("[{}]修改主机ID成功",gprsId);
                info.setSendDone((byte) 2);
                break;
            case (byte) 0x01:
                logger.warn("[{}]修改主机ID失败",gprsId);
                info.setSendDone((byte) 3);
                break;
            default:
                info.setSendDone((byte) 3);
                logger.warn("未知错误 [{}]修改主机ID失败 应答：[{}]",gprsId,StringUtil.toHexString(data));
        }
        ModifyGprsIdACKMessage message = new ModifyGprsIdACKMessage();
        message.setType(DataType.MODIFY_GPRSID_ACK);
        message.setModifyGprsIdInfo(info);
        sender.saveMessage(message);
    }
    /**
     * 处理修改Gprs从机Id应答
     */
    private void doModifyGprsSlaveId(byte[] data,String gprsId){
        ModifyGprsIdInfo info = CacheUtil.getModifyGprsIdInfo(gprsId);
        if (info == null || info.getType()!= 2){
            return;
        }
        switch (data[DATA_OFFSET+1]){
            case (byte) 0x80:
                logger.info("[{}]修改从机ID成功",gprsId);
                info.setSendDone((byte) 2);
                break;
            case (byte) 0x01:
                logger.warn("[{}]修改从机ID失败",gprsId);
                info.setSendDone((byte) 3);
                break;
            default:
                info.setSendDone((byte) 3);
                logger.warn("未知错误 [{}]修改从机ID失败 应答：[{}]",gprsId,StringUtil.toHexString(data));
        }
        ModifyGprsIdACKMessage message = new ModifyGprsIdACKMessage();
        message.setType(DataType.MODIFY_GPRSID_ACK);
        message.setModifyGprsIdInfo(info);
        sender.saveMessage(message);
    }

    /**
     * 处理修改IPPort应答
     */
    private void doModifyIPPort(byte[] data,String gprsId){
        ModifyIPPortInfo info = new ModifyIPPortInfo();
        info.setGprsId(gprsId);
        if (data[DATA_OFFSET] == (byte) 0x18){
            logger.info("[{}]IP&Port修改成功",gprsId);
            info.setSendDone(2);
        }else {
            logger.warn("[{}]IP&Port修改失败",gprsId);
            info.setSendDone(3);
        }
        ModifyIPPortACKMessage message = new ModifyIPPortACKMessage();
        message.setType(DataType.MODIFY_IP_PORT_ACK);
        message.setModifyIPPortInfo(info);
        sender.saveMessage(message);
    }

    /**
     * 处理修改网络地址配置应答
     */
    private void doModifyConnectAddr(byte[] data,String gprsId){
        ModifyConnectAddrInfo info = new ModifyConnectAddrInfo();
        info.setGprsId(gprsId);
        switch (data[DATA_OFFSET+1]){
            case (byte) 0x80:
                logger.info("[{}]修改网络地址配置成功",gprsId);
                info.setSendDone(2);
                break;
            case (byte) 0x01:
                logger.warn("[{}]修改网络地址配置失败,地址类型错误",gprsId);
                info.setSendDone(31);
                break;
            case (byte) 0x02:
                logger.warn("[{}]修改网络地址配置失败,地址长度错误",gprsId);
                info.setSendDone(31);
                break;
            default:
                info.setSendDone(33);
                logger.warn("未知错误 [{}]修改网络地址配置失败 应答：[{}]",gprsId,StringUtil.toHexString(data));
        }
        ModifyConnectAddrACKMessage message = new ModifyConnectAddrACKMessage();
        message.setType(DataType.MODIFY_CONNECT_ADDR_ACK);
        message.setModifyConnectAddrInfo(info);
        sender.saveMessage(message);
    }

    /**
     * 处理配置内阻计算参数应答
     */
    private void doResistanceTypeSend(byte[] data,String gprsId){
        ResistanceTypeSendInfo info = new ResistanceTypeSendInfo();
        info.setGprsId(gprsId);
        switch (data[DATA_OFFSET+1]){
            case (byte) 0x80:
                logger.info("[{}]配置内阻计算参数成功",gprsId);
                info.setSendDone((byte) 2);
                break;
            case (byte) 0x01:
                logger.warn("[{}]配置内阻计算参数失败",gprsId);
                info.setSendDone((byte) 3);
                break;
            default:
                info.setSendDone((byte) 3);
                logger.warn("未知错误 [{}]配置内阻计算参数失败 应答：[{}]",gprsId,StringUtil.toHexString(data));
        }
        ResistanceTypeSendACKMessage message = new ResistanceTypeSendACKMessage();
        message.setType(DataType.RESISTANCE_TYPE_SEND_ACK);
        message.setResistanceTypeSendInfo(info);
        sender.saveMessage(message);
    }

    /**
     * 处理远程重启命令应答
     */
    private void doGprsRestart(byte[] data,String gprsId){
        GprsRestartInfo info = new GprsRestartInfo();
        info.setGprsId(gprsId);
        if (data[DATA_OFFSET] == (byte) 0x17){
            logger.info("[{}]重启命令接收成功",gprsId);
            info.setSendDone(2);
        }else {
            logger.warn("[{}]重启命令接收失败",gprsId);
            info.setSendDone(3);
        }
        GprsRestartACKMessage message = new GprsRestartACKMessage();
        message.setInfo(info);
        message.setType(DataType.GPRS_RESTART_ACK);
        sender.saveMessage(message);
    }

    /**
     * 处理均衡从机均衡参数配置应答
     * @param data
     * @param gprsId
     */
    private void doSubBalanceConfig(byte[] data,String gprsId) {
        SubBalanceConfigSend send = CacheUtil.getSubBalanceConfigSend(gprsId);
        if (send == null) {
            return;
        }
        send.setEndTime(new Date());
        switch (data[DATA_OFFSET + 1]) {
            case (byte) 0x80:
                logger.info("[{}]均衡从机均衡参数配置成功", gprsId);
                send.setSendDone(2);
                break;
            case (byte) 0x01:
                logger.warn("[{}]均衡从机均衡参数配置失败:参数错误", gprsId);
                send.setSendDone(3);
                break;
            case (byte) 0x02:
                logger.warn("[{}]均衡从机均衡参数配置失败:从机离线", gprsId);
                send.setSendDone(3);
                break;
            case (byte) 0x03:
                logger.warn("[{}]均衡从机均衡参数配置失败:从机写错误", gprsId);
                send.setSendDone(3);
                break;
            default:
                send.setSendDone(3);
                logger.warn("未知错误 [{}]均衡从机均衡参数配置失败 应答：[{}]", gprsId , StringUtil.toHexString(data));
        }
        SubBalanceConfigACKMessage message = new SubBalanceConfigACKMessage();
        message.setSend(send);
        message.setType(DataType.SUB_BALANCE_CONFIG_ACK);
        sender.saveMessage(message);
    }


    /**
     * 处理均衡从机均均衡状态修改应答
     * @param data
     * @param gprsId
     */
    private void doSubBalanceStatus(byte[] data,String gprsId) {
        SubBalanceStatusSend send = CacheUtil.getSubBalanceStatusSend(gprsId);
        if (send == null) {
            return;
        }
        send.setInsertTime(new Date());
        switch (data[DATA_OFFSET + 1]) {
            case (byte) 0x80:
                logger.info("[{}] [{}]均衡从机均均衡状态修改成功", gprsId ,send.getBalanceSubdeviceCode());
                send.setSendDone(2);
                break;
            case (byte) 0x01:
                logger.warn("[{}] [{}]均衡从机均均衡状态修改失败:均衡从机不存在", gprsId,send.getBalanceSubdeviceCode());
                send.setSendDone(3);
                break;
            default:
                send.setSendDone(3);
                logger.warn("未知错误 [{}] [{}]均衡从机均均衡状态修改失败 应答：[{}]", gprsId,send.getBalanceSubdeviceCode()
                        ,StringUtil.toHexString(data));
        }
        SubBalanceStatusACKMessage message = new SubBalanceStatusACKMessage();
        message.setSend(send);
        message.setType(DataType.SUB_BALANCE_STATUS_ACK);
        sender.saveMessage(message);
    }

}
