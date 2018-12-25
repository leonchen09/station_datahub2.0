package com.walletech.dao.mapper;

import com.walletech.po.ConnectFailedAddr;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectFailedAddrMapper {

    int insertConnectFailedAddr(ConnectFailedAddr addr);

}