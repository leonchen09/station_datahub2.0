package com.walletech.dao.mapper;

import com.walletech.po.GprsDeviceReadInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GprsDeviceReadInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GprsDeviceReadInfo record);

    int insertSelective(GprsDeviceReadInfo record);

    List<GprsDeviceReadInfo> pollingGprsDeviceRead(Integer ServerNum);

    int updateByPrimaryKeySelective(GprsDeviceReadInfo record);

    int updateByPrimaryKey(GprsDeviceReadInfo record);
}