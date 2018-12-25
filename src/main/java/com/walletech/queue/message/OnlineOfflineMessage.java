package com.walletech.queue.message;

import com.walletech.po.GprsConnectionInfo;

public class OnlineOfflineMessage extends Message {

    private GprsConnectionInfo gprsConnectionInfo;

    public GprsConnectionInfo getGprsConnectionInfo() {
        return gprsConnectionInfo;
    }

    public void setGprsConnectionInfo(GprsConnectionInfo gprsConnectionInfo) {
        this.gprsConnectionInfo = gprsConnectionInfo;
    }
}
