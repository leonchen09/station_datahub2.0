package com.walletech.dao.mapper;

import com.walletech.po.SalveVersionInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalveVersionInfoMapper {

    int insert(SalveVersionInfo record);

    int replaceSalveVersionInfos(List<SalveVersionInfo> infos);
}