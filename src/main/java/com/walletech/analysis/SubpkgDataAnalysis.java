package com.walletech.analysis;


import com.walletech.bo.SubpackageInfo;
import com.walletech.po.PulseCMDInfo;
import com.walletech.po.PulseDischargeInfo;
import com.walletech.queue.Sender;
import com.walletech.queue.message.PulseCMDMessage;
import com.walletech.queue.message.PulseDischargeMessage;
import com.walletech.util.ByteExchangeUtil;
import com.walletech.util.CacheUtil;
import com.walletech.util.ProtocolUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 * 分包数据帧处理类
 */
@Component
public class SubpkgDataAnalysis implements Analysis{

    private static final Logger logger = LoggerFactory.getLogger(SubpkgDataAnalysis.class);

    private static final int DATA_OFFSET= 10;
    private static final BigDecimal DECIMAL_1000 = new BigDecimal(1000);
    private static final BigDecimal DECIMAL_10 = new BigDecimal(10);

    @Autowired
    private Sender sender;

    @Override
    public void doAnalysis(byte[] data, String gprsId, ChannelHandlerContext ctx) {
        //获取缓存中分包数据信息
        SubpackageInfo subpackageInfo = CacheUtil.getSubPkgInfo(gprsId);
        //获取分包编号
        int pkgIndex = getPkgIndex(data);
        //if未注册分包发送请求
        if (subpackageInfo==null){
            logger.warn("未查询到设备[{}]此分包对应请求，丢弃此包",gprsId);
            logger.warn("丢弃包类容：[{}]",StringUtil.toHexString(data));
            return;
        }
        //判断分包编号是否正确
        if (pkgIndex != subpackageInfo.getPackageIndex()+1){
            logger.warn("设备[{}]分包序号错误，请求重发");
            byte[] response = getResendResponse(subpackageInfo.getPackageIndex()+1);
            ByteBuf buf = ctx.alloc().buffer(8);
            buf.writeBytes(response);
            ctx.writeAndFlush(buf);
            return;
        }

        ArrayList<Byte> datas = subpackageInfo.getData();
        //将内容及序号更新到缓存
        for (int i = DATA_OFFSET+2 ; i < data.length-1; i++) {
            datas.add(data[i]);
        }
        subpackageInfo.setData(datas);
        subpackageInfo.setPackageIndex(pkgIndex);
        CacheUtil.putSubPkgInfo(gprsId,subpackageInfo);
        //分包未接收完毕
        if (pkgIndex != subpackageInfo.getSumPackageNum()){
            logger.debug("设备[{}]分包未接收完毕，继续接收，当前包数[{}]，总包数[{}]",
                    gprsId,pkgIndex,subpackageInfo.getSumPackageNum());
//            byte[] response = getACKResponse(pkgIndex);
//            ByteBuf buf = ctx.alloc().directBuffer(9);
//            buf.writeBytes(response);
//            ctx.writeAndFlush(buf);
            return;
        }
        switch (subpackageInfo.getDataType()){
            case DataType.PULSE_DISCHARGE_TYPE:
                analysisPulseData(subpackageInfo);
//                byte[] response = getACKResponse(pkgIndex);
//                ByteBuf buf = ctx.alloc().directBuffer(9);
//                buf.writeBytes(response);
//                ctx.writeAndFlush(buf);
                break;
            default:
                logger.warn("错误的分包类型");
        }
    }
    /**
     * 获取分包编号
     */
    private int getPkgIndex(byte[] data){
        byte[] num = new byte[2];
        System.arraycopy(data,DATA_OFFSET,num,0,2);
        return ByteExchangeUtil.byteArraytoInt(num);
    }

