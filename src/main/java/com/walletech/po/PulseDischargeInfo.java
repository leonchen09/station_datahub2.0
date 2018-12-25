package com.walletech.po;

import java.math.BigDecimal;

public class PulseDischargeInfo {
    private Integer id;

    private Integer pulseDischargeSendId;

    private String voltage;

    private String current;

    private BigDecimal impendance;

    private Integer pointsNumber;

    private String filterVoltage;

    private String filterCurrent;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPulseDischargeSendId() {
        return pulseDischargeSendId;
    }

    public void setPulseDischargeSendId(Integer pulseDischargeSendId) {
        this.pulseDischargeSendId = pulseDischargeSendId;
    }

    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage == null ? null : voltage.trim();
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current == null ? null : current.trim();
    }

    public BigDecimal getImpendance() {
        return impendance;
    }

    public void setImpendance(BigDecimal impendance) {
        this.impendance = impendance;
    }

    public Integer getPointsNumber() {
        return pointsNumber;
    }

    public void setPointsNumber(Integer pointsNumber) {
        this.pointsNumber = pointsNumber;
    }

    public String getFilterVoltage() {
        return filterVoltage;
    }

    public void setFilterVoltage(String filterVoltage) {
        this.filterVoltage = filterVoltage;
    }

    public String getFilterCurrent() {
        return filterCurrent;
    }

    public void setFilterCurrent(String filterCurrent) {
        this.filterCurrent = filterCurrent;
    }
}