package com.walletech.service;

import com.walletech.dao.mapper.GprsStateSnapshotMapper;
import com.walletech.dao.mapper.SystemStartMapper;
import com.walletech.po.GprsStateSnapshot;
import com.walletech.util.CacheUtil;
import com.walletech.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;

@Service
public class SystemStartService {

    private static final Logger logger = LoggerFactory.getLogger(SystemStartService.class);

    @Autowired
    private SystemStartMapper systemStartMapper;
    @Value("${server.num}")
    private Integer serverNum;
    @Autowired
    private GprsStateSnapshotMapper gprsStateSnapshotMapper;

    public void systemStart(){
        systemStartMapper.updateGprsConnectionInfo(serverNum);
        systemStartMapper.updateDeviceInfo(serverNum);
        List<GprsStateSnapshot> gprsStateSnapshots = gprsStateSnapshotMapper.getGprsState();
        HashMap<String,GprsStateSnapshot> gprsMap = new HashMap<>();
        HashMap<Integer,GprsStateSnapshot> stationMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(gprsStateSnapshots)){
            for (GprsStateSnapshot gprsState : gprsStateSnapshots){
                CacheUtil.putGprsStateSnapshotByGprsId(gprsState.getGprsId(),gprsState);
                CacheUtil.putGprsStateSnapshotByStationId(gprsState.getStationId(),gprsState);
                gprsMap.put(gprsState.getGprsId(),gprsState);
                stationMap.put(gprsState.getStationId(),gprsState);
            }
        }
        RedisUtil.set("gateway_station_state_gprsId",gprsMap);
        RedisUtil.set("gateway_station_state_stationId",stationMap);
        RedisUtil.unbind();
        logger.info("保存设备在线状况快照完成，[{}]条记录",gprsMap.size());
    }
}
