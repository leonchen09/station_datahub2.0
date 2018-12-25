package com.walletech.service;

import com.walletech.dao.mapper.GprsConnectionInfoMapper;
import com.walletech.po.GprsConnectionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GprsConnectionService {

    private static final Logger logger = LoggerFactory.getLogger(GprsConnectionService.class);
    @Autowired
    private GprsConnectionInfoMapper gprsConnectionInfoMapper;

    public GprsConnectionInfo getGprsConnectionInfoByGprsId(String gprsId){
        return gprsConnectionInfoMapper.getGprsConnectionInfoByGprsId(gprsId);
    }

}
