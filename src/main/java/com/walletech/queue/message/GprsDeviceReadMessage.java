package com.walletech.queue.message;

import com.walletech.po.GprsDeviceInfo;

public class GprsDeviceReadMessage extends Message {
    private GprsDeviceInfo gprsDeviceInfo;

    public GprsDeviceInfo getGprsDeviceInfo() {
        return gprsDeviceInfo;
    }

    public void setGprsDeviceInfo(GprsDeviceInfo gprsDeviceInfo) {
        this.gprsDeviceInfo = gprsDeviceInfo;
    }
}
