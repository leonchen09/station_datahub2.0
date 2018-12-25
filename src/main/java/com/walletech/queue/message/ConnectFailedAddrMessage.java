package com.walletech.queue.message;

import com.walletech.po.ConnectFailedAddr;

public class ConnectFailedAddrMessage extends Message {
    private ConnectFailedAddr connectFailedAddr;

    public ConnectFailedAddr getConnectFailedAddr() {
        return connectFailedAddr;
    }

    public void setConnectFailedAddr(ConnectFailedAddr connectFailedAddr) {
        this.connectFailedAddr = connectFailedAddr;
    }
}
