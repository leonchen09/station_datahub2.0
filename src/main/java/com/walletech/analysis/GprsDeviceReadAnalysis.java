package com.walletech.analysis;

import com.walletech.po.GprsDeviceInfo;
import com.walletech.queue.Sender;
import com.walletech.queue.message.GprsDeviceReadMessage;
import com.walletech.util.ByteExchangeUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 参数读取帧处理类
 */
@Component
public class GprsDeviceReadAnalysis implements Analysis {

    private static final Logger logger = LoggerFactory.getLogger(GprsDeviceReadAnalysis.class);
    private static final int DATA_OFFSET = 10;
    private static final BigDecimal DECIMAL_1000 = new BigDecimal(1000);
    private static final BigDecimal DECIMAL_100 = new BigDecimal(100);
    private static final BigDecimal DECIMAL_10 = new BigDecimal(10);
    @Autowired
    private Sender sender;

    @Override
    public void doAnalysis(byte[] data, String gprsId, ChannelHandlerContext ctx) {
        if (data.length<61){
            logger.warn("[{}]参数读取帧 数据长度错误,[{}]",gprsId,StringUtil.toHexString(data));
            return;
        }
        logger.debug("开始解析[{}]参数读取帧",gprsId);
        int index = DATA_OFFSET;
        Date now = new Date();
        GprsDeviceInfo  deviceInfo = new GprsDeviceInfo();
        deviceInfo.setGprsId(gprsId);
        deviceInfo.setGprsIdOut(gprsId);
        deviceInfo.setRcvTime(now);
        //连接类型 2bit
        deviceInfo.setConnectionType(data[index] & 0xff >> 6);
        //心跳间隔 6bit
        deviceInfo.setHeartbeatInterval((data[index] & 0x3F) * 30);
        index += 1;
        //浮充状态下状态帧传输间隔 2byte
        deviceInfo.setFloatInterval(ByteExchangeUtil.getIntFromBytes(data,index,2));
        index += 2;
        //充电状态下状态帧传输间隔 2byte
        deviceInfo.setChargeInterval(ByteExchangeUtil.getIntFromBytes(data,index,2));
        index += 2;
        //放电判断阈值 2byte
        deviceInfo.setDischargeThreshold(
                new BigDecimal(30000 - ByteExchangeUtil.getIntFromBytes(data,index,2)).divide(DECIMAL_100)
        );
        index += 2;
        //充电判断阈值 2byte
        deviceInfo.setChargeThreshold(
                new BigDecimal(30000 - ByteExchangeUtil.getIntFromBytes(data,index,2)).divide(DECIMAL_100)
        );
        index += 2;
        //电池额定容量 2byte (未使用)
        index += 2;
        //总电压过高告警阈值 2byte
        deviceInfo.setVolHighWarningThreshold(
                new BigDecimal(ByteExchangeUtil.getIntFromBytes(data,index,2)).divide(DECIMAL_100)
        );
        index += 2;
        //总电压过低告警阈值 2byte
        deviceInfo.setVolLowWarningThreshold(
                new BigDecimal(ByteExchangeUtil.getIntFromBytes(data,index,2)).divide(DECIMAL_100)
        );
        index += 2;
        //单体温度过高告警阈值 1byte
        deviceInfo.setTemHighWarningThreshold(ByteExchangeUtil.getIntFromBytes(data,index,1) - 50);
        index += 1;
        //单体温度过低告警阈值 1byte
        deviceInfo.setTemLowWarningThreshold(ByteExchangeUtil.getIntFromBytes(data,index,1) - 50);
        index += 1;
        //SOC过低告警阈值 1byte
        deviceInfo.setSocLowWarningThreshold(ByteExchangeUtil.getIntFromBytes(data,index,1));
        index += 1;
        //程序版本号 7byte (未使用)
        index += 7;
        //放电状态下状态帧传输间隔 2byte
        deviceInfo.setDischargeInterval(ByteExchangeUtil.getIntFromBytes(data,index,2));
        index += 2;
        //电流异常告警上限 2byte
        deviceInfo.setCurrentWarningToplimit(
                new BigDecimal(3000 - ByteExchangeUtil.getIntFromBytes(data,index,2)).multiply(DECIMAL_100)
                        .divide(DECIMAL_1000)
        );
        index += 2;
        //电流异常告警下限 2byte
        deviceInfo.setCurrentWarningLowerlimit(
                new BigDecimal(3000 - ByteExchangeUtil.getIntFromBytes(data,index,2)).multiply(DECIMAL_100)
                        .divide(DECIMAL_1000)
        );
        index += 2;
        //单体过压告警阈值 2byte
        deviceInfo.setSingleHighVolThreshold(
                new BigDecimal(ByteExchangeUtil.getIntFromBytes(data,index,2)).divide(DECIMAL_1000)
        );
        index += 2;
        //单体欠压告警阈值 2byte
        deviceInfo.setSingleLowVolThreshold(
                new BigDecimal(ByteExchangeUtil.getIntFromBytes(data,index,2)).divide(DECIMAL_1000)
        );
        index += 2;
        //总电压过压恢复点 2byte
        deviceInfo.setHighVolRecover(
                new BigDecimal(ByteExchangeUtil.getIntFromBytes(data,index,2)).multiply(DECIMAL_10)
                        .divide(DECIMAL_1000)
        );
        index += 2;
        //总电压欠压恢复点 2byte
        deviceInfo.setLowVolRecover(
                new BigDecimal(ByteExchangeUtil.getIntFromBytes(data,index,2)).multiply(DECIMAL_10)
                        .divide(DECIMAL_1000)
        );
        index += 2;
        //单体过压恢复点 2byte
        deviceInfo.setSingleHighVolRecover(
                new BigDecimal(ByteExchangeUtil.getIntFromBytes(data,index,2)).divide(DECIMAL_1000)
        );
        index += 2;
        //单体欠压恢复点 2byte
        deviceInfo.setSingleLowVolRecover(
                new BigDecimal(ByteExchangeUtil.getIntFromBytes(data,index,2)).divide(DECIMAL_1000)
        );
        index += 2;
        //环境高温告警阈值 1byte
        deviceInfo.setHighSurroundingtemWarningThreshold(ByteExchangeUtil.getIntFromBytes(data,index,1) - 50);
        index += 1;
        //环境低温告警阈值 1byte
        deviceInfo.setLowSurroundingtemWarningThreshold(ByteExchangeUtil.getIntFromBytes(data,index,1) - 50);
        index += 1;
        //环境高温告警恢复点 1byte
        deviceInfo.setHighSurroundingtemWarningRecover(ByteExchangeUtil.getIntFromBytes(data,index,1) - 50);
        index += 1;
        //环境低温告警恢复点 1byte
        deviceInfo.setLowSurroundingtemWarningRecover(ByteExchangeUtil.getIntFromBytes(data,index,1) - 50);
        index += 1;
        //单体高温告警恢复点 1byte
        deviceInfo.setHightemWarningRecover(ByteExchangeUtil.getIntFromBytes(data,index,1) - 50);
        index += 1;
        //单体低温告警恢复点 1byte
        deviceInfo.setLowtemWarningRecover(ByteExchangeUtil.getIntFromBytes(data,index,1) - 50);
        index += 1;
        //电量低告警恢复点 1byte
        deviceInfo.setLowSocRecover(ByteExchangeUtil.getIntFromBytes(data,index,1));

        GprsDeviceReadMessage message = new GprsDeviceReadMessage();
        message.setType(DataType.GPRS_DEVICE_READ);
        message.setGprsDeviceInfo(deviceInfo);
        sender.saveMessage(message);
        logger.info("[{}]参数读取帧 解析完成",gprsId);
    }

}
