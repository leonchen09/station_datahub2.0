package com.walletech.dao.mapper;


import com.walletech.po.PackDataInfo;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author
 * @version 1.0
 * @description
 * @since
 */
@Repository
public interface PackDataInfoMapper {

    Integer insertInfo(PackDataInfo packDataInfo);

}
