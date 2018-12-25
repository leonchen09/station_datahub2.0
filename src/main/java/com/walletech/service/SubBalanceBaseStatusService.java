package com.walletech.service;


import com.alibaba.fastjson.JSON;
import com.walletech.dao.mapper.PackDataBalanceInfoLatestMapper;
import com.walletech.dao.mapper.PackDataBalanceInfoMapper;
import com.walletech.po.PackDataBalanceInfo;
import com.walletech.queue.exception.QueueException;
import com.walletech.queue.message.SubBalanceBaseStatusMessage;
import com.walletech.util.ProtocolUtil;
import com.walletech.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubBalanceBaseStatusService {

    private static final String REDIS_PACK_BALANCE_LASTEST_KEY = "srv_station_packDataBalanceInfoLatest_";

    private static final Logger logger = LoggerFactory.getLogger(SubBalanceBaseStatusService.class);

    @Autowired
    private PackDataBalanceInfoMapper packDataBalanceInfoMapper;
    @Autowired
    private PackDataBalanceInfoLatestMapper packDataBalanceInfoLatestMapper;
//    @Autowired
//    private SubDeviceBalanceConfigMapper subDeviceBalanceConfigMapper;

    public void doService(SubBalanceBaseStatusMessage message) throws QueueException {
        try{
            List<PackDataBalanceInfo> infos = message.getInfos();
            String gprsId = message.getGprsId();
            Integer stationId = ProtocolUtil.getStationId(gprsId);
            for (PackDataBalanceInfo info : infos){
                info.setStationId(stationId);
            }
            packDataBalanceInfoMapper.insertPackDataBalanceInfos(infos);
            //减缓数据库写入压力,暂时取消写入sub_device_config表
//            subDeviceBalanceConfigMapper.updateBalanceStatus(message.getConfigs());

//            int count = packDataBalanceInfoLatestMapper.updatePackDataBalanceLatestInfos(infos);
//            if (count == 0) packDataBalanceInfoLatestMapper.insertPackDataBalanceLatestInfos(infos);

            byte[] balanceInfos = JSON.toJSONString(infos).getBytes();
            RedisUtil.setBytes(REDIS_PACK_BALANCE_LASTEST_KEY + gprsId, balanceInfos);

        } catch (Exception e){
            throw new QueueException(e.getMessage(), e);
        }
    }

}
