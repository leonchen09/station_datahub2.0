package com.walletech.util;

public class RabbitMqCommon {

    public static final String ROUTING_KEY_SUB_BALANCE_CONFIG = "balanceConfig";

    public static final String DELAY_ROUTING_KEY_SUB_BALANCE_CONFIG = "balanceConfig.";

    public static final String ROUTING_KEY_SUB_BALANCE_STATUS = "subBalanceStatus";

    public static final String DELAY_ROUTING_KEY_SUB_BALANCE_STATUS = "subBalanceStatus.";

    public static final String ROUTING_KEY_PARAMETER_CONFIG = "parameterConfig";

    public static final String DELAY_ROUTING_KEY_PARAMETER_CONFIG = "parameterConfig.";

    public static final String ROUTING_KEY_PULSE_DISCHARGE = "pulseDischarge";

    public static final String DELAY_ROUTING_KEY_PULSE_DISCHARGE = "pulseDischarge.";

    public static final String ROUTING_KEY_UPDATE_CAPACITY = "updateCapacity";

    public static final String DELAY_ROUTING_KEY_UPDATE_CAPACITY = "updateCapacity.";

    public static final String ROUTING_KEY_UPDATE_GPRSID = "updateGprsId";

    public static final String DELAY_ROUTING_KEY_UPDATE_GPRSID = "updateGprsId.";

    public static final String MQ_ACK = "1";

    public static final String MQ_NACK = "0";

}
