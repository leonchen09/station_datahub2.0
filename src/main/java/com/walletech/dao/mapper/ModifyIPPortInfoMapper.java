package com.walletech.dao.mapper;

import com.walletech.po.ModifyIPPortInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModifyIPPortInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ModifyIPPortInfo record);

    int insertSelective(ModifyIPPortInfo record);

    List<ModifyIPPortInfo> pollingModifyIPPort(Integer serverNum);

    void updateModifyIPPortInfo(ModifyIPPortInfo info);

    void updateModifyIPPortInfoACK(ModifyIPPortInfo info);
}