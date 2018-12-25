package com.walletech.dao.mapper;

import com.walletech.po.PulseDischargeInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface PulseDischargeInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insertPulseDischargeInfo(PulseDischargeInfo record);

    PulseDischargeInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PulseDischargeInfo record);

}