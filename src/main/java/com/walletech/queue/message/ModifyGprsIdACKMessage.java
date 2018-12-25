package com.walletech.queue.message;

import com.walletech.po.ModifyGprsIdInfo;

public class ModifyGprsIdACKMessage extends Message {
    private ModifyGprsIdInfo modifyGprsIdInfo;

    public ModifyGprsIdInfo getModifyGprsIdInfo() {
        return modifyGprsIdInfo;
    }

    public void setModifyGprsIdInfo(ModifyGprsIdInfo modifyGprsIdInfo) {
        this.modifyGprsIdInfo = modifyGprsIdInfo;
    }
}
