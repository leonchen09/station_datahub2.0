package com.walletech.po;

import com.walletech.bo.BaseRabbitMqInfo;

import java.io.Serializable;
import java.util.Date;

public class VerifyCapacity extends BaseRabbitMqInfo implements Serializable {

    private Integer id;

    private String gprsId;

    private Integer cmd;

    private Byte sendDone;

    private Date sendTime;

    private Date createdTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGprsId() {
        return gprsId;
    }

    public void setGprsId(String gprsId) {
        this.gprsId = gprsId == null ? null : gprsId.trim();
    }

    public Integer getCmd() {
        return cmd;
    }

    public void setCmd(Integer cmd) {
        this.cmd = cmd;
    }

    public Byte getSendDone() {
        return sendDone;
    }

    public void setSendDone(Byte sendDone) {
        this.sendDone = sendDone;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}