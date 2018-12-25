package com.walletech.service;

import com.walletech.dao.mapper.GprsDeviceInfoMapper;
import com.walletech.dao.mapper.SubDeviceBalanceConfigMapper;
import com.walletech.po.GprsDeviceInfo;
import com.walletech.po.SubDeviceBalanceConfig;
import com.walletech.queue.exception.QueueException;
import com.walletech.queue.message.SubBalanceSupportMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubBalanceSupportService {

    private static final Logger logger = LoggerFactory.getLogger(SubBalanceSupportService.class);

    @Autowired
    private SubDeviceBalanceConfigMapper subDeviceBalanceConfigMapper;
    @Autowired
    private GprsDeviceInfoMapper gprsDeviceInfoMapper;

    /**
     * 处理当前支持的均衡从机信息
     * @param message
     * @throws QueueException
     */
    public void doService(SubBalanceSupportMessage message) throws QueueException {
        try {
            GprsDeviceInfo info = message.getInfo();
            gprsDeviceInfoMapper.insertOrUpdateGprsDeviceInfo(info);
            List<SubDeviceBalanceConfig> configs = message.getConfigs();
            subDeviceBalanceConfigMapper.insertOrUpdateDeviceStatus(configs);
        } catch (Exception e){
            throw new QueueException(e.getMessage(), e);
        }
    }


}
