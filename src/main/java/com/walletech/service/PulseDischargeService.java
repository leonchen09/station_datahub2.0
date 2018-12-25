package com.walletech.service;

import com.walletech.dao.mapper.PulseDischargeCMDMapper;
import com.walletech.dao.mapper.PulseDischargeInfoMapper;
import com.walletech.po.PulseCMDInfo;
import com.walletech.po.PulseDischargeInfo;
import com.walletech.queue.exception.QueueException;
import com.walletech.queue.message.PulseDischargeMessage;
import com.walletech.util.CacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PulseDischargeService {
    @Autowired
    private PulseDischargeInfoMapper pulseDischargeInfoMapper;
    @Autowired
    private PulseDischargeCMDMapper pulseDischargeCMDMapper;

    public void doService(PulseDischargeMessage message) throws QueueException {
        try {
            PulseDischargeInfo pulseDischargeInfo = message.getPulseDischargeInfo();
            PulseCMDInfo pulseCMDInfo = message.getPulseCMDInfo();
            String gprsId = pulseCMDInfo.getGprsId();
            pulseDischargeInfoMapper.insertPulseDischargeInfo(pulseDischargeInfo);
            pulseDischargeCMDMapper.updatePulseCMDStatus(pulseCMDInfo);
            CacheUtil.removePulseCMDInfo(gprsId);
            CacheUtil.removeSubPkgInfo(gprsId);
        }catch (Exception e){
            throw new QueueException(e.getMessage(), e);
        }
    }
}
