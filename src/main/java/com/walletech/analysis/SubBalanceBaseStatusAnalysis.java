package com.walletech.analysis;

import com.walletech.po.PackDataBalanceInfo;
import com.walletech.po.SubDeviceBalanceConfig;
import com.walletech.queue.Sender;
import com.walletech.queue.message.SubBalanceBaseStatusMessage;
import com.walletech.util.ByteExchangeUtil;
import com.walletech.util.ProtocolUtil;
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
public class SubBalanceBaseStatusAnalysis implements Analysis {

    private static final Logger logger = LoggerFactory.getLogger(SubBalanceBaseStatusAnalysis.class);

    @Autowired
    private Sender sender;

    private static final int DATA_LENGTH = 23;
    private static final int DATA_OFFSET = 10;
    private static final BigDecimal DECIMAL_10 = new BigDecimal(10);
    private static final BigDecimal DECIMAL_100 = new BigDecimal(100);


    @Override
    public void doAnalysis(byte[] data, String gprsId, ChannelHandlerContext ctx) {
        if (data.length < DATA_LENGTH+DATA_OFFSET){
            logger.warn("设备[{}]均衡从机实时数据长度异常,[{}]",gprsId,StringUtil.toHexString(data));
            return;
        }
        Date now = new Date();
        logger.debug("开始解析[{}]均衡从机实时数据",gprsId);
        List<PackDataBalanceInfo> infos = new ArrayList<>();
        int num = (data.length - DATA_OFFSET) / DATA_LENGTH ;
        for (int i = 0 ; i < num ; i++){
            PackDataBalanceInfo info = analysisOneSubBalanceInfo(data,gprsId,now,i);
            infos.add(info);
        }
        SubBalanceBaseStatusMessage message = new SubBalanceBaseStatusMessage();
        message.setGprsId(gprsId);
        message.setInfos(infos);
        message.setType(DataType.SUB_BALANCE_BASE_STATUS);
        sender.saveMessage(message);
        logger.debug("[{}]均衡从机实时数据解析完毕",gprsId);
    }

    /**
     * 解析单个均衡从机实时数据
     * @param data
     * @param gprsId
     * @param time
     * @param num
     * @return
     */
    private PackDataBalanceInfo analysisOneSubBalanceInfo(byte[] data,String gprsId,Date time,int num){
        PackDataBalanceInfo info = new PackDataBalanceInfo();
        info.setRcvTime(time);
        info.setGprsId(gprsId);
        info.setBalanceSubdeviceCode(data[DATA_OFFSET + DATA_LENGTH*num]);
        //低压侧电压
        info.setLowVol(new BigDecimal(ByteExchangeUtil.getIntFromBytes(data,
                DATA_OFFSET+1 + DATA_LENGTH*num,2))
                .divide(DECIMAL_100));
        //高压侧电压
        info.setHighVol(new BigDecimal(ByteExchangeUtil.getIntFromBytes(data,
                DATA_OFFSET+3 + DATA_LENGTH*num,2))
                .divide(DECIMAL_100));
        //低压侧电流
        info.setLowCur(new BigDecimal(ByteExchangeUtil.getShortFromBytes(data,
                DATA_OFFSET+5 + DATA_LENGTH*num,2))
                .divide(DECIMAL_100));
        //高压侧电流
        info.setHighCur(new BigDecimal(ByteExchangeUtil.getShortFromBytes(data,
                DATA_OFFSET+7 + DATA_LENGTH*num,2))
                .divide(DECIMAL_100));
        //低压侧功率
        info.setLowPower(new BigDecimal(ByteExchangeUtil.getShortFromBytes(data,
                DATA_OFFSET+9 + DATA_LENGTH*num,2))
                .divide(DECIMAL_10));
        //高压侧功率
        info.setHighPower(new BigDecimal(ByteExchangeUtil.getShortFromBytes(data,
                DATA_OFFSET+11 + DATA_LENGTH*num,2))
                .divide(DECIMAL_10));
        //功率器件温度
        info.setPowerTem(new BigDecimal(ByteExchangeUtil.getShortFromBytes(data,
                DATA_OFFSET+13 + DATA_LENGTH*num,2))
                .divide(DECIMAL_10));
        //当前工作模式
        info.setCurrentPattern(getSubBalanceWorkPattern(data[DATA_OFFSET+15 + DATA_LENGTH*num]));
        //升压均衡状态
        info.setUpBalance(getUpSubBalanceStatus(data[DATA_OFFSET+15 + DATA_LENGTH*num]));
        //降压均衡状态
        info.setDownBalance(getDownSubBalanceStatus(data[DATA_OFFSET+15 + DATA_LENGTH*num]));
        //当前工作状态
        info.setCurrentStatus((int)data[DATA_OFFSET+16 + DATA_LENGTH*num]);
        //告警状态
        byte[] warnings = new byte[2];
        System.arraycopy(data,DATA_OFFSET+17 + DATA_LENGTH*num,warnings,0,2);
        setWarningInfo(info,warnings);
        return info;
    }

    /**
     * 获取工作模式
     * @param data
     * @return
     */
    private Integer getSubBalanceWorkPattern(byte data){
        String pattern = ProtocolUtil.supplementZero(Integer.toBinaryString((int) data),4).substring(2,4);
        return Integer.valueOf(pattern,2);
    }

    /**
     * 获取升压均衡状态
     * @param data
     * @return
     */
    private Integer getUpSubBalanceStatus(byte data){
        String pattern = ProtocolUtil.supplementZero(Integer.toBinaryString((int) data),3).substring(0,1);
        return Integer.valueOf(pattern);
    }

    /**
     * 获取降压均衡状态
     * @param data
     * @return
     */
    private Integer getDownSubBalanceStatus(byte data){
        String pattern = ProtocolUtil.supplementZero(Integer.toBinaryString((int) data),4).substring(0,1);
        return Integer.valueOf(pattern);
    }

    /**
     * 解析告警数据
     * @param info
     * @param data
     * @return
     */
    private void setWarningInfo(PackDataBalanceInfo info,byte[] data){
        String warning = ProtocolUtil.supplementZero(Integer.toBinaryString(ByteExchangeUtil.byteArraytoInt(data)),6);
        char[] warnings = warning.toCharArray();
        info.setOvercurrentWarn(Integer.valueOf(warnings[5]+""));
        info.setUpHighVolWarn(Integer.valueOf(warnings[4]+""));
        info.setUpLowVolWarn(Integer.valueOf(warnings[3]+""));
        info.setDownHighVolWarn(Integer.valueOf(warnings[2]+""));
        info.setDownLowVolWarn(Integer.valueOf(warnings[1]+""));
        info.setOvercurrentTem(Integer.valueOf(warnings[0]+""));
    }

}
