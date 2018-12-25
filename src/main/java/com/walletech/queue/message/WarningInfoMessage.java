package com.walletech.queue.message;

import com.walletech.po.WarningInfo;

public class WarningInfoMessage extends Message {

    private WarningInfo warningInfo;

    public WarningInfo getWarningInfo() {
        return warningInfo;
    }

    public void setWarningInfo(WarningInfo warningInfo) {
        this.warningInfo = warningInfo;
    }
}
