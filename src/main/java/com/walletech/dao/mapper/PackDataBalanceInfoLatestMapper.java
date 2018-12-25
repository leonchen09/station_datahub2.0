package com.walletech.dao.mapper;

import com.walletech.po.PackDataBalanceInfo;
import com.walletech.po.PackDataBalanceInfoLatest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackDataBalanceInfoLatestMapper {

    int insertPackDataBalanceLatestInfos(List<PackDataBalanceInfo> records);

    int replacePackDataBalanceLatestInfos(List<PackDataBalanceInfo> records);

    PackDataBalanceInfoLatest checkInsertOrUpdate(String gprsId);

    int updatePackDataBalanceLatestInfos(List<PackDataBalanceInfo> records);

}