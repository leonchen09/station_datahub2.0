package com.walletech.analysis;

import com.walletech.util.ByteExchangeUtil;
import com.walletech.util.ProtocolUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
public class TimeSyncAnalysis implements Analysis{

    private static final Logger logger = LoggerFactory.getLogger(TimeSyncAnalysis.class);

    @Override
    public void doAnalysis(byte[] data, String gprsId, ChannelHandlerContext ctx) {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1;
        int day = now.get(Calendar.DAY_OF_MONTH);
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int min = now.get(Calendar.MINUTE);
        int second = now.get(Calendar.SECOND);
        logger.debug("[{}]时间同步：[{}]-[{}]-[{}] [{}]:[{}]:[{}] ",gprsId,year,month,day,hour,min,second);
        byte[] response = new byte[12];
        response[3] = (byte) 0x13;
        System.arraycopy(ByteExchangeUtil.intToUInt16Bytes(year),0,response,4,2);
        response[6] = (byte) month ;
        response[7] = (byte) day ;
        response[8] = (byte) hour ;
        response[9] = (byte) min ;
        response[10] = (byte) second ;
        response = ProtocolUtil.beforeSend(response);
        ByteBuf buf = ctx.alloc().directBuffer(12);
        buf.writeBytes(response);
        ctx.writeAndFlush(buf);
        logger.info("[{}]时间同步成功 ",gprsId);
    }
}
