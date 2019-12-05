package com.walletech.queue.message;

import com.walletech.po.VerifyCapacity;

public class VerifyCapacityACKMessage extends Message {
    private VerifyCapacity verifyCapacity;

    public VerifyCapacity getVerifyCapacity() {
        return verifyCapacity;
    }

    public void setVerifyCapacity(VerifyCapacity verifyCapacity) {
        this.verifyCapacity = verifyCapacity;
    }
}
