package com.walletech.analysis;

import com.walletech.po.ConnectAddrInfo;
import com.walletech.queue.Sender;
import com.walletech.queue.message.ConnectAddrReadMessage;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 网络连接地址读取帧处理类
 */
@Component
public class ConnectAddrReadAnalysis implements Analysis {

    private static final Logger logger = LoggerFactory.getLogger(ConnectAddrReadAnalysis.class);
    @Autowired
    private Sender sender;
    private static final int DATA_OFFSET = 10;
    @Override
    public void doAnalysis(byte[] data, String gprsId, ChannelHandlerContext ctx) {
        logger.debug("开始解析[{}]网络连接地址读取帧",gprsId);
        ConnectAddrInfo info = new ConnectAddrInfo();
        info.setGprsId(gprsId);
        info.setSendTime(new Date());
        info.setSendDone(2);
        info.setType1((int) data[DATA_OFFSET]);
        info.setType2((int) data[DATA_OFFSET+1]);
        info.setType3((int) data[DATA_OFFSET+2]);
        info.setType4((int) data[DATA_OFFSET+3]);
        byte[] address = new byte[data.length-15];
        System.arraycopy(data,14,address,0,address.length);
        String[] allAddr = new String(address).split("#");
        info.setAdress1(allAddr[0]);
        info.setAdress2(allAddr[1]);
        info.setAdress3(allAddr[2]);
        info.setAdress4(allAddr[3]);
        ConnectAddrReadMessage message = new ConnectAddrReadMessage();
        message.setConnectAddrInfo(info);
        message.setType(DataType.CONNECT_ADDR_READ);
        sender.saveMessage(message);
        logger.info("[{}]网络连接地址读取帧 解析完成",gprsId);
    }
}
