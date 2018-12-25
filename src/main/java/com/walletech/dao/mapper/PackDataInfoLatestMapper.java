package com.walletech.dao.mapper;


import com.walletech.po.PackDataInfo;
import org.springframework.stereotype.Repository;


/**
 * @author
 * @version 1.0
 * @description
 * @since
 */
@Repository
public interface PackDataInfoLatestMapper {

    void replaceIntoInfoLatest(PackDataInfo packDataInfo);

    void insertIntoInfoLatest(PackDataInfo packDataInfo);

    Integer updateInfoLatest(PackDataInfo packDataInfo);

    PackDataInfo checkInsertOrUpdate(String gprsId);
}
