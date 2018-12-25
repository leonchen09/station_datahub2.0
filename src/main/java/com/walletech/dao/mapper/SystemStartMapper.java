package com.walletech.dao.mapper;

import org.springframework.stereotype.Repository;

@Repository
public interface SystemStartMapper {

    void updateGprsConnectionInfo(Integer serverNum);

    void updateDeviceInfo(Integer serverNum);

}
