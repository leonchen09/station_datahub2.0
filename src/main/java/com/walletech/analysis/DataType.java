package com.walletech.analysis;

/**
 * 数据类型常量，用于在入队出队时判断数据类型，调用对应的Service
 * @author
 * @version 1.0
 * @description
 * @since
 */
public interface DataType {
    /**
     * 设备状态
     */
    String DEVICE_STATUS_TYPE = "deviceStatus";
    /**
     * 告警信息
     */
    String WARNING_INFO_TYPE = "warningInfo";
    /**
     * 上下线信息
     */
    String ONLINEOFFLINE_TYPE = "onlineOffline";
    /**
     * 脉冲放电
     */
    String PULSE_DISCHARGE_TYPE = "pulseDischarge";
    /**
     * 脉冲放电命令
     */
    String PULSE_CMD = "pulseCMD";
    /**
     * 参数配置应答
     */
    String GPRS_DEVICE_SEND_ACK = "gprsConfigSetACK";
    /**
     * 参数读取帧
     */
    String GPRS_DEVICE_READ = "gprsDeviceRead";
    /**
     * 均衡控制应答
     */
    String BALANCE_CMD_ACK = "balanceCMDACK";
    /**
     * 修改额定容量应答
     */
    String MODIFY_CAPACITY_ACK = "modifyCapacityACK";
    /**
     * 修改均衡策略应答
     */
    String MODIFY_BALANCE_STRATEGY_ACK = "modifyBalanceStrategyACK";
    /**
     * 修改GprsId应答
     */
    String MODIFY_GPRSID_ACK = "modifyGprsIdACK";
    /**
     * 修改IP&Port应答
     */
    String MODIFY_IP_PORT_ACK = "modifyIPPortACK";
    /**
     * 修改网络地址配置应答
     */
    String MODIFY_CONNECT_ADDR_ACK = "modifyConnectAddrACK";
    /**
     * 读取网络地址配置
     */
    String CONNECT_ADDR_READ = "connectAddrRead";
    /**
     * 配置内阻计算参数应答
     */
    String RESISTANCE_TYPE_SEND_ACK = "resistanceTypeSendACK";
    /**
     * 内阻计算参数读取帧
     */
    String RESISTANCE_TYPE_READ = "resistanceTypeRead";
    /**
     * 远程读取主从机信息帧
     */
    String VERSION_BALANCE_READ = "versionBalanceRead";
    /**
     * 远程读取从机版本信息帧
     */
    String SALVE_VERSION_READ = "salveVersionRead";
    /**
     * 错误连接地址帧
     */
    String ERROR_CONNECT_ADDR = "errorConnectAddr";
    /**
     * 重启设备ACK
     */
    String GPRS_RESTART_ACK = "gprsRestartACK";
    /**
     * 均衡从机实时状态帧
     */
    String SUB_BALANCE_BASE_STATUS = "subBalanceBaseStatus";
    /**
     * 均衡从机均衡参数读取帧
     */
    String SUB_BALANCE_CONFIG = "subBalanceConfig";
    /**
     * 均衡从机均衡参数ACK
     */
    String SUB_BALANCE_CONFIG_ACK = "subBalanceConfigACK";
    /**
     * 当前支持的均衡从机编号帧
     */
    String SUB_BALANCE_SUPPORT = "subBalanceSupport";
    /**
     * 均衡从机均衡状态修改ACK
     */
    String SUB_BALANCE_STATUS_ACK = "subBalanceStatusACK";
}
