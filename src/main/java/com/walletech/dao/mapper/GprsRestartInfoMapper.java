package com.walletech.dao.mapper;

import com.walletech.po.GprsRestartInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GprsRestartInfoMapper {

    void insertGprsRestartInfo(GprsRestartInfo info);

    void updateGprsRestartACK(GprsRestartInfo info);

    List<GprsRestartInfo> pollingGprsRestartInfo(Integer serverNum);

    void updateGprsRestartSendStatus(GprsRestartInfo info);
}