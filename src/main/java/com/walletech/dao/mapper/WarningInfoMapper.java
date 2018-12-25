package com.walletech.dao.mapper;

import com.walletech.po.WarningInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface WarningInfoMapper {

    Integer insertWarningInfo(WarningInfo warningInfo);

}
