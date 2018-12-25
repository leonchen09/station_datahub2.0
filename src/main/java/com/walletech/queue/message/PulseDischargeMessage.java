package com.walletech.queue.message;

import com.walletech.po.PulseCMDInfo;
import com.walletech.po.PulseDischargeInfo;

public class PulseDischargeMessage extends Message {
    private PulseDischargeInfo pulseDischargeInfo;
    private PulseCMDInfo pulseCMDInfo;

    public PulseDischargeInfo getPulseDischargeInfo() {
        return pulseDischargeInfo;
    }

    public void setPulseDischargeInfo(PulseDischargeInfo pulseDischargeInfo) {
        this.pulseDischargeInfo = pulseDischargeInfo;
    }

    public PulseCMDInfo getPulseCMDInfo() {
        return pulseCMDInfo;
    }

    public void setPulseCMDInfo(PulseCMDInfo pulseCMDInfo) {
        this.pulseCMDInfo = pulseCMDInfo;
    }
}
