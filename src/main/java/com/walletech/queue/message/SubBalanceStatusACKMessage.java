package com.walletech.queue.message;

import com.walletech.po.SubBalanceStatusSend;

public class SubBalanceStatusACKMessage extends Message {

    private SubBalanceStatusSend send;

    public SubBalanceStatusSend getSend() {
        return send;
    }

    public void setSend(SubBalanceStatusSend send) {
        this.send = send;
    }
}
