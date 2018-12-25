package com.walletech.po;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class GprsDeviceInfo implements Serializable {
    private static final long serialVersionUID = -8662230338481047092L;
    private Integer id;

    private String gprsId;

    private String gprsIdOut;

    private Integer deviceType;

    private Date rcvTime;

    private Boolean linkStatus;

    private Integer companyId3;

    private Integer gprsFlag;

    private String gprsPort;

    private String gprsSpec;

    private Integer subDeviceCount;

    private Integer validDay;

    private Integer hallFlag;

    private Integer connectionType;

    private Integer heartbeatInterval;

    private Integer floatInterval;

    private Integer dischargeInterval;

    private Integer chargeInterval;

    private Integer fastSampleInterval;

    private Integer slowSampleInterval;

    private Integer dischargeTime;

    private Integer slowSampleTime;

    private BigDecimal consoleCellCapError;

    private BigDecimal consoleCellCapNormal;

    private Integer durationExcellent;

    private Integer durationGood;

    private Integer durationMedium;

    private BigDecimal validDischargeVol;

    private BigDecimal validChargeVol;

    private BigDecimal validDischargeCur;

    private BigDecimal validChargeCur;

    private BigDecimal downCur;

    private BigDecimal downVol;

    private BigDecimal marginTime;

    private String suggestTime;

    private String suggestCellCapPercent;

    private BigDecimal suggestResistThreshold;

    private Integer suggestDurationPercent;

    private BigDecimal suggestLoadCurThreshold;

    private BigDecimal maxDischargeCur;

    private BigDecimal volHighWarningThreshold;

    private BigDecimal dischargeThreshold;

    private BigDecimal chargeThreshold;

    private BigDecimal volLowWarningThreshold;

    private BigDecimal currentWarningToplimit;

    private BigDecimal currentWarningLowerlimit;

    private BigDecimal singleHighVolThreshold;

    private BigDecimal singleLowVolThreshold;

    private Integer temHighWarningThreshold;

    private Integer temLowWarningThreshold;

    private Integer socLowWarningThreshold;

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

    private Integer subBalanceCount;

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

    public String getGprsIdOut() {
        return gprsIdOut;
    }

    public void setGprsIdOut(String gprsIdOut) {
        this.gprsIdOut = gprsIdOut == null ? null : gprsIdOut.trim();
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public Date getRcvTime() {
        return rcvTime;
    }

    public void setRcvTime(Date rcvTime) {
        this.rcvTime = rcvTime;
    }

    public Boolean getLinkStatus() {
        return linkStatus;
    }

    public void setLinkStatus(Boolean linkStatus) {
        this.linkStatus = linkStatus;
    }

    public Integer getCompanyId3() {
        return companyId3;
    }

    public void setCompanyId3(Integer companyId3) {
        this.companyId3 = companyId3;
    }

    public Integer getGprsFlag() {
        return gprsFlag;
    }

    public void setGprsFlag(Integer gprsFlag) {
        this.gprsFlag = gprsFlag;
    }

    public String getGprsPort() {
        return gprsPort;
    }

    public void setGprsPort(String gprsPort) {
        this.gprsPort = gprsPort == null ? null : gprsPort.trim();
    }

    public String getGprsSpec() {
        return gprsSpec;
    }

    public void setGprsSpec(String gprsSpec) {
        this.gprsSpec = gprsSpec == null ? null : gprsSpec.trim();
    }

    public Integer getSubDeviceCount() {
        return subDeviceCount;
    }

    public void setSubDeviceCount(Integer subDeviceCount) {
        this.subDeviceCount = subDeviceCount;
    }

    public Integer getValidDay() {
        return validDay;
    }

    public void setValidDay(Integer validDay) {
        this.validDay = validDay;
    }

    public Integer getHallFlag() {
        return hallFlag;
    }

    public void setHallFlag(Integer hallFlag) {
        this.hallFlag = hallFlag;
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

    public Integer getFastSampleInterval() {
        return fastSampleInterval;
    }

    public void setFastSampleInterval(Integer fastSampleInterval) {
        this.fastSampleInterval = fastSampleInterval;
    }

    public Integer getSlowSampleInterval() {
        return slowSampleInterval;
    }

    public void setSlowSampleInterval(Integer slowSampleInterval) {
        this.slowSampleInterval = slowSampleInterval;
    }

    public Integer getDischargeTime() {
        return dischargeTime;
    }

    public void setDischargeTime(Integer dischargeTime) {
        this.dischargeTime = dischargeTime;
    }

    public Integer getSlowSampleTime() {
        return slowSampleTime;
    }

    public void setSlowSampleTime(Integer slowSampleTime) {
        this.slowSampleTime = slowSampleTime;
    }

    public BigDecimal getConsoleCellCapError() {
        return consoleCellCapError;
    }

    public void setConsoleCellCapError(BigDecimal consoleCellCapError) {
        this.consoleCellCapError = consoleCellCapError;
    }

    public BigDecimal getConsoleCellCapNormal() {
        return consoleCellCapNormal;
    }

    public void setConsoleCellCapNormal(BigDecimal consoleCellCapNormal) {
        this.consoleCellCapNormal = consoleCellCapNormal;
    }

    public Integer getDurationExcellent() {
        return durationExcellent;
    }

    public void setDurationExcellent(Integer durationExcellent) {
        this.durationExcellent = durationExcellent;
    }

    public Integer getDurationGood() {
        return durationGood;
    }

    public void setDurationGood(Integer durationGood) {
        this.durationGood = durationGood;
    }

    public Integer getDurationMedium() {
        return durationMedium;
    }

    public void setDurationMedium(Integer durationMedium) {
        this.durationMedium = durationMedium;
    }

    public BigDecimal getValidDischargeVol() {
        return validDischargeVol;
    }

    public void setValidDischargeVol(BigDecimal validDischargeVol) {
        this.validDischargeVol = validDischargeVol;
    }

    public BigDecimal getValidChargeVol() {
        return validChargeVol;
    }

    public void setValidChargeVol(BigDecimal validChargeVol) {
        this.validChargeVol = validChargeVol;
    }

    public BigDecimal getValidDischargeCur() {
        return validDischargeCur;
    }

    public void setValidDischargeCur(BigDecimal validDischargeCur) {
        this.validDischargeCur = validDischargeCur;
    }

    public BigDecimal getValidChargeCur() {
        return validChargeCur;
    }

    public void setValidChargeCur(BigDecimal validChargeCur) {
        this.validChargeCur = validChargeCur;
    }

    public BigDecimal getDownCur() {
        return downCur;
    }

    public void setDownCur(BigDecimal downCur) {
        this.downCur = downCur;
    }

    public BigDecimal getDownVol() {
        return downVol;
    }

    public void setDownVol(BigDecimal downVol) {
        this.downVol = downVol;
    }

    public BigDecimal getMarginTime() {
        return marginTime;
    }

    public void setMarginTime(BigDecimal marginTime) {
        this.marginTime = marginTime;
    }

    public String getSuggestTime() {
        return suggestTime;
    }

    public void setSuggestTime(String suggestTime) {
        this.suggestTime = suggestTime == null ? null : suggestTime.trim();
    }

    public String getSuggestCellCapPercent() {
        return suggestCellCapPercent;
    }

    public void setSuggestCellCapPercent(String suggestCellCapPercent) {
        this.suggestCellCapPercent = suggestCellCapPercent == null ? null : suggestCellCapPercent.trim();
    }

    public BigDecimal getSuggestResistThreshold() {
        return suggestResistThreshold;
    }

    public void setSuggestResistThreshold(BigDecimal suggestResistThreshold) {
        this.suggestResistThreshold = suggestResistThreshold;
    }

    public Integer getSuggestDurationPercent() {
        return suggestDurationPercent;
    }

    public void setSuggestDurationPercent(Integer suggestDurationPercent) {
        this.suggestDurationPercent = suggestDurationPercent;
    }

    public BigDecimal getSuggestLoadCurThreshold() {
        return suggestLoadCurThreshold;
    }

    public void setSuggestLoadCurThreshold(BigDecimal suggestLoadCurThreshold) {
        this.suggestLoadCurThreshold = suggestLoadCurThreshold;
    }

    public BigDecimal getMaxDischargeCur() {
        return maxDischargeCur;
    }

    public void setMaxDischargeCur(BigDecimal maxDischargeCur) {
        this.maxDischargeCur = maxDischargeCur;
    }

    public BigDecimal getVolHighWarningThreshold() {
        return volHighWarningThreshold;
    }

    public void setVolHighWarningThreshold(BigDecimal volHighWarningThreshold) {
        this.volHighWarningThreshold = volHighWarningThreshold;
    }

    public BigDecimal getDischargeThreshold() {
        return dischargeThreshold;
    }

    public void setDischargeThreshold(BigDecimal dischargeThreshold) {
        this.dischargeThreshold = dischargeThreshold;
    }

    public BigDecimal getChargeThreshold() {
        return chargeThreshold;
    }

    public void setChargeThreshold(BigDecimal chargeThreshold) {
        this.chargeThreshold = chargeThreshold;
    }

    public BigDecimal getVolLowWarningThreshold() {
        return volLowWarningThreshold;
    }

    public void setVolLowWarningThreshold(BigDecimal volLowWarningThreshold) {
        this.volLowWarningThreshold = volLowWarningThreshold;
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

    public Integer getSubBalanceCount() {
        return subBalanceCount;
    }

    public void setSubBalanceCount(Integer subBalanceCount) {
        this.subBalanceCount = subBalanceCount;
    }
}