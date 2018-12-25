package com.walletech.dao.mapper;

import com.walletech.po.PackDataCellInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @description
 * @since
 */
@Repository
public interface PackDataCellInfoLatestMapper {

    void replaceIntoCellInfoLatest(@Param("list") List<PackDataCellInfo> packDataCellInfo);

    void insertCellInfoLatest(@Param("list") List<PackDataCellInfo> packDataCellInfo);

    void updateCellInfoLatest(@Param("list") List<PackDataCellInfo> packDataCellInfo);

}
