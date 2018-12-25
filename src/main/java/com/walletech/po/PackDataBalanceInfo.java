package com.walletech.po;

import java.math.BigDecimal;
import java.util.Date;

public class PackDataBalanceInfo {
    private Integer id;

    private Integer stationId;

    private String gprsId;

    private Date rcvTime;

    private Byte balanceSubdeviceCode;

    private BigDecimal lowVol;

    private BigDecimal highVol;

    private BigDecimal lowCur;

    private BigDecimal highCur;

    private BigDecimal lowPower;

    private BigDecimal highPower;

    private BigDecimal powerTem;

    private Integer currentPattern;

    private Integer currentStatus;

    private Integer overcurrentWarn;

    private Integer upHighVolWarn;

    private Integer upLowVolWarn;

    private Integer downHighVolWarn;

    private Integer downLowVolWarn;

    private Integer overcurrentTem;

    private Integer upBalance;

    private Integer downBalance;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    public String getGprsId() {
        return gprsId;
    }

    public void setGprsId(String gprsId) {
        this.gprsId = gprsId == null ? null : gprsId.trim();
    }

    public Date getRcvTime() {
        return rcvTime;
    }

    public void setRcvTime(Date rcvTime) {
        this.rcvTime = rcvTime;
    }

    public Byte getBalanceSubdeviceCode() {
        return balanceSubdeviceCode;
    }

    public void setBalanceSubdeviceCode(Byte balanceSubdeviceCode) {
        this.balanceSubdeviceCode = balanceSubdeviceCode;
    }

    public BigDecimal getLowVol() {
        return lowVol;
    }

    public void setLowVol(BigDecimal lowVol) {
        this.lowVol = lowVol;
    }

    public BigDecimal getHighVol() {
        return highVol;
    }

    public void setHighVol(BigDecimal highVol) {
        this.highVol = highVol;
    }

    public BigDecimal getLowCur() {
        return lowCur;
    }

    public void setLowCur(BigDecimal lowCur) {
        this.lowCur = lowCur;
    }

    public BigDecimal getHighCur() {
        return highCur;
    }

    public void setHighCur(BigDecimal highCur) {
        this.highCur = highCur;
    }

    public BigDecimal getLowPower() {
        return lowPower;
    }

    public void setLowPower(BigDecimal lowPower) {
        this.lowPower = lowPower;
    }

    public BigDecimal getHighPower() {
        return highPower;
    }

    public void setHighPower(BigDecimal highPower) {
        this.highPower = highPower;
    }

    public BigDecimal getPowerTem() {
        return powerTem;
    }

    public void setPowerTem(BigDecimal powerTem) {
        this.powerTem = powerTem;
    }

    public Integer getCurrentPattern() {
        return currentPattern;
    }

    public void setCurrentPattern(Integer currentPattern) {
        this.currentPattern = currentPattern;
    }

    public Integer getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(Integer currentStatus) {
        this.currentStatus = currentStatus;
    }

    public Integer getOvercurrentWarn() {
        return overcurrentWarn;
    }

    public void setOvercurrentWarn(Integer overcurrentWarn) {
        this.overcurrentWarn = overcurrentWarn;
    }

    public Integer getUpHighVolWarn() {
        return upHighVolWarn;
    }

    public void setUpHighVolWarn(Integer upHighVolWarn) {
        this.upHighVolWarn = upHighVolWarn;
    }

    public Integer getUpLowVolWarn() {
        return upLowVolWarn;
    }

    public void setUpLowVolWarn(Integer upLowVolWarn) {
        this.upLowVolWarn = upLowVolWarn;
    }

    public Integer getDownHighVolWarn() {
        return downHighVolWarn;
    }

    public void setDownHighVolWarn(Integer downHighVolWarn) {
        this.downHighVolWarn = downHighVolWarn;
    }

    public Integer getDownLowVolWarn() {
        return downLowVolWarn;
    }

    public void setDownLowVolWarn(Integer downLowVolWarn) {
        this.downLowVolWarn = downLowVolWarn;
    }

    public Integer getOvercurrentTem() {
        return overcurrentTem;
    }

    public void setOvercurrentTem(Integer overcurrentTem) {
        this.overcurrentTem = overcurrentTem;
    }


}