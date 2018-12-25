package com.walletech.analysis;

import com.walletech.po.SalveVersionInfo;
import com.walletech.queue.Sender;
import com.walletech.queue.message.SalveVersionMessage;
import com.walletech.util.ProtocolUtil;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 处理远程读取从机版本信息
 */
@Component
public class SalveVersionAnalysis implements Analysis {

    private static final Logger logger = LoggerFactory.getLogger(SalveVersionAnalysis.class);

    private static final int DATA_OFFSET = 10;
    @Autowired
    private Sender sender;

    @Override
    public void doAnalysis(byte[] data, String gprsId, ChannelHandlerContext ctx) {
        logger.debug("开始解析[{}]远程读取从机版本信息帧",gprsId);
        Date now = new Date();
        List<SalveVersionInfo> infos = new ArrayList<>();
        for (int i = 0; i < (data.length-DATA_OFFSET-1) / 7; i++){
            SalveVersionInfo info = new SalveVersionInfo();
            info.setUpdateTme(now);
            info.setGprsId(gprsId);
            info.setCellIndex(i+1);
            byte[] version = new byte[7];
            System.arraycopy(data,DATA_OFFSET+ i*7,version,0,7);
            info.setVersion(ProtocolUtil.getVersionInfo(version));
            infos.add(info);
        }
        SalveVersionMessage message = new SalveVersionMessage();
        message.setType(DataType.SALVE_VERSION_READ);
        message.setSalveVersionInfos(infos);
        sender.saveMessage(message);
        logger.info("[{}]远程读取从机版本信息帧 解析完成",gprsId);
    }
}
