package com.walletech.dao.mapper;

import com.walletech.po.ModifyGprsIdInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModifyGprsIdInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ModifyGprsIdInfo record);

    int insertSelective(ModifyGprsIdInfo record);

    List<ModifyGprsIdInfo> pollingModifyGprsId(Integer serverNum);

    void updateModifyGprsIdInfo(ModifyGprsIdInfo info);

    int updateByPrimaryKey(ModifyGprsIdInfo record);

    ModifyGprsIdInfo checkTimeOut(ModifyGprsIdInfo info);
}