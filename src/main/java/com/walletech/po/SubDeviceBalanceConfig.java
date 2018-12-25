package com.walletech.po;

import java.math.BigDecimal;
import java.util.Date;

public class SubDeviceBalanceConfig {
    private Integer id;

    private String gprsId;

    private Date rcvTime;

    private Byte balanceSubdeviceCode;

    private BigDecimal upPatternInPower;

    private BigDecimal upPatternOutVol;

    private BigDecimal downPatternOutCur;

    private BigDecimal downPatternOutVol;

    private BigDecimal upPatternLowVolThreshold;

    private BigDecimal upPatternHighVolConstantVol;

    private BigDecimal upPatternHighVolThreshold;

    private BigDecimal downPatternHighVolLowVolThreshold;

    private BigDecimal downPatternDownVolConstant;

    private BigDecimal downPatternHighVolThreshold;

    private Integer isBalance;

    private Integer isValid;

    private BigDecimal upBalanceTime;

    private BigDecimal minDischargeVolThreshold;

    private BigDecimal lowFloatingDischargeVolThreshold;

    private BigDecimal lowFloatingDischargeCurThreshold;

    private BigDecimal downPatternThreeChangeFloatingCur;

    private BigDecimal downPatternThreeFloatingCurUpper;

    private String cellList;

    private Integer balanceLinkWay;


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

    public BigDecimal getUpPatternInPower() {
        return upPatternInPower;
    }

    public void setUpPatternInPower(BigDecimal upPatternInPower) {
        this.upPatternInPower = upPatternInPower;
    }

    public BigDecimal getUpPatternOutVol() {
        return upPatternOutVol;
    }

    public void setUpPatternOutVol(BigDecimal upPatternOutVol) {
        this.upPatternOutVol = upPatternOutVol;
    }

    public BigDecimal getDownPatternOutCur() {
        return downPatternOutCur;
    }

    public void setDownPatternOutCur(BigDecimal downPatternOutCur) {
        this.downPatternOutCur = downPatternOutCur;
    }

    public BigDecimal getDownPatternOutVol() {
        return downPatternOutVol;
    }

    public void setDownPatternOutVol(BigDecimal downPatternOutVol) {
        this.downPatternOutVol = downPatternOutVol;
    }

    public BigDecimal getUpPatternLowVolThreshold() {
        return upPatternLowVolThreshold;
    }

    public void setUpPatternLowVolThreshold(BigDecimal upPatternLowVolThreshold) {
        this.upPatternLowVolThreshold = upPatternLowVolThreshold;
    }

    public BigDecimal getUpPatternHighVolConstantVol() {
        return upPatternHighVolConstantVol;
    }

    public void setUpPatternHighVolConstantVol(BigDecimal upPatternHighVolConstantVol) {
        this.upPatternHighVolConstantVol = upPatternHighVolConstantVol;
    }

    public BigDecimal getUpPatternHighVolThreshold() {
        return upPatternHighVolThreshold;
    }

    public void setUpPatternHighVolThreshold(BigDecimal upPatternHighVolThreshold) {
        this.upPatternHighVolThreshold = upPatternHighVolThreshold;
    }

    public BigDecimal getDownPatternHighVolLowVolThreshold() {
        return downPatternHighVolLowVolThreshold;
    }

    public void setDownPatternHighVolLowVolThreshold(BigDecimal downPatternHighVolLowVolThreshold) {
        this.downPatternHighVolLowVolThreshold = downPatternHighVolLowVolThreshold;
    }

    public BigDecimal getDownPatternDownVolConstant() {
        return downPatternDownVolConstant;
    }

    public void setDownPatternDownVolConstant(BigDecimal downPatternDownVolConstant) {
        this.downPatternDownVolConstant = downPatternDownVolConstant;
    }

    public BigDecimal getDownPatternHighVolThreshold() {
        return downPatternHighVolThreshold;
    }

    public void setDownPatternHighVolThreshold(BigDecimal downPatternHighVolThreshold) {
        this.downPatternHighVolThreshold = downPatternHighVolThreshold;
    }

    public Integer getIsBalance() {
        return isBalance;
    }

    public void setIsBalance(Integer isBalance) {
        this.isBalance = isBalance;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }

    public BigDecimal getUpBalanceTime() {
        return upBalanceTime;
    }

    public void setUpBalanceTime(BigDecimal upBalanceTime) {
        this.upBalanceTime = upBalanceTime;
    }

    public BigDecimal getMinDischargeVolThreshold() {
        return minDischargeVolThreshold;
    }

    public void setMinDischargeVolThreshold(BigDecimal minDischargeVolThreshold) {
        this.minDischargeVolThreshold = minDischargeVolThreshold;
    }

    public BigDecimal getLowFloatingDischargeVolThreshold() {
        return lowFloatingDischargeVolThreshold;
    }

    public void setLowFloatingDischargeVolThreshold(BigDecimal lowFloatingDischargeVolThreshold) {
        this.lowFloatingDischargeVolThreshold = lowFloatingDischargeVolThreshold;
    }

    public BigDecimal getLowFloatingDischargeCurThreshold() {
        return lowFloatingDischargeCurThreshold;
    }

    public void setLowFloatingDischargeCurThreshold(BigDecimal lowFloationDischargeCurThreshold) {
        this.lowFloatingDischargeCurThreshold = lowFloationDischargeCurThreshold;
    }

    public BigDecimal getDownPatternThreeChangeFloatingCur() {
        return downPatternThreeChangeFloatingCur;
    }

    public void setDownPatternThreeChangeFloatingCur(BigDecimal downPatternThreeChangeFloatingCur) {
        this.downPatternThreeChangeFloatingCur = downPatternThreeChangeFloatingCur;
    }

    public BigDecimal getDownPatternThreeFloatingCurUpper() {
        return downPatternThreeFloatingCurUpper;
    }

    public void setDownPatternThreeFloatingCurUpper(BigDecimal downPatternThreeFloatingCurUpper) {
        this.downPatternThreeFloatingCurUpper = downPatternThreeFloatingCurUpper;
    }

    public String getCellList() {
        return cellList;
    }

    public void setCellList(String cellList) {
        this.cellList = cellList;
    }

    public Integer getBalanceLinkWay() {
        return balanceLinkWay;
    }

    public void setBalanceLinkWay(Integer balanceLinkWay) {
        this.balanceLinkWay = balanceLinkWay;
    }
}