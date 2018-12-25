package com.walletech.dao.mapper;

import com.walletech.po.GprsDeviceInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface GprsDeviceInfoMapper {
    int deleteByPrimaryKey(Integer id);

    void insertOrUpdateGprsDeviceInfo (GprsDeviceInfo gprsDeviceInfo);

    GprsDeviceInfo selectByPrimaryKey(Integer id);

}