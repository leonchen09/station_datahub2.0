package com.walletech.service;

import com.alibaba.fastjson.JSON;
import com.walletech.dao.mapper.PackDataCellInfoLatestMapper;
import com.walletech.dao.mapper.PackDataCellInfoMapper;
import com.walletech.dao.mapper.PackDataInfoLatestMapper;
import com.walletech.dao.mapper.PackDataInfoMapper;
import com.walletech.po.PackDataCellInfo;
import com.walletech.po.PackDataInfo;
import com.walletech.queue.exception.QueueException;
import com.walletech.queue.message.DeviceStatusMessage;
import com.walletech.util.ProtocolUtil;
import com.walletech.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @description
 * @since
 */
@Service
public class DeviceStatusService {

    private static final Logger logger = LoggerFactory.getLogger(DeviceStatusService.class);

    @Autowired
    private PackDataCellInfoMapper cellMapper;
    @Autowired
    private PackDataInfoMapper infoMapper;
    @Autowired
    private PackDataInfoLatestMapper packDataInfoLatestMapper;
    @Autowired
    private PackDataCellInfoLatestMapper packDataCellInfoLatestMapper;

    /**
     * 执行设备状态登记
     * 1、检查状态是否第一次登记，若是，则直接更新设备状态最新表pack_data_info_latest和pack_data_cellinfo_latest
     * 2、若状态已存在则更新pack_data_info_latest和pack_data_cellinfo_latest表，
     * 并将历史数据插入pack_data_info和pack_data_cellinfo表
     *
     * @param message
     * @throws QueueException 抛出次异常，Queue捕捉到，执行设定次数的操作
     */
    public void doService(DeviceStatusMessage message) throws QueueException {
        try {
            PackDataInfo packDataInfo = message.getPackDataInfo();
            List<PackDataCellInfo> packDataCellInfoList = message.getPackDataCellInfoList();
            //获取stationId
            String gprsId = packDataInfo.getGprsId();
            Integer stationId = ProtocolUtil.getStationId(gprsId);
            packDataInfo.setStationId(stationId);
            infoMapper.insertInfo(packDataInfo);
            for (PackDataCellInfo cellInfo : packDataCellInfoList) {
                cellInfo.setPackDataInfoId(packDataInfo.getId());
                cellInfo.setStationId(stationId);
            }

            int count = packDataInfoLatestMapper.updateInfoLatest(packDataInfo);
            if (count == 0) packDataInfoLatestMapper.insertIntoInfoLatest(packDataInfo);

            cellMapper.insertCellInfo(packDataCellInfoList);

            byte[] cellInfos = JSON.toJSONString(packDataCellInfoList).getBytes();
            RedisUtil.setBytes("srv_station_packDataCellInfoLatest_"+gprsId,cellInfos);

        } catch (Exception e) {
            throw new QueueException(e.getMessage(), e);
        }
    }
}
