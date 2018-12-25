package com.walletech.service;

import com.walletech.dao.mapper.GprsStateSnapshotMapper;
import com.walletech.po.GprsStateSnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GprsStateSnapshotService {
    @Autowired
    private GprsStateSnapshotMapper gprsStateSnapshotMapper;

    public List<GprsStateSnapshot> getGprsState(){
        return gprsStateSnapshotMapper.getGprsState();
    }

}
