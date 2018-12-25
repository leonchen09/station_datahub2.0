package com.walletech.analysis;

import com.walletech.bo.SubpackageInfo;
import com.walletech.util.ByteExchangeUtil;
import com.walletech.util.CacheUtil;
import com.walletech.util.ProtocolUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 分包请求处理类
 */
@Component
public class SubpkgRequestAnalysis implements Analysis {

    private static final Logger logger = LoggerFactory.getLogger(SubpkgRequestAnalysis.class);
    private static final int DATA_OFFSET = 10;
    private static final int END_OFFSET = 1;
    private static final int DATA_LENGTH = 7;

    @Override
    public void doAnalysis(byte[] data, String gprsId, ChannelHandlerContext ctx) {
        ByteBuf buf = ctx.alloc().directBuffer();
        if (data.length < DATA_OFFSET + END_OFFSET + DATA_LENGTH) {
            logger.warn("设备[{}]分包请求数据长度不足！", gprsId);
            return;
        }
        byte[] response = getResponse();
        //分包数据类型
        String dataType;
        //获取分包发送的数据帧类型 如数据类型不符，禁止发送
        switch (data[DATA_OFFSET]) {
            case (byte) 0x15:
                dataType = DataType.PULSE_DISCHARGE_TYPE;
                break;
            default:
                logger.info("设备[{}]分包请求数据类型不符,数据类型为[{}] ", gprsId, data[DATA_OFFSET]);
                response[5] = (byte) 0x02;
                response = ProtocolUtil.beforeSend(response);
                buf.writeBytes(response);
                ctx.writeAndFlush(buf);
                return;
        }
        //获取请求消息中数据长度
        byte[] dataLength = new byte[4];
        System.arraycopy(data, DATA_OFFSET + 1, dataLength, 0, 4);
        int length = ByteExchangeUtil.byteArraytoInt(dataLength);
        //获取请求消息中数据包数量
        byte[] dataNum = new byte[2];
        System.arraycopy(data, DATA_OFFSET + 5, dataNum, 0, 2);
        int num = ByteExchangeUtil.byteArraytoInt(dataNum);
        //判断数据长度与数据包是否匹配
        if (!checkPkgNum(num, length)) {
            logger.info("设备[{}]分包请求中 数据长度与数据包数目不匹配,包数：[{}],长度[{}] ", gprsId,num,length);
            response[5] = (byte) 0x03;
            response = ProtocolUtil.beforeSend(response);
            buf.writeBytes(response);
            ctx.writeAndFlush(buf);
            return;
        }
        //获取附加数据
        byte[] additionalData = new byte[data.length - DATA_OFFSET - DATA_LENGTH - END_OFFSET];
        System.arraycopy(data, DATA_OFFSET + DATA_LENGTH, additionalData, 0,
                data.length - DATA_OFFSET - DATA_LENGTH - END_OFFSET);

        SubpackageInfo subpackageInfo = new SubpackageInfo();
        subpackageInfo.setDataType(dataType);
        subpackageInfo.setGprsId(gprsId);
        subpackageInfo.setSumDataLength(length);
        subpackageInfo.setSumPackageNum(num);
        subpackageInfo.setAdditionalData(additionalData);

        CacheUtil.putSubPkgInfo(gprsId, subpackageInfo);
        response = ProtocolUtil.beforeSend(response);
        buf.writeBytes(response);
        ctx.writeAndFlush(buf);
        logger.info("开始接收设备[{}]的[{}]数据,共[{}]条数据,长度[{}]", gprsId, dataType, num,length);
    }

    /**
     * 生成response 默认回复允许发送，200ms间隔
     * 注：未生成bcc
     * @return
     */
    private byte[] getResponse(){
        byte[] response = new byte[9];
        response[3] = (byte) 0xf1; //数据类型 ACK
        response[4] = (byte) 0xf2; //应答的帧类型 分包请求
        response[5] = (byte) 0x80;
        byte[] interval = ByteExchangeUtil.intToUInt16Bytes(200);
        System.arraycopy(interval,0,response,6,2);
        return response;
    }

    /**
     * 判断数据长度与数据包是否匹配
     * @param pkgNum
     * @param dataLength
     * @return
     */
    private boolean checkPkgNum(int pkgNum,int dataLength){
        return pkgNum*100 > dataLength && dataLength > 100;
    }

}
