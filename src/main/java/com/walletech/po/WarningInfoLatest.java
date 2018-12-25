package com.walletech.po;

import java.util.Date;

public class WarningInfoLatest {
    private Integer id;

    private Integer stationId;

    private String gprsId;

    private Date rcvTime;

    private Byte cellTemHigh;

    private Byte envTemHigh;

    private Byte cellTemLow;

    private Byte envTemLow;

    private Byte genVolHigh;

    private Byte genVolLow;

    private Byte lossElectricity;

    private Byte socLow;

    private Byte abnormalCurrent;

    private Byte singleVolHigh;

    private Byte singleVolLow;

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

    public Byte getCellTemHigh() {
        return cellTemHigh;
    }

    public void setCellTemHigh(Byte cellTemHigh) {
        this.cellTemHigh = cellTemHigh;
    }

    public Byte getEnvTemHigh() {
        return envTemHigh;
    }

    public void setEnvTemHigh(Byte envTemHigh) {
        this.envTemHigh = envTemHigh;
    }

    public Byte getCellTemLow() {
        return cellTemLow;
    }

    public void setCellTemLow(Byte cellTemLow) {
        this.cellTemLow = cellTemLow;
    }

    public Byte getEnvTemLow() {
        return envTemLow;
    }

    public void setEnvTemLow(Byte envTemLow) {
        this.envTemLow = envTemLow;
    }

    public Byte getGenVolHigh() {
        return genVolHigh;
    }

    public void setGenVolHigh(Byte genVolHigh) {
        this.genVolHigh = genVolHigh;
    }

    public Byte getGenVolLow() {
        return genVolLow;
    }

    public void setGenVolLow(Byte genVolLow) {
        this.genVolLow = genVolLow;
    }

    public Byte getLossElectricity() {
        return lossElectricity;
    }

    public void setLossElectricity(Byte lossElectricity) {
        this.lossElectricity = lossElectricity;
    }

    public Byte getSocLow() {
        return socLow;
    }

    public void setSocLow(Byte socLow) {
        this.socLow = socLow;
    }

    public Byte getAbnormalCurrent() {
        return abnormalCurrent;
    }

    public void setAbnormalCurrent(Byte abnormalCurrent) {
        this.abnormalCurrent = abnormalCurrent;
    }

    public Byte getSingleVolHigh() {
        return singleVolHigh;
    }

    public void setSingleVolHigh(Byte singleVolHigh) {
        this.singleVolHigh = singleVolHigh;
    }

    public Byte getSingleVolLow() {
        return singleVolLow;
    }

    public void setSingleVolLow(Byte singleVolLow) {
        this.singleVolLow = singleVolLow;
    }
}