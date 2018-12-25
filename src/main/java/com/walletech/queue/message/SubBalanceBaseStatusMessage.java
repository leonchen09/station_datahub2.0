package com.walletech.queue.message;

import com.walletech.po.PackDataBalanceInfo;
import com.walletech.po.SubDeviceBalanceConfig;

import java.util.List;

public class SubBalanceBaseStatusMessage extends Message {
    private List<PackDataBalanceInfo> infos;

    private String gprsId;

    private List<SubDeviceBalanceConfig> configs;

    public List<SubDeviceBalanceConfig> getConfigs() {
        return configs;
    }

    public void setConfigs(List<SubDeviceBalanceConfig> configs) {
        this.configs = configs;
    }

    public String getGprsId() {
        return gprsId;
    }

    public void setGprsId(String gprsId) {
        this.gprsId = gprsId;
    }

    public List<PackDataBalanceInfo> getInfos() {
        return infos;
    }

    public void setInfos(List<PackDataBalanceInfo> infos) {
        this.infos = infos;
    }
}
