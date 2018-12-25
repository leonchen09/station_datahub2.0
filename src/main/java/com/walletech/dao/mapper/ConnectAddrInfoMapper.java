package com.walletech.dao.mapper;

import com.walletech.po.ConnectAddrInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnectAddrInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ConnectAddrInfo record);

    int replaceConnectAddrInfo(ConnectAddrInfo info);

    List<ConnectAddrInfo> pollingConnectAddrInfoRead(Integer serverNum);

    int updateConnectAddrInfo(ConnectAddrInfo info);

    int updateByPrimaryKey(ConnectAddrInfo record);
}