package com.walletech.service;

import com.walletech.dao.mapper.WarningInfoLatestMapper;
import com.walletech.dao.mapper.WarningInfoMapper;
import com.walletech.po.WarningInfo;
import com.walletech.queue.exception.QueueException;
import com.walletech.queue.message.WarningInfoMessage;
import com.walletech.util.ProtocolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WarningInfoService {

    private static final Logger logger = LoggerFactory.getLogger(WarningInfoService.class);

    @Autowired
    private WarningInfoMapper warningInfoMapper;
    @Autowired
    private WarningInfoLatestMapper warningInfoLatestMapper;

    public void doService(WarningInfoMessage message) throws QueueException {
        try {
            WarningInfo warningInfo = message.getWarningInfo();
            //获取stationId
            warningInfo.setStationId(ProtocolUtil.getStationId(warningInfo.getGprsId()));
            //如设备未绑定station,打印日志
            if(warningInfo.getStationId() == null){
                StringBuilder warning = new StringBuilder();
                if (warningInfo.getAbnormalCurrent() == 1) warning.append("异常电流   ");
                if (warningInfo.getCellTemHigh() == 1) warning.append("单体温度过高   ");
                if (warningInfo.getEnvTemHigh() == 1) warning.append("环境温度过高   ");
                if (warningInfo.getCellTemLow() == 1) warning.append("单体温度过低   ");
                if (warningInfo.getEnvTemLow() == 1) warning.append("环境温度过低   ");
                if (warningInfo.getGenVolHigh() == 1) warning.append("总电压过高   ");
                if (warningInfo.getGenVolLow() == 1) warning.append("总电压过低   ");
                if (warningInfo.getLossElectricity() == 1) warning.append("掉电警告   ");
                if (warningInfo.getSocLow() == 1) warning.append("SOC过低   ");
                if (warningInfo.getSingleVolHigh() == 1) warning.append("单体电压过高   ");
                if (warningInfo.getSingleVolLow() == 1) warning.append("单体电压过低   ");
                logger.info("[{}]未绑定station!   告警：[{}]",warningInfo.getGprsId(),warning.toString());
                return;
            }
            warningInfoMapper.insertWarningInfo(warningInfo);
            int count = warningInfoLatestMapper.updateWarningInfoLatest(warningInfo);
            if (count == 0) warningInfoLatestMapper.insertIntoWarningInfoLatest(warningInfo);

        } catch (Exception e) {
            throw new QueueException(e.getMessage(), e);
        }
    }
}
