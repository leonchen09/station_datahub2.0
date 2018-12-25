package com.walletech.dao.mapper;

import com.walletech.po.GprsConnectionInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GprsConnectionInfoMapper {

    int deleteByPrimaryKey(Integer id);

    int insertOrUpdateGprsConnectionInfo(GprsConnectionInfo record);

    GprsConnectionInfo getGprsConnectionInfoByGprsId(String gprsId);

    int updateGprsConnectionInfo(GprsConnectionInfo record);

}