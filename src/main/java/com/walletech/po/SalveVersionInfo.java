package com.walletech.po;

import java.util.Date;

public class SalveVersionInfo {
    private String gprsId;

    private Integer cellIndex;

    private String version;

    private Date updateTme;

    public String getGprsId() {
        return gprsId;
    }

    public void setGprsId(String gprsId) {
        this.gprsId = gprsId == null ? null : gprsId.trim();
    }

    public Integer getCellIndex() {
        return cellIndex;
    }

    public void setCellIndex(Integer cellIndex) {
        this.cellIndex = cellIndex;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version == null ? null : version.trim();
    }

    public Date getUpdateTme() {
        return updateTme;
    }

    public void setUpdateTme(Date updateTme) {
        this.updateTme = updateTme;
    }
}