package com.walletech.dao.mapper;

import com.walletech.po.GprsDeviceSendInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GprsDeviceSendInfoMapper {
    int deleteByPrimaryKey(Integer id);

    List<GprsDeviceSendInfo> pollingGprsDeviceSend(Integer serverNum);

    void updateByPrimaryKey(GprsDeviceSendInfo gprsDeviceSendInfo);

    void updateSuccessByGprsId(String gprsId);

    void updateFailedByGprsId(String gprsId);

    void sendMsgTimeOut(Integer id);
}