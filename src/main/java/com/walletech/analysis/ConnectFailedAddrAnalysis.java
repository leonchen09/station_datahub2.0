package com.walletech.analysis;

import com.walletech.po.ConnectFailedAddr;
import com.walletech.queue.Sender;
import com.walletech.queue.message.ConnectFailedAddrMessage;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ConnectFailedAddrAnalysis implements Analysis {

    private static final Logger logger = LoggerFactory.getLogger(ConnectFailedAddrAnalysis.class);
    @Autowired
    private Sender sender;
    private static final int DATA_OFFSET = 10;

    @Override
    public void doAnalysis(byte[] data, String gprsId, ChannelHandlerContext ctx) {
        ConnectFailedAddr connectFailedAddr = new ConnectFailedAddr();
        connectFailedAddr.setRcvTiem(new Date());
        connectFailedAddr.setGprsId(gprsId);
        int addressType = (int) data[DATA_OFFSET];
        connectFailedAddr.setAddressType(addressType);
        byte[] address = new byte[data.length-DATA_OFFSET-2];
        System.arraycopy(data,DATA_OFFSET+1,address,0,address.length);
        String addr = new String(address);
        String errorAddr = addr.substring(0, addr.length() - 1);
        connectFailedAddr.setErrorAddress(errorAddr);
        ConnectFailedAddrMessage message = new ConnectFailedAddrMessage();
        message.setType(DataType.ERROR_CONNECT_ADDR);
        message.setConnectFailedAddr(connectFailedAddr);
        sender.saveMessage(message);
        logger.info("[{}]错误连接地址解析完毕，地址：[{}]",gprsId,errorAddr);
    }
}
