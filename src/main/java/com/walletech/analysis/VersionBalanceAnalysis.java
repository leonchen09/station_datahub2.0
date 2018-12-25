package com.walletech.analysis;

import com.walletech.po.VersionBalanceInfo;
import com.walletech.queue.Sender;
import com.walletech.queue.message.VersionBalanceMessage;
import com.walletech.util.ByteExchangeUtil;
import com.walletech.util.ProtocolUtil;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 处理远程读取主从机信息帧
 */
@Component
public class VersionBalanceAnalysis implements Analysis {

    private static final Logger logger = LoggerFactory.getLogger(VersionBalanceAnalysis.class);

    private static final int DATA_OFFSET = 10;
    @Autowired
    private Sender sender;

    @Override
    public void doAnalysis(byte[] data, String gprsId, ChannelHandlerContext ctx) {
        logger.debug("开始解析[{}]远程读取主从机信息帧",gprsId);
        Date now = new Date();
        VersionBalanceInfo info = new VersionBalanceInfo();
        info.setUpdateTime(now);
        info.setGprsId(gprsId);
        info.setPara1((int) data[DATA_OFFSET]);
        info.setPara2((int) data[DATA_OFFSET+1]);
        info.setPara3((int) data[DATA_OFFSET+2]);
        info.setPara4((int) data[DATA_OFFSET+3]);
        info.setPara5((int) data[DATA_OFFSET+4]);
        info.setPara6((int) data[DATA_OFFSET+5] - 10);
        info.setPara7(ByteExchangeUtil.getIntFromBytes(data,DATA_OFFSET+6,2));
        String bluMasterIdStr =
                new String(new byte[]{data[DATA_OFFSET+8],data[DATA_OFFSET+9],data[DATA_OFFSET+10]});
        String bluMasterIdNum = "" + ByteExchangeUtil.getIntFromBytes(data,DATA_OFFSET+11,3);
        String bluMasterId = bluMasterIdStr +
                String.format("%6s", bluMasterIdNum.trim()).replaceAll("\\s", "0");
        info.setBluMasterId(bluMasterId);
        byte[] masterVersion = new byte[7];
        System.arraycopy(data,DATA_OFFSET+14,masterVersion,0,7);
        info.setMasterVersion(ProtocolUtil.getVersionInfo(masterVersion));
        byte[] bluMasterVersion = new byte[7];
        System.arraycopy(data,DATA_OFFSET+21,bluMasterVersion,0,7);
        info.setBluMasterVersion(ProtocolUtil.getVersionInfo(bluMasterVersion));
        VersionBalanceMessage message = new VersionBalanceMessage();
        message.setType(DataType.VERSION_BALANCE_READ);
        message.setVersionBalanceInfo(info);
        sender.saveMessage(message);
        logger.info("[{}]远程读取主从机信息帧 解析成功",gprsId);
    }


}
