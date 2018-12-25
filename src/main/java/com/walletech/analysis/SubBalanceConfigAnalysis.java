package com.walletech.analysis;

import com.alibaba.fastjson.JSON;
import com.walletech.po.SubDeviceBalanceConfig;
import com.walletech.queue.Sender;
import com.walletech.queue.message.SubBalanceConfigMessage;
import com.walletech.util.ByteExchangeUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class SubBalanceConfigAnalysis implements Analysis {

    private static final Logger logger = LoggerFactory.getLogger(SubBalanceConfigAnalysis.class);

    @Autowired
    private Sender sender;

    private static final int DATA_LENGTH = 34;
    private static final int DATA_OFFSET = 10;
    private static final BigDecimal DECIMAL_10 = new BigDecimal(10);
    private static final BigDecimal DECIMAL_100 = new BigDecimal(100);
    private static final BigDecimal DECIMAL_1000 = new BigDecimal(1000);

    @Override
    public void doAnalysis(byte[] data, String gprsId, ChannelHandlerContext ctx) {
        if (data.length < DATA_LENGTH + DATA_OFFSET){
            logger.warn("设备[{}] 均衡配置参数数据长度异常 : [{}]",gprsId,StringUtil.toHexString(data));
            return;
        }
        Date now = new Date();
        logger.debug("开始解析[{}]均衡配置参数数据",gprsId);
        int num = (data.length - DATA_OFFSET - 1) / DATA_LENGTH;
        List<SubDeviceBalanceConfig> configs = new ArrayList<>();
        for (int i = 0 ; i < num ; i++){
            SubDeviceBalanceConfig config = analysisOneBalanceConfig(data,gprsId,now,i);
            configs.add(config);
            logger.debug("均衡从机参数数据：[{}]",JSON.toJSONString(config));
        }
        SubBalanceConfigMessage message = new SubBalanceConfigMessage();
        message.setConfigs(configs);
        message.setType(DataType.SUB_BALANCE_CONFIG);
        sender.saveMessage(message);
        logger.info("[{}]均衡配置参数数据解析完成",gprsId);
    }

    /**
     * 解析单个均衡从机配置信息
     * @param data
     * @param gprsId
     * @param time
     * @param num
     * @return
     */
    private SubDeviceBalanceConfig analysisOneBalanceConfig(byte[] data,String gprsId,Date time,int num){
        SubDeviceBalanceConfig config = new SubDeviceBalanceConfig();
        config.setGprsId(gprsId);
        config.setRcvTime(time);
        //均衡从机编号
        config.setBalanceSubdeviceCode(data[DATA_OFFSET + DATA_LENGTH*num]);
        //升压模式输出功率
        config.setUpPatternInPower(new BigDecimal(ByteExchangeUtil.getShortFromBytes(data,
                DATA_OFFSET+1 + DATA_LENGTH*num,2)).divide(DECIMAL_10));
        //升压模式输出电压
        config.setUpPatternOutVol(new BigDecimal(ByteExchangeUtil.getIntFromBytes(data,
                DATA_OFFSET+3 + DATA_LENGTH*num,2)).divide(DECIMAL_100));
        //降压模式三段式充电恒流值
        config.setDownPatternOutCur(new BigDecimal(ByteExchangeUtil.getShortFromBytes(data,
                DATA_OFFSET+5 + DATA_LENGTH*num,2)).divide(DECIMAL_100));
        //降压模式三段式浮充电压值
        config.setDownPatternOutVol(new BigDecimal(ByteExchangeUtil.getIntFromBytes(data,
                DATA_OFFSET+7 + DATA_LENGTH*num,2)).divide(DECIMAL_100));
        //升压模式低压侧欠压保护阈值
        config.setUpPatternLowVolThreshold(new BigDecimal(ByteExchangeUtil.getIntFromBytes(data,
                DATA_OFFSET+9 + DATA_LENGTH*num,2)).divide(DECIMAL_100));
        //升压模式升压输出恒压值
        config.setUpPatternHighVolConstantVol(new BigDecimal(ByteExchangeUtil.getIntFromBytes(data,
                DATA_OFFSET+11 + DATA_LENGTH*num,2)).divide(DECIMAL_100));
        //升压模式输出高压保护阈值
        config.setUpPatternHighVolThreshold(new BigDecimal(ByteExchangeUtil.getIntFromBytes(data,
                DATA_OFFSET+13 + DATA_LENGTH*num,2)).divide(DECIMAL_100));
        //降压模式高压侧欠压保护阈值
        config.setDownPatternHighVolLowVolThreshold(new BigDecimal(ByteExchangeUtil.getIntFromBytes(data,
                DATA_OFFSET+15 + DATA_LENGTH*num,2)).divide(DECIMAL_100));
        //降压模式三段式恒压电压值
        config.setDownPatternDownVolConstant(new BigDecimal(ByteExchangeUtil.getIntFromBytes(data,
                DATA_OFFSET+17 + DATA_LENGTH*num,2)).divide(DECIMAL_100));
        //降压模式输出高压保护阈值
        config.setDownPatternHighVolThreshold(new BigDecimal(ByteExchangeUtil.getIntFromBytes(data,
                DATA_OFFSET+19 + DATA_LENGTH*num,2)).divide(DECIMAL_100));
        //升压均衡时长
        config.setUpBalanceTime(new BigDecimal(ByteExchangeUtil.getIntFromBytes(data,
                DATA_OFFSET+21 + DATA_LENGTH*num,2)));
        //单体最低放电电压阀值
        config.setMinDischargeVolThreshold(new BigDecimal(ByteExchangeUtil.getIntFromBytes(data,
                DATA_OFFSET+23 + DATA_LENGTH*num,2)).divide(DECIMAL_1000));
        //低压侧浮充电压阀值
        config.setLowFloatingDischargeVolThreshold(new BigDecimal(ByteExchangeUtil.getIntFromBytes(data,
                DATA_OFFSET+25 + DATA_LENGTH*num,2)).divide(DECIMAL_1000));
        //低压侧浮充电流阀值
        config.setLowFloatingDischargeCurThreshold(new BigDecimal(ByteExchangeUtil.getShortFromBytes(data,
                DATA_OFFSET+27 + DATA_LENGTH*num,2)).divide(DECIMAL_100));
        //降压模式三段式均充转浮充电流
        config.setDownPatternThreeChangeFloatingCur(new BigDecimal(ByteExchangeUtil.getIntFromBytes(data,
                DATA_OFFSET+29 + DATA_LENGTH*num,2)).divide(DECIMAL_100));
        //降压模式三段式浮充电流上限值
        config.setDownPatternThreeFloatingCurUpper(new BigDecimal(ByteExchangeUtil.getIntFromBytes(data,
                DATA_OFFSET+31 + DATA_LENGTH*num,2)).divide(DECIMAL_100));
        //均衡从机连接方式
        config.setBalanceLinkWay((int) data[DATA_OFFSET+33 + DATA_LENGTH*num ]);
        return config;
    }
}
