package com.walletech.queue.message;

import com.walletech.po.ResistanceTypeReadInfo;

public class ResistanceTypeReadMessage extends Message {
    private ResistanceTypeReadInfo resistanceTypeReadInfo;

    public ResistanceTypeReadInfo getResistanceTypeReadInfo() {
        return resistanceTypeReadInfo;
    }

    public void setResistanceTypeReadInfo(ResistanceTypeReadInfo resistanceTypeReadInfo) {
        this.resistanceTypeReadInfo = resistanceTypeReadInfo;
    }
}
