package com.walletech.dao.mapper;

import com.walletech.po.VersionBalanceInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface VersionBalanceInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(VersionBalanceInfo record);

    int insertSelective(VersionBalanceInfo record);

    VersionBalanceInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VersionBalanceInfo record);

    void updateVersionBalanceInfo(VersionBalanceInfo info);
}