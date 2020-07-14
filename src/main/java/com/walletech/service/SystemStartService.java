package com.walletech.service;

import com.walletech.dao.mapper.GprsStateSnapshotMapper;
import com.walletech.dao.mapper.SystemStartMapper;
import com.walletech.po.GprsStateSnapshot;
import com.walletech.util.CacheUtil;
import com.walletech.util.ProtocolUtil;
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
import java.util.Map;

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
        //刷新所有的station与gprs对应关系
        List<Map> stationids = systemStartMapper.selectAllStationId();
        for(Map station : stationids){
            String gprsid = station.get("gprs_id").toString();
            Integer stationid = Integer.valueOf(station.get("id").toString());
            ProtocolUtil.putStationId(gprsid, stationid);
            logger.info("系统初始化，缓存电池与设备关联关系:{}", gprsid+"--"+stationid);
        }
    }
}
