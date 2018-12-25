package com.walletech.task;


import com.walletech.po.GprsStateSnapshot;
import com.walletech.service.GprsStateSnapshotService;
import com.walletech.util.CacheUtil;
import com.walletech.util.RedisUtil;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 设备状态快照任务类
 */
@Component
public class GprsStateSnapshotTask {

    private static final Logger logger = LoggerFactory.getLogger(GprsStateSnapshotTask.class);

    private HashedWheelTimer timer = new HashedWheelTimer();

    @Autowired
    private GprsStateSnapshotService gprsStateSnapshotService;

    public void start(){
        TimerTask task = new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                addTask(this);
                List<GprsStateSnapshot> gprsStateSnapshots = gprsStateSnapshotService.getGprsState();
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
        };
        addTask(task);
    }

    private void addTask(TimerTask task){
        timer.newTimeout(task,1,TimeUnit.MINUTES);
    }
}
