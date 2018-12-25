package com.walletech.queue.message;

import com.walletech.po.PulseCMDInfo;

public class PulseCMDMessage extends Message {

    private PulseCMDInfo pulseCMDInfo;

    public PulseCMDInfo getPulseCMDInfo() {
        return pulseCMDInfo;
    }

    public void setPulseCMDInfo(PulseCMDInfo pulseCMDInfo) {
        this.pulseCMDInfo = pulseCMDInfo;
    }
}
