package com.walletech.dao.mapper;

import com.walletech.po.PulseCMDInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PulseDischargeCMDMapper {
    /**
     * 轮询放电命令
     * @param
     * @return
     */
    List<PulseCMDInfo> pollingPulseCMD(Integer serverNum);
    /**
     * 更新放电命令状态
     */
    void updatePulseCMDStatus(PulseCMDInfo pulseCMDInfo);
    /**
     * 超时确认
     */
    PulseCMDInfo checkTimeOut(PulseCMDInfo pulseCMDInfo);
}
