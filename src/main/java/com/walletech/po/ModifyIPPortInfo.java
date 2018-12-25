package com.walletech.po;

import java.util.Date;

public class ModifyIPPortInfo
{
    private Integer id;

    private String gprsId;

    private String ipNew;

    private Integer portNew;

    private Date sendTime;

    private Integer sendDone;

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

    public String getIpNew() {
        return ipNew;
    }

    public void setIpNew(String ipNew) {
        this.ipNew = ipNew == null ? null : ipNew.trim();
    }

    public Integer getPortNew() {
        return portNew;
    }

    public void setPortNew(Integer portNew) {
        this.portNew = portNew;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Integer getSendDone() {
        return sendDone;
    }

    public void setSendDone(Integer sendDone) {
        this.sendDone = sendDone;
    }
}