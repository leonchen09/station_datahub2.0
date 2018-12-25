package com.walletech.po;

import java.util.Date;

public class ResistanceTypeReadInfo {
    private Integer id;

    private String gprsId;

    private Date sendTime;

    private Byte subNum;

    private Byte type;

    private Short maxVolPoint;

    private Short minVolPoint;

    private Short aveCurPoint;

    private Byte sendDone;

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

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Byte getSubNum() {
        return subNum;
    }

    public void setSubNum(Byte subNum) {
        this.subNum = subNum;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Short getMaxVolPoint() {
        return maxVolPoint;
    }

    public void setMaxVolPoint(Short maxVolPoint) {
        this.maxVolPoint = maxVolPoint;
    }

    public Short getMinVolPoint() {
        return minVolPoint;
    }

    public void setMinVolPoint(Short minVolPoint) {
        this.minVolPoint = minVolPoint;
    }

    public Short getAveCurPoint() {
        return aveCurPoint;
    }

    public void setAveCurPoint(Short aveCurPoint) {
        this.aveCurPoint = aveCurPoint;
    }

    public Byte getSendDone() {
        return sendDone;
    }

    public void setSendDone(Byte sendDone) {
        this.sendDone = sendDone;
    }
}