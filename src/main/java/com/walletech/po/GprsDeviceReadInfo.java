package com.walletech.po;

import java.util.Date;

public class GprsDeviceReadInfo {
    private Integer id;

    private String gprsId;

    private Boolean readDone;

    private Date readTime;

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

    public Boolean getReadDone() {
        return readDone;
    }

    public void setReadDone(Boolean readDone) {
        this.readDone = readDone;
    }

    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }
}