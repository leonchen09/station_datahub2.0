package com.walletech.queue.message;

/**
 * 数据在Queue的消息接口
 *
 * @author
 * @version 1.0
 * @description
 * @since
 */
public abstract class Message {
    
    private Integer dealTimes = 0;
    private String type;

    public Integer getDealTimes() {
        return dealTimes;
    }

    public void setDealTimes(Integer dealTimes) {
        this.dealTimes = dealTimes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
