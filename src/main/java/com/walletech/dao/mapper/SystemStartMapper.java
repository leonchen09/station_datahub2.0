package com.walletech.dao.mapper;

import java.util.List;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface SystemStartMapper {

    void updateGprsConnectionInfo(Integer serverNum);

    void updateDeviceInfo(Integer serverNum);

    List<Map> selectAllStationId();

}
