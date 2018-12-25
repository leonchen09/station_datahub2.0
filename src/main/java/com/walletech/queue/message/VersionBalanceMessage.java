package com.walletech.queue.message;

import com.walletech.po.VersionBalanceInfo;

public class VersionBalanceMessage extends Message {
    private VersionBalanceInfo versionBalanceInfo;

    public VersionBalanceInfo getVersionBalanceInfo() {
        return versionBalanceInfo;
    }

    public void setVersionBalanceInfo(VersionBalanceInfo versionBalanceInfo) {
        this.versionBalanceInfo = versionBalanceInfo;
    }
}
