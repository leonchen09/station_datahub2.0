package com.walletech.dao.mapper;

import com.walletech.po.WarningInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface WarningInfoLatestMapper {

    Integer replaceIntoWarningInfoLatest(WarningInfo warningInfo);

    Integer updateWarningInfoLatest(WarningInfo warningInfo);

    void insertIntoWarningInfoLatest(WarningInfo warningInfo);

}