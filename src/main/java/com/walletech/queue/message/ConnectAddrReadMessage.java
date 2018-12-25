package com.walletech.queue.message;

import com.walletech.po.ConnectAddrInfo;

public class ConnectAddrReadMessage extends Message {
    private ConnectAddrInfo connectAddrInfo;

    public ConnectAddrInfo getConnectAddrInfo() {
        return connectAddrInfo;
    }

    public void setConnectAddrInfo(ConnectAddrInfo connectAddrInfo) {
        this.connectAddrInfo = connectAddrInfo;
    }
}
