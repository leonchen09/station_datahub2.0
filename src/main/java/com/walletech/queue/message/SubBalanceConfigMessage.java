package com.walletech.queue.message;

import com.walletech.po.SubDeviceBalanceConfig;

import java.util.List;

public class SubBalanceConfigMessage extends Message {

    private List<SubDeviceBalanceConfig> configs;

    public List<SubDeviceBalanceConfig> getConfigs() {
        return configs;
    }

    public void setConfigs(List<SubDeviceBalanceConfig> configs) {
        this.configs = configs;
    }
}
