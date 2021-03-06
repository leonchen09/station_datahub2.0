package com.walletech.dao.mapper;

import com.walletech.po.SubDeviceBalanceConfig;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubDeviceBalanceConfigMapper {

    int replaceSubDeviceBalanceConfigs(List<SubDeviceBalanceConfig> records);

    int insertOrUpdateDeviceStatus(List<SubDeviceBalanceConfig> records);

}