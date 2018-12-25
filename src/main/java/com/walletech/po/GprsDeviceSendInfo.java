package com.walletech.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.walletech.bo.BaseRabbitMqInfo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class GprsDeviceSendInfo extends BaseRabbitMqInfo implements Serializable {

    private static final long serialVersionUID = 7935415424440003027L;
    private Integer id;

    private String gprsId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date insertTime;

    private Integer connectionType;

    private Integer heartbeatInterval;

    private Integer floatInterval;

    private Integer dischargeInterval;

    private Integer chargeInterval;

    private BigDecimal chargeThreshold;

    private BigDecimal dischargeThreshold;

    private BigDecimal nominalCapacity;

    private BigDecimal currentCapacity;

    private BigDecimal volHighWarningThreshold;

    private BigDecimal volLowWarningThreshold;

    private Integer temHighWarningThreshold;

    private Integer temLowWarningThreshold;

    private Integer socLowWarningThreshold;

    private Integer sendDone;

    private BigDecimal currentWarningToplimit;

    private BigDecimal currentWarningLowerlimit;

    private BigDecimal singleHighVolThreshold;

    private BigDecimal singleLowVolThreshold;

    private BigDecimal highVolRecover;

    private BigDecimal lowVolRecover;

    private BigDecimal singleHighVolRecover;

    private BigDecimal singleLowVolRecover;

    private Integer highSurroundingtemWarningThreshold;

    private Integer lowSurroundingtemWarningThreshold;

    private Integer highSurroundingtemWarningRecover;

    private Integer lowSurroundingtemWarningRecover;

    private Integer hightemWarningRecover;

    private Integer lowtemWarningRecover;

    private Integer lowSocRecover;

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

    public Integer getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(Integer connectionType) {
        this.connectionType = connectionType;
    }

    public Integer getHeartbeatInterval() {
        return heartbeatInterval;
    }

    public void setHeartbeatInterval(Integer heartbeatInterval) {
        this.heartbeatInterval = heartbeatInterval;
    }

    public Integer getFloatInterval() {
        return floatInterval;
    }

    public void setFloatInterval(Integer floatInterval) {
        this.floatInterval = floatInterval;
    }

    public Integer getDischargeInterval() {
        return dischargeInterval;
    }

    public void setDischargeInterval(Integer dischargeInterval) {
        this.dischargeInterval = dischargeInterval;
    }

    public Integer getChargeInterval() {
        return chargeInterval;
    }

    public void setChargeInterval(Integer chargeInterval) {
        this.chargeInterval = chargeInterval;
    }

    public BigDecimal getChargeThreshold() {
        return chargeThreshold;
    }

    public void setChargeThreshold(BigDecimal chargeThreshold) {
        this.chargeThreshold = chargeThreshold;
    }

    public BigDecimal getDischargeThreshold() {
        return dischargeThreshold;
    }

    public void setDischargeThreshold(BigDecimal dischargeThreshold) {
        this.dischargeThreshold = dischargeThreshold;
    }

    public BigDecimal getNominalCapacity() {
        return nominalCapacity;
    }

    public void setNominalCapacity(BigDecimal nominalCapacity) {
        this.nominalCapacity = nominalCapacity;
    }

    public BigDecimal getCurrentCapacity() {
        return currentCapacity;
    }

    public void setCurrentCapacity(BigDecimal currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

    public BigDecimal getVolHighWarningThreshold() {
        return volHighWarningThreshold;
    }

    public void setVolHighWarningThreshold(BigDecimal volHighWarningThreshold) {
        this.volHighWarningThreshold = volHighWarningThreshold;
    }

    public BigDecimal getVolLowWarningThreshold() {
        return volLowWarningThreshold;
    }

    public void setVolLowWarningThreshold(BigDecimal volLowWarningThreshold) {
        this.volLowWarningThreshold = volLowWarningThreshold;
    }

    public Integer getTemHighWarningThreshold() {
        return temHighWarningThreshold;
    }

    public void setTemHighWarningThreshold(Integer temHighWarningThreshold) {
        this.temHighWarningThreshold = temHighWarningThreshold;
    }

    public Integer getTemLowWarningThreshold() {
        return temLowWarningThreshold;
    }

    public void setTemLowWarningThreshold(Integer temLowWarningThreshold) {
        this.temLowWarningThreshold = temLowWarningThreshold;
    }

    public Integer getSocLowWarningThreshold() {
        return socLowWarningThreshold;
    }

    public void setSocLowWarningThreshold(Integer socLowWarningThreshold) {
        this.socLowWarningThreshold = socLowWarningThreshold;
    }

    public Integer getSendDone() {
        return sendDone;
    }

    public void setSendDone(Integer sendDone) {
        this.sendDone = sendDone;
    }

    public BigDecimal getCurrentWarningToplimit() {
        return currentWarningToplimit;
    }

    public void setCurrentWarningToplimit(BigDecimal currentWarningToplimit) {
        this.currentWarningToplimit = currentWarningToplimit;
    }

    public BigDecimal getCurrentWarningLowerlimit() {
        return currentWarningLowerlimit;
    }

    public void setCurrentWarningLowerlimit(BigDecimal currentWarningLowerlimit) {
        this.currentWarningLowerlimit = currentWarningLowerlimit;
    }

    public BigDecimal getSingleHighVolThreshold() {
        return singleHighVolThreshold;
    }

    public void setSingleHighVolThreshold(BigDecimal singleHighVolThreshold) {
        this.singleHighVolThreshold = singleHighVolThreshold;
    }

    public BigDecimal getSingleLowVolThreshold() {
        return singleLowVolThreshold;
    }

    public void setSingleLowVolThreshold(BigDecimal singleLowVolThreshold) {
        this.singleLowVolThreshold = singleLowVolThreshold;
    }

    public BigDecimal getHighVolRecover() {
        return highVolRecover;
    }

    public void setHighVolRecover(BigDecimal highVolRecover) {
        this.highVolRecover = highVolRecover;
    }

    public BigDecimal getLowVolRecover() {
        return lowVolRecover;
    }

    public void setLowVolRecover(BigDecimal lowVolRecover) {
        this.lowVolRecover = lowVolRecover;
    }

    public BigDecimal getSingleHighVolRecover() {
        return singleHighVolRecover;
    }

    public void setSingleHighVolRecover(BigDecimal singleHighVolRecover) {
        this.singleHighVolRecover = singleHighVolRecover;
    }

    public BigDecimal getSingleLowVolRecover() {
        return singleLowVolRecover;
    }

    public void setSingleLowVolRecover(BigDecimal singleLowVolRecover) {
        this.singleLowVolRecover = singleLowVolRecover;
    }

    public Integer getHighSurroundingtemWarningThreshold() {
        return highSurroundingtemWarningThreshold;
    }

    public void setHighSurroundingtemWarningThreshold(Integer highSurroundingtemWarningThreshold) {
        this.highSurroundingtemWarningThreshold = highSurroundingtemWarningThreshold;
    }

    public Integer getLowSurroundingtemWarningThreshold() {
        return lowSurroundingtemWarningThreshold;
    }

    public void setLowSurroundingtemWarningThreshold(Integer lowSurroundingtemWarningThreshold) {
        this.lowSurroundingtemWarningThreshold = lowSurroundingtemWarningThreshold;
    }

    public Integer getHighSurroundingtemWarningRecover() {
        return highSurroundingtemWarningRecover;
    }

    public void setHighSurroundingtemWarningRecover(Integer highSurroundingtemWarningRecover) {
        this.highSurroundingtemWarningRecover = highSurroundingtemWarningRecover;
    }

    public Integer getLowSurroundingtemWarningRecover() {
        return lowSurroundingtemWarningRecover;
    }

    public void setLowSurroundingtemWarningRecover(Integer lowSurroundingtemWarningRecover) {
        this.lowSurroundingtemWarningRecover = lowSurroundingtemWarningRecover;
    }

    public Integer getHightemWarningRecover() {
        return hightemWarningRecover;
    }

    public void setHightemWarningRecover(Integer hightemWarningRecover) {
        this.hightemWarningRecover = hightemWarningRecover;
    }

    public Integer getLowtemWarningRecover() {
        return lowtemWarningRecover;
    }

    public void setLowtemWarningRecover(Integer lowtemWarningRecover) {
        this.lowtemWarningRecover = lowtemWarningRecover;
    }

    public Integer getLowSocRecover() {
        return lowSocRecover;
    }

    public void setLowSocRecover(Integer lowSocRecover) {
        this.lowSocRecover = lowSocRecover;
    }
}