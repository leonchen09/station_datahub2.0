package com.walletech.queue.message;

import com.walletech.po.SalveVersionInfo;

import java.util.List;

public class SalveVersionMessage extends Message {
    private List<SalveVersionInfo> salveVersionInfos ;

    public List<SalveVersionInfo> getSalveVersionInfos() {
        return salveVersionInfos;
    }

    public void setSalveVersionInfos(List<SalveVersionInfo> salveVersionInfos) {
        this.salveVersionInfos = salveVersionInfos;
    }
}
