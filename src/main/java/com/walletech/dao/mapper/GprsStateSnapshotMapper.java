package com.walletech.dao.mapper;

import com.walletech.po.GprsStateSnapshot;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GprsStateSnapshotMapper {

    List<GprsStateSnapshot> getGprsState();
}