    /**
     * 生成重发响应
     */
    private byte[] getResendResponse(int pkgIndex){
        byte[] response = new byte[8];
        response[3] = (byte) 0xf4; //重发请求帧l
        response[4] = (byte) 0x02; //重发方式
        System.arraycopy(ByteExchangeUtil.intToUInt16Bytes(pkgIndex),0,response,5,2);
        response = ProtocolUtil.beforeSend(response);
        return response;
    }
    /**
     * 生成分包ACK响应
     */
    private byte[] getACKResponse(int pkgIndex){
        byte[] response = new byte[9];
        response[3] = (byte) 0xf1; //此帧类型
        response[4] = (byte) 0xf3; //应答的帧
        System.arraycopy(ByteExchangeUtil.intToUInt16Bytes(pkgIndex),0,response,5,2);
        response[7] = (byte) 0x80; //传送成功
        response = ProtocolUtil.beforeSend(response);
        return response;
    }
    /**
     * 解析脉冲数据
     */
    private void analysisPulseData(SubpackageInfo subpackageInfo){
        Date now = new Date();
        String gprsId = subpackageInfo.getGprsId();
        PulseCMDInfo pulseCMDInfo = CacheUtil.getPulseCMDInfo(gprsId);
        Integer pulseCell = pulseCMDInfo.getPulseCell();
        logger.info("[{}] [{}]脉冲数据接收完毕,开始解析",gprsId,pulseCell);
        byte[] data = null;
        try {
            if (!checkPulseData(subpackageInfo)){
                logger.warn("[{}] [{}]脉冲数据验证未通过,脉冲失败",gprsId,pulseCell);
                pulseFailed(now,gprsId,pulseCMDInfo);
                return;
            }
            ArrayList<Byte> dataInfos = subpackageInfo.getData();
            data = new byte[dataInfos.size()];
            for(int i=0 ; i < dataInfos.size() ; i++ ){
                data[i] = dataInfos.get(i);
            }
            //解析内阻数据
            logger.debug("[{}] [{}]脉冲完整数据：[{}]",gprsId,pulseCell,StringUtil.toHexString(data));
            byte[] impendanceBytes = new byte[4];
            System.arraycopy(data,0,impendanceBytes,0,4);
            BigDecimal impendance = new BigDecimal(ByteExchangeUtil.byteArraytoInt(impendanceBytes)).divide(DECIMAL_1000);
            byte[] points = new byte[2];
            System.arraycopy(data,4,points,0,2);
            Integer pointsNum = ByteExchangeUtil.byteArraytoInt(points);
            //解析电流电压
            int number = (data.length - 8) / 4;
            StringBuilder current = new StringBuilder();
            StringBuilder voltage = new StringBuilder();
            for (int i = 0;i < number ; i++){
                byte[] cur = new byte[2];
                System.arraycopy(data,8 + i*4,cur,0,2);
                current.append(new BigDecimal(ByteExchangeUtil.byteArraytoInt(cur) - 30000)
                        .multiply(DECIMAL_10).divide(DECIMAL_1000));
                current.append("-");
                byte[] vol = new byte[2];
                System.arraycopy(data,8 + i*4+2,vol,0,2);
                voltage.append(new BigDecimal(ByteExchangeUtil.byteArraytoInt(vol)).divide(DECIMAL_1000));
                voltage.append("-");
            }
            current.deleteCharAt(current.lastIndexOf("-"));
            voltage.deleteCharAt(voltage.lastIndexOf("-"));
            PulseDischargeInfo pulseDischargeInfo = new PulseDischargeInfo();
            pulseDischargeInfo.setPulseDischargeSendId(CacheUtil.getPulseCMDInfo(gprsId).getId());
            pulseDischargeInfo.setImpendance(impendance);
            pulseDischargeInfo.setPointsNumber(pointsNum);
            pulseDischargeInfo.setCurrent(current.toString());
            pulseDischargeInfo.setVoltage(voltage.toString());
            PulseDischargeMessage message = new PulseDischargeMessage();

            pulseCMDInfo.setEndTime(now);
            pulseCMDInfo.setSendDone(2);
            message.setPulseCMDInfo(pulseCMDInfo);
            message.setPulseDischargeInfo(pulseDischargeInfo);
            message.setType(DataType.PULSE_DISCHARGE_TYPE);
            sender.saveMessage(message);
            logger.info("解析[{}] [{}]脉冲数据完成",gprsId,pulseCell);
        } catch (Exception e){
            logger.error(e.getMessage());
            pulseFailed(now,gprsId,pulseCMDInfo);
            if (data != null)
            logger.error("解析[{}] [{}]脉冲数据失败: [{}]",gprsId,pulseCell, StringUtil.toHexString(data));
        }
    }

    /**
     * 脉冲失败处理
     */
    private void pulseFailed(Date now, String gprsId, PulseCMDInfo pulseCMDInfo){
        pulseCMDInfo.setSendDone(3);
        pulseCMDInfo.setEndTime(now);
        CacheUtil.putPulseCMDInfo(gprsId,pulseCMDInfo);
        PulseCMDMessage message = new PulseCMDMessage();
        message.setType(DataType.PULSE_CMD);
        message.setPulseCMDInfo(pulseCMDInfo);
        sender.saveMessage(message);
    }

    /**
     * 验证脉冲数据
     */
    private boolean checkPulseData(SubpackageInfo subpackageInfo){
        String gprsId = subpackageInfo.getGprsId();
        byte[] cellIndex = new byte[1];
        System.arraycopy(subpackageInfo.getAdditionalData(),0,cellIndex,0,1);
        Integer index = ByteExchangeUtil.byteArraytoInt(cellIndex);
        Integer pulseCMDCellIndex = CacheUtil.getPulseCMDInfo(gprsId).getPulseCell();
        if (!index.equals(pulseCMDCellIndex)){
            logger.warn("[{}]脉冲数据电池序号错误,脉冲请求为[{}],收到的数据为[{}]",gprsId,pulseCMDCellIndex,index);
            return false;
        }
        int dataSize = subpackageInfo.getData().size();
        if (dataSize % 4 != 0 || subpackageInfo.getSumDataLength()!=dataSize){
            logger.warn("[{}]脉冲数据量异常",gprsId);
            return false;
        }
        return true;
    }
}
