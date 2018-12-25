package com.walletech.dao.mapper;

import com.walletech.po.ModifyConnectAddrInfo;

import java.util.List;

public interface ModifyConnectAddrInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ModifyConnectAddrInfo record);

    int insertSelective(ModifyConnectAddrInfo record);

    List<ModifyConnectAddrInfo> pollingModifyConnectAddr(Integer ServerNum);

    void updateModifyConnectAddrInfo(ModifyConnectAddrInfo info);

    void updateModifyConnectAddrInfoACK(ModifyConnectAddrInfo info);
}