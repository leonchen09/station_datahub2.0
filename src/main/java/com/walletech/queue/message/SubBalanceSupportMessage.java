package com.walletech.queue.message;

import com.walletech.po.GprsDeviceInfo;
import com.walletech.po.SubDeviceBalanceConfig;

import java.util.List;

public class SubBalanceSupportMessage extends Message {

    private GprsDeviceInfo info;

    private List<SubDeviceBalanceConfig> configs;

    public GprsDeviceInfo getInfo() {
        return info;
    }

    public void setInfo(GprsDeviceInfo info) {
        this.info = info;
    }

    public List<SubDeviceBalanceConfig> getConfigs() {
        return configs;
    }

    public void setConfigs(List<SubDeviceBalanceConfig> configs) {
        this.configs = configs;
    }
}
