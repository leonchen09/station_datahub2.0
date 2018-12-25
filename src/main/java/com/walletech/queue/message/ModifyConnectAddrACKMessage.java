package com.walletech.queue.message;

import com.walletech.po.ModifyConnectAddrInfo;

public class ModifyConnectAddrACKMessage extends Message {
    ModifyConnectAddrInfo modifyConnectAddrInfo;

    public ModifyConnectAddrInfo getModifyConnectAddrInfo() {
        return modifyConnectAddrInfo;
    }

    public void setModifyConnectAddrInfo(ModifyConnectAddrInfo modifyConnectAddrInfo) {
        this.modifyConnectAddrInfo = modifyConnectAddrInfo;
    }
}
