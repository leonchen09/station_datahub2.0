package com.walletech.queue.message;

import com.walletech.po.ModifyCapacityInfo;

public class ModifyCapacityACKMessage extends Message {
    private ModifyCapacityInfo modifyCapacityInfo;

    public ModifyCapacityInfo getModifyCapacityInfo() {
        return modifyCapacityInfo;
    }

    public void setModifyCapacityInfo(ModifyCapacityInfo modifyCapacityInfo) {
        this.modifyCapacityInfo = modifyCapacityInfo;
    }
}
