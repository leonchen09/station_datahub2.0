package com.walletech.service;

import com.walletech.dao.mapper.VersionBalanceInfoMapper;
import com.walletech.po.VersionBalanceInfo;
import com.walletech.queue.exception.QueueException;
import com.walletech.queue.message.VersionBalanceMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VersionBalanceService {

    @Autowired
    private VersionBalanceInfoMapper versionBalanceInfoMapper;


    public void doService(VersionBalanceMessage message) throws QueueException {
        try {
            VersionBalanceInfo info = message.getVersionBalanceInfo();
            versionBalanceInfoMapper.updateVersionBalanceInfo(info);
        } catch (Exception e) {
            throw new QueueException(e.getMessage(), e);
        }
    }
}
