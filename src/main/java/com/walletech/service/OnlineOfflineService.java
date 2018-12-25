package com.walletech.service;

import com.walletech.dao.mapper.GprsConnectionInfoMapper;
import com.walletech.dao.mapper.GprsDeviceInfoMapper;
import com.walletech.po.GprsConnectionInfo;
import com.walletech.po.GprsDeviceInfo;
import com.walletech.queue.exception.QueueException;
import com.walletech.queue.message.OnlineOfflineMessage;
import com.walletech.util.CacheUtil;
import com.walletech.util.ProtocolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OnlineOfflineService {

    private static final Logger logger = LoggerFactory.getLogger(OnlineOfflineService.class);
    @Autowired
    private GprsConnectionInfoMapper gprsConnectionInfoMapper;
    @Autowired
    private GprsDeviceInfoMapper gprsDeviceInfoMapper;
    /**
     * 执行设备上线下操作
     * 上线：插入更新表gprs_connection_list，gprs_device_info 将连接信息存入GPRSMAP
     * 离线：插入更新表gprs_connection_list，gprs_device_info 将连接信息从GPRSMAP中移除
     * @param message
     */
    public void doService(OnlineOfflineMessage message) throws QueueException {
        try {
            GprsConnectionInfo gprsInfo = message.getGprsConnectionInfo();
            String gprsId = gprsInfo.getGprsId();
            GprsDeviceInfo deviceInfo = new GprsDeviceInfo();
            deviceInfo.setGprsId(gprsId);
            deviceInfo.setGprsIdOut(gprsId);
            if (gprsInfo.getLinkStatus()) {
                //设备上线
                gprsConnectionInfoMapper.insertOrUpdateGprsConnectionInfo(gprsInfo);
                deviceInfo.setLinkStatus(true);
                deviceInfo.setDeviceType(ProtocolUtil.getDeviceType(gprsId));
                CacheUtil.getGprsMap().put(gprsInfo.getGprsId(), gprsInfo);
                CacheUtil.getGprsChannelMap().put(gprsInfo.getGprsId(), gprsInfo.getChannel());
                gprsDeviceInfoMapper.insertOrUpdateGprsDeviceInfo(deviceInfo);
            } else {
                //设备离线
                gprsConnectionInfoMapper.updateGprsConnectionInfo(gprsInfo);
                CacheUtil.getGprsMap().remove(gprsInfo.getGprsId());
                CacheUtil.getGprsChannelMap().remove(gprsInfo.getGprsId());
            }
        }catch (Exception e){
            throw new QueueException(e.getMessage(), e);
        }
    }

}
