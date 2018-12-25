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
public interface PackDataCellInfoMapper {

    Integer insertCellInfo(@Param("list") List<PackDataCellInfo> packDataCellInfo);

}
