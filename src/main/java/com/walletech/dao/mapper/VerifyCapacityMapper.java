package com.walletech.dao.mapper;

import com.walletech.po.VerifyCapacity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VerifyCapacityMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(VerifyCapacity record);

    int insertSelective(VerifyCapacity record);

    List<VerifyCapacity> pollingVerifyCapacity(Integer serverNum);

    int updateVerifyCapacity(VerifyCapacity info);

    int updateByPrimaryKey(VerifyCapacity record);

    VerifyCapacity checkTimeOut(VerifyCapacity info);
}