package com.walletech.dao.mapper;

import com.walletech.po.ModifyCapacityInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModifyCapacityInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ModifyCapacityInfo record);

    int insertSelective(ModifyCapacityInfo record);

    List<ModifyCapacityInfo> pollingModifyCapacity(Integer serverNum);

    int updateModifyCapacityInfo(ModifyCapacityInfo info);

    int updateByPrimaryKey(ModifyCapacityInfo record);

    ModifyCapacityInfo checkTimeOut(ModifyCapacityInfo info);
}