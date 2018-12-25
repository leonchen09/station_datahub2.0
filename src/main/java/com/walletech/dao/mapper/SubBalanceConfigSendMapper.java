package com.walletech.dao.mapper;

import com.walletech.po.SubBalanceConfigSend;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SubBalanceConfigSendMapper {

    List<SubBalanceConfigSend> pollingSubBalanceConfigSend(Integer serverNum);

    SubBalanceConfigSend checkTimeOut(SubBalanceConfigSend record);

    int updateByPrimaryKey(SubBalanceConfigSend record);
}