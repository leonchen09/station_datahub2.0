package com.walletech.po;

import com.walletech.bo.BaseRabbitMqInfo;

import java.util.Date;

public class SubBalanceStatusSend extends BaseRabbitMqInfo {
    private Integer id;

    private String gprsId;

    private Date insertTime;

    private Byte balanceSubdeviceCode;

    private Integer upBalance;

    private Integer downBalance;

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

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public Byte getBalanceSubdeviceCode() {
        return balanceSubdeviceCode;
    }

    public void setBalanceSubdeviceCode(Byte balanceSubdeviceCode) {
        this.balanceSubdeviceCode = balanceSubdeviceCode;
    }

    public Integer getSendDone() {
        return sendDone;
    }

    public void setSendDone(Integer sendDone) {
        this.sendDone = sendDone;
    }

    public Integer getUpBalance() {
        return upBalance;
    }

    public void setUpBalance(Integer upBalance) {
        this.upBalance = upBalance;
    }

    public Integer getDownBalance() {
        return downBalance;
    }

    public void setDownBalance(Integer downBalance) {
        this.downBalance = downBalance;
    }
}