package com.walletech.po;

import java.util.Date;

public class VersionBalanceInfo {
    private Integer id;

    private String gprsId;

    private Integer para1;

    private Integer para2;

    private Integer para3;

    private Integer para4;

    private Integer para5;

    private Integer para6;

    private Integer para7;

    private String bluMasterId;

    private String masterVersion;

    private String bluMasterVersion;

    private String iccid;

    private Date updateTime;

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

    public Integer getPara1() {
        return para1;
    }

    public void setPara1(Integer para1) {
        this.para1 = para1;
    }

    public Integer getPara2() {
        return para2;
    }

    public void setPara2(Integer para2) {
        this.para2 = para2;
    }

    public Integer getPara3() {
        return para3;
    }

    public void setPara3(Integer para3) {
        this.para3 = para3;
    }

    public Integer getPara4() {
        return para4;
    }

    public void setPara4(Integer para4) {
        this.para4 = para4;
    }

    public Integer getPara5() {
        return para5;
    }

    public void setPara5(Integer para5) {
        this.para5 = para5;
    }

    public Integer getPara6() {
        return para6;
    }

    public void setPara6(Integer para6) {
        this.para6 = para6;
    }

    public Integer getPara7() {
        return para7;
    }

    public void setPara7(Integer para7) {
        this.para7 = para7;
    }

    public String getBluMasterId() {
        return bluMasterId;
    }

    public void setBluMasterId(String bluMasterId) {
        this.bluMasterId = bluMasterId == null ? null : bluMasterId.trim();
    }

    public String getMasterVersion() {
        return masterVersion;
    }

    public void setMasterVersion(String masterVersion) {
        this.masterVersion = masterVersion == null ? null : masterVersion.trim();
    }

    public String getBluMasterVersion() {
        return bluMasterVersion;
    }

    public void setBluMasterVersion(String bluMasterVersion) {
        this.bluMasterVersion = bluMasterVersion == null ? null : bluMasterVersion.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }
}