package com.walletech.dao.mapper;

import com.walletech.po.ResistanceTypeReadInfo;

import java.util.List;

public interface ResistanceTypeReadInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ResistanceTypeReadInfo record);

    int insertSelective(ResistanceTypeReadInfo info);

    List<ResistanceTypeReadInfo> pollingResistanceTypeRead(Integer serverNum);

    void updateResistanceTypeReadInfo(ResistanceTypeReadInfo info);

    void updateResistanceTypeReceive(ResistanceTypeReadInfo info);
}