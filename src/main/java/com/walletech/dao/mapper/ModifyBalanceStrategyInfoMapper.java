package com.walletech.dao.mapper;

import com.walletech.po.ModifyBalanceStrategyInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModifyBalanceStrategyInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ModifyBalanceStrategyInfo record);

    int insertSelective(ModifyBalanceStrategyInfo record);

    List<ModifyBalanceStrategyInfo> pollingModifyBalanceStrategy(Integer serverNum);

    void updateModifyBalanceStrategyInfo(ModifyBalanceStrategyInfo info);

    int updateByPrimaryKey(ModifyBalanceStrategyInfo record);

    ModifyBalanceStrategyInfo checkTimeOut(ModifyBalanceStrategyInfo info);
}