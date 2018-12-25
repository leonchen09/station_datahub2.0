package com.walletech.dao.mapper;

import com.walletech.po.ResistanceTypeSendInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResistanceTypeSendInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(ResistanceTypeSendInfo record);

    List<ResistanceTypeSendInfo> pollingResistanceTypeSend(Integer serverNum);

    void updateResistanceTypeSendInfo(ResistanceTypeSendInfo info);

    void updateResistanceTypeSendACK(ResistanceTypeSendInfo info);

}