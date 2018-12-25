package com.walletech.queue.message;

import com.walletech.po.GprsBalanceSendInfo;

public class BalanceCMDACKMessage extends Message {
    private GprsBalanceSendInfo gprsBalanceSendInfo;

    public GprsBalanceSendInfo getGprsBalanceSendInfo() {
        return gprsBalanceSendInfo;
    }

    public void setGprsBalanceSendInfo(GprsBalanceSendInfo gprsBalanceSendInfo) {
        this.gprsBalanceSendInfo = gprsBalanceSendInfo;
    }
}
