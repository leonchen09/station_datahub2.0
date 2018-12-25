package com.walletech.po;

import java.util.Date;

public class ConnectFailedAddr {
    private Integer id;

    private String gprsId;

    private int addressType;

    private String errorAddress;

    private Date rcvTiem;

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
        this.gprsId = gprsId;
    }

    public int getAddressType() {
        return addressType;
    }

    public void setAddressType(int addressType) {
        this.addressType = addressType;
    }

    public String getErrorAddress() {
        return errorAddress;
    }

    public void setErrorAddress(String errorAddress) {
        this.errorAddress = errorAddress == null ? null : errorAddress.trim();
    }

    public Date getRcvTiem() {
        return rcvTiem;
    }

    public void setRcvTiem(Date rcvTiem) {
        this.rcvTiem = rcvTiem;
    }
}