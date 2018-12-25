package com.walletech.queue.message;

import com.walletech.po.ModifyIPPortInfo;

public class ModifyIPPortACKMessage extends Message {
    private ModifyIPPortInfo modifyIPPortInfo;

    public ModifyIPPortInfo getModifyIPPortInfo() {
        return modifyIPPortInfo;
    }

    public void setModifyIPPortInfo(ModifyIPPortInfo modifyIPPortInfo) {
        this.modifyIPPortInfo = modifyIPPortInfo;
    }
}
