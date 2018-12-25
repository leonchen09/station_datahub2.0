package com.walletech.po;

import java.util.Date;

public class ConnectAddrInfo {
    private Integer id;

    private String gprsId;

    private Integer type1;

    private String adress1;

    private Integer type2;

    private String adress2;

    private Integer type3;

    private String adress3;

    private Integer type4;

    private String adress4;

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

    public Integer getType1() {
        return type1;
    }

    public void setType1(Integer type1) {
        this.type1 = type1;
    }

    public String getAdress1() {
        return adress1;
    }

    public void setAdress1(String adress1) {
        this.adress1 = adress1 == null ? null : adress1.trim();
    }

    public Integer getType2() {
        return type2;
    }

    public void setType2(Integer type2) {
        this.type2 = type2;
    }

    public String getAdress2() {
        return adress2;
    }

    public void setAdress2(String adress2) {
        this.adress2 = adress2 == null ? null : adress2.trim();
    }

    public Integer getType3() {
        return type3;
    }

    public void setType3(Integer type3) {
        this.type3 = type3;
    }

    public String getAdress3() {
        return adress3;
    }

    public void setAdress3(String adress3) {
        this.adress3 = adress3 == null ? null : adress3.trim();
    }

    public Integer getType4() {
        return type4;
    }

    public void setType4(Integer type4) {
        this.type4 = type4;
    }

    public String getAdress4() {
        return adress4;
    }

    public void setAdress4(String adress4) {
        this.adress4 = adress4 == null ? null : adress4.trim();
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