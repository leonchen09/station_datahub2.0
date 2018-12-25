package com.walletech.service;

import com.walletech.dao.mapper.SalveVersionInfoMapper;
import com.walletech.po.SalveVersionInfo;
import com.walletech.queue.exception.QueueException;
import com.walletech.queue.message.SalveVersionMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalveVersionService {
    @Autowired
    private SalveVersionInfoMapper salveVersionInfoMapper;

    public void doService(SalveVersionMessage message) throws QueueException {
        try {
            List<SalveVersionInfo> infos = message.getSalveVersionInfos();
            salveVersionInfoMapper.replaceSalveVersionInfos(infos);
        } catch (Exception e) {
            throw new QueueException(e.getMessage(), e);
        }
    }
}
