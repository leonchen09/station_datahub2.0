package com.walletech.analysis;

import com.walletech.po.GprsDeviceInfo;
import com.walletech.po.SubDeviceBalanceConfig;
import com.walletech.queue.Sender;
import com.walletech.queue.message.SubBalanceSupportMessage;
import com.walletech.util.ProtocolUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class SubBalanceSupportAnalysis implements Analysis {

    private static final Logger logger = LoggerFactory.getLogger(SubBalanceSupportAnalysis.class);

    @Autowired
    private Sender sender;

    private static final int DATA_LENGTH = 1;
    private static final int DATA_OFFSET = 10;

    @Override
    public void doAnalysis(byte[] data, String gprsId, ChannelHandlerContext ctx) {
        if (data.length < DATA_LENGTH + DATA_OFFSET){
            logger.warn("设备[{}]支持的均衡从机 数据长度异常",gprsId);
            return;
        }

        Date now = new Date();
        int deviceCount = 0;
        List<SubDeviceBalanceConfig> configs = new ArrayList<>();
        String supports = ProtocolUtil.supplementZero(Integer.toBinaryString(data[DATA_OFFSET]),4);
        char[] chars = supports.toCharArray();
        for (int i = 0 ; i < chars.length ; i++ ){
            int flag = 0;
            SubDeviceBalanceConfig config = new SubDeviceBalanceConfig();
            if ( '1' == chars[3 - i]){
                deviceCount++;
                flag = 1;

            }
            config.setGprsId(gprsId);
            config.setBalanceSubdeviceCode((byte) i);
            config.setIsValid(flag);
            config.setRcvTime(now);
            configs.add(config);
        }
        GprsDeviceInfo info = new GprsDeviceInfo();
        info.setGprsId(gprsId);
        info.setSubBalanceCount(deviceCount);
        SubBalanceSupportMessage message = new SubBalanceSupportMessage();
        message.setConfigs(configs);
        message.setInfo(info);
        message.setType(DataType.SUB_BALANCE_SUPPORT);
        sender.saveMessage(message);
        logger.info("解析设备[{}]支持的均衡从机数据完成,有效从机数量：[{}]",gprsId,deviceCount);
    }
}
