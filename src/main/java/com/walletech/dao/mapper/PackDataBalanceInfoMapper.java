package com.walletech.dao.mapper;

import com.walletech.po.PackDataBalanceInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackDataBalanceInfoMapper {

    int insertPackDataBalanceInfos(List<PackDataBalanceInfo> records);

}