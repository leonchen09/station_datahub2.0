package com.walletech.queue.message;

import com.walletech.po.GprsRestartInfo;

public class GprsRestartACKMessage extends Message {
    private GprsRestartInfo info;

    public GprsRestartInfo getInfo() {
        return info;
    }

    public void setInfo(GprsRestartInfo info) {
        this.info = info;
    }
}
