package com.walletech.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.walletech.bo.BaseRabbitMqInfo;

import java.io.Serializable;
import java.util.Date;

public class ModifyGprsIdInfo extends BaseRabbitMqInfo implements Serializable {
    private static final long serialVersionUID = -8287943762266257818L;
    private Integer id;

    private Integer type;

    private String gprsId;

    private String innerId;

    private String outerId;

    private Byte sendDone;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;

    private Byte state;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getGprsId() {
        return gprsId;
    }

    public void setGprsId(String gprsId) {
        this.gprsId = gprsId == null ? null : gprsId.trim();
    }

    public String getInnerId() {
        return innerId;
    }

    public void setInnerId(String innerId) {
        this.innerId = innerId == null ? null : innerId.trim();
    }

    public String getOuterId() {
        return outerId;
    }

    public void setOuterId(String outerId) {
        this.outerId = outerId == null ? null : outerId.trim();
    }

    public Byte getSendDone() {
        return sendDone;
    }

    public void setSendDone(Byte sendDone) {
        this.sendDone = sendDone;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }
}