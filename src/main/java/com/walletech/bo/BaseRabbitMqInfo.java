package com.walletech.bo;

import java.util.Date;

public class BaseRabbitMqInfo {
    private Date mqRcvTime;

    public Date getMqRcvTime() {
        return mqRcvTime;
    }

    public void setMqRcvTime(Date mqRcvTime) {
        this.mqRcvTime = mqRcvTime;
    }
}
