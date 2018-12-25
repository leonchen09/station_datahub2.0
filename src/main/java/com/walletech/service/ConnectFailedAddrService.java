package com.walletech.service;

import com.walletech.dao.mapper.ConnectFailedAddrMapper;
import com.walletech.po.ConnectFailedAddr;
import com.walletech.queue.exception.QueueException;
import com.walletech.queue.message.ConnectFailedAddrMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConnectFailedAddrService {
    @Autowired
    private ConnectFailedAddrMapper connectFailedAddrMapper;

    public void doService(ConnectFailedAddrMessage message) throws QueueException {
        try {
            ConnectFailedAddr addr = message.getConnectFailedAddr();
            connectFailedAddrMapper.insertConnectFailedAddr(addr);
        }catch (Exception e){
            throw new QueueException(e.getMessage(), e);
        }
    }

}
