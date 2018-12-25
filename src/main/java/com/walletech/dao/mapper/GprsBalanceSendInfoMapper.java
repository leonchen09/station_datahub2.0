package com.walletech.dao.mapper;

import com.walletech.po.GprsBalanceSendInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GprsBalanceSendInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GprsBalanceSendInfo record);

    int insertSelective(GprsBalanceSendInfo record);

    List<GprsBalanceSendInfo> pollingBalanceSend(Integer serverNum);

    void updateGprsBalanceSendInfo(GprsBalanceSendInfo info);

    int updateByPrimaryKey(GprsBalanceSendInfo record);

    GprsBalanceSendInfo checkTimeOut(GprsBalanceSendInfo info);
}