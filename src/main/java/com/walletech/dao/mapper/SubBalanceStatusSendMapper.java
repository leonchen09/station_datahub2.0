package com.walletech.dao.mapper;

import com.walletech.po.SubBalanceStatusSend;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubBalanceStatusSendMapper {

    List<SubBalanceStatusSend> pollingSubBalanceStatusSend(Integer serverNum);

    int updateSubBalanceStatusSend(SubBalanceStatusSend record);

    SubBalanceStatusSend checkTimeOut(SubBalanceStatusSend record);
}