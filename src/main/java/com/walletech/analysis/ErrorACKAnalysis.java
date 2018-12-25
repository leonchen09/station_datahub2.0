package com.walletech.analysis;

import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ErrorACKAnalysis implements Analysis {
    private static final Logger logger = LoggerFactory.getLogger(ErrorACKAnalysis.class);
    private static final int DATA_OFFSET = 10;
    @Override
    public void doAnalysis(byte[] data, String gprsId, ChannelHandlerContext ctx) {
        String errorMsg;
        switch (data[DATA_OFFSET]){
            case (byte) 0x01 :
                errorMsg = "帧头错误";
                break;
            case (byte) 0x02:
                errorMsg = "帧长度错误";
                break;
            case (byte) 0x03:
                errorMsg = "BCC校验错误";
                break;
            case (byte) 0x04:
                errorMsg = "CRC校验错误";
                break;
            default:
                errorMsg = "未知错误";
        }
        logger.warn("[{}] 错误回复帧，错误原因:"+errorMsg,gprsId);
    }
}
