package com.walletech.queue.message;

import com.walletech.po.SubBalanceConfigSend;

public class SubBalanceConfigACKMessage extends Message {
    private SubBalanceConfigSend send;

    public SubBalanceConfigSend getSend() {
        return send;
    }

    public void setSend(SubBalanceConfigSend send) {
        this.send = send;
    }
}
