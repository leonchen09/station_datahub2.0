package com.walletech.service;

import com.walletech.dao.mapper.SubDeviceBalanceConfigMapper;
import com.walletech.queue.exception.QueueException;
import com.walletech.queue.message.SubBalanceConfigMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubBalanceConfigReadService {

    private static final Logger logger = LoggerFactory.getLogger(SubBalanceConfigReadService.class);

    @Autowired
    private SubDeviceBalanceConfigMapper subDeviceBalanceConfigMapper;

    public void doService(SubBalanceConfigMessage message) throws QueueException {
        try{
            subDeviceBalanceConfigMapper.replaceSubDeviceBalanceConfigs(message.getConfigs());
        } catch (Exception e){
            throw new QueueException(e.getMessage(), e);
        }
    }

}
