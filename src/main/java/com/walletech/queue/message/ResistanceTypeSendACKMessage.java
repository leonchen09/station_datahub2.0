package com.walletech.queue.message;

import com.walletech.po.ResistanceTypeSendInfo;

public class ResistanceTypeSendACKMessage extends Message {
    private ResistanceTypeSendInfo resistanceTypeSendInfo;

    public ResistanceTypeSendInfo getResistanceTypeSendInfo() {
        return resistanceTypeSendInfo;
    }

    public void setResistanceTypeSendInfo(ResistanceTypeSendInfo resistanceTypeSendInfo) {
        this.resistanceTypeSendInfo = resistanceTypeSendInfo;
    }
}
