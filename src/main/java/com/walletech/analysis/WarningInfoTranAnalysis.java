package com.walletech.analysis;


import com.walletech.po.WarningInfo;
import com.walletech.queue.Sender;
import com.walletech.queue.message.WarningInfoMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 告警信息处理类
 */
@Component
public class WarningInfoTranAnalysis implements Analysis {

    private static final Logger logger = LoggerFactory.getLogger(WarningInfoTranAnalysis.class);

    private static final int DATA_OFFSET = 10;

    private static final int DATA_END_OFFSET = 1;

    @Autowired
    private Sender sender ;

    /**
     * 解析告警数据
     * @param data
     * @param gprsId
     */
    @Override
    public void doAnalysis(byte[] data, String gprsId , ChannelHandlerContext ctx) {
        if (data.length <= DATA_OFFSET+DATA_END_OFFSET){
            logger.warn("设备[{}]告警数据长度异常,[{}]",gprsId,StringUtil.toHexString(data));
            return;
        }
        Date time = new Date();
        logger.debug("开始解析设备[{}]告警信息",gprsId);

        WarningInfo warningInfo = new WarningInfo();
        warningInfo = analysisWarnType(data,warningInfo);
        warningInfo.setRcvTime(time);
        warningInfo.setGprsId(gprsId);

        WarningInfoMessage message = new WarningInfoMessage();
        message.setType(DataType.WARNING_INFO_TYPE);
        message.setWarningInfo(warningInfo);

        sender.saveMessage(message);
        logger.debug("设备[{}]告警信息解析成功",gprsId);
    }

    /**
     * 解析告警类型
     * @param data
     * @param warningInfo
     * @return
     */
    private WarningInfo analysisWarnType(byte[] data, WarningInfo warningInfo){
        //获取warning type 去掉约定消息头尾
        byte[] warningType = new byte[data.length-DATA_OFFSET-DATA_END_OFFSET];
        System.arraycopy(data,DATA_OFFSET,warningType,0,data.length-DATA_OFFSET-DATA_END_OFFSET);

        for (byte info : warningType){
            switch (info){
                case 0x01:
                    warningInfo.setCellTemHigh((byte) 1);
                    break;
                case 0x02:
                    warningInfo.setEnvTemHigh((byte) 1);
                    break;
                case 0x03:
                    warningInfo.setCellTemLow((byte) 1);
                    break;
                case 0x04:
                    warningInfo.setEnvTemLow((byte) 1);
                    break;
                case 0x05:
                    warningInfo.setGenVolHigh((byte) 1);
                    break;
                case 0x06:
                    warningInfo.setGenVolLow((byte) 1);
                    break;
                case 0x07:
                    warningInfo.setLossElectricity((byte) 1);
                    break;
                case 0x08:
                    warningInfo.setSocLow((byte) 1);
                    break;
                case 0x09:
                    warningInfo.setAbnormalCurrent(1);
                    break;
                case 0x0a:
                    warningInfo.setSingleVolHigh(1);
                    break;
                case 0x0b:
                    warningInfo.setSingleVolLow(1);
                    break;
                default:
                    logger.error("wrong warning type : "+ info);
            }
        }
        return warningInfo;
    }

}
