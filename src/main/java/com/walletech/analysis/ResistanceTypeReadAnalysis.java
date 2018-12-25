package com.walletech.analysis;

import com.walletech.po.ResistanceTypeReadInfo;
import com.walletech.queue.Sender;
import com.walletech.queue.message.ResistanceTypeReadMessage;
import com.walletech.util.ByteExchangeUtil;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 读取内阻计算参数帧处理类
 */
@Component
public class ResistanceTypeReadAnalysis implements Analysis {

    private static final Logger logger = LoggerFactory.getLogger(ResistanceTypeReadAnalysis.class);
    @Autowired
    private Sender sender;
    private static final int DATA_OFFSET = 10;

    @Override
    public void doAnalysis(byte[] data, String gprsId, ChannelHandlerContext ctx) {
        logger.debug("开始解析[{}]读取内阻计算参数帧",gprsId);
        ResistanceTypeReadInfo info = new ResistanceTypeReadInfo();
        info.setGprsId(gprsId);
        info.setType(data[DATA_OFFSET]);
        byte[] maxVolPoint = new byte[2];
        System.arraycopy(data,DATA_OFFSET+1,maxVolPoint,0,2);
        info.setMaxVolPoint(ByteExchangeUtil.byteToShort(maxVolPoint));
        info.setMinVolPoint((short) data[DATA_OFFSET+3]);
        info.setAveCurPoint((short) data[DATA_OFFSET+4]);
        info.setSendDone((byte) 2);
        ResistanceTypeReadMessage message = new ResistanceTypeReadMessage();
        message.setResistanceTypeReadInfo(info);
        message.setType(DataType.RESISTANCE_TYPE_READ);
        sender.saveMessage(message);
        logger.info("[{}]读取内阻计算参数帧 解析完成",gprsId);
    }
}
