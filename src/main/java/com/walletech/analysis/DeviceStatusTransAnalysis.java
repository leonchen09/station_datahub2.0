package com.walletech.analysis;

import com.walletech.po.PackDataCellInfo;
import com.walletech.po.PackDataInfo;
import com.walletech.queue.Sender;
import com.walletech.queue.message.DeviceStatusMessage;
import com.walletech.util.ByteExchangeUtil;
import com.walletech.util.ProtocolUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 设备状态信息处理类
 */
@Component
public class DeviceStatusTransAnalysis implements Analysis {
    private static final Logger logger = LoggerFactory.getLogger(DeviceStatusTransAnalysis.class);

    @Autowired
    private Sender deviceStatusSender;

    private static final int CELL_VOL_DATA_SIZE = 2;
    private static final int DATA_LENGTH = 105;
    private static final int FULL_DATA_LENGTH = 121;
    private static final int DATA_OFFSET = 10;
    private static final BigDecimal DECIMAL_1000 = new BigDecimal(1000);
    private static final BigDecimal DECIMAL_10 = new BigDecimal(10);
    private static final BigDecimal DECIMAL_50 = new BigDecimal(50);

    /**
     * 解析数据信息
     * @param data
     * @param gprsId
     */
    public void doAnalysis(byte[] data,String gprsId,ChannelHandlerContext ctx) {
        if (data.length < DATA_LENGTH){
            logger.warn("设备[{}]状态数据长度异常:[{}]",gprsId,StringUtil.toHexString(data));
            return;
        }
        Date time = new Date();
        logger.debug("开始解析设备[{}]状态信息",gprsId);

        String batteryStatus = new StringBuilder(analysisBatteryStatus(data)).reverse().toString();

        PackDataInfo packDataInfo = analysisDataInfo(gprsId,data,batteryStatus,time);
        List<PackDataCellInfo> packDataCellInfoList = analysisCells(gprsId,data,time);

        DeviceStatusMessage message = new DeviceStatusMessage();
        message.setPackDataCellInfoList(packDataCellInfoList);
        message.setPackDataInfo(packDataInfo);
        message.setType(DataType.DEVICE_STATUS_TYPE);

        deviceStatusSender.saveMessage(message);
        logger.debug("[{}]设备状态帧 解析完成",gprsId);
    }

    /**
     * 获取设备信息
     * @param gprsId
     * @param data
     * @param batteryStatus
     * @param time
     * @return
     */
    private PackDataInfo analysisDataInfo(String gprsId, byte[] data, String batteryStatus,Date time){
        PackDataInfo packDataInfo = new PackDataInfo();
        packDataInfo.setGprsId(gprsId);
        packDataInfo.setRcvTime(time);
        packDataInfo.setState(batteryStatus(batteryStatus));
        packDataInfo.setGenVol(analysisGenVol(data));
        packDataInfo.setGenCur(analysisGenCur(data));
        packDataInfo.setEnvironTem((long) (data[4 + DATA_OFFSET] & 0xff) -DECIMAL_50.longValue());//温度偏移量50
        packDataInfo.setSoc(new BigDecimal(data[3+DATA_OFFSET]));
        packDataInfo.setPassiveBalance(passiveBalance(batteryStatus));
        packDataInfo.setInitiativeBalance(activeBalance(batteryStatus));
        if(data.length > DATA_LENGTH) {//加bcc位，长度超过105，表示有后续值传递。
            if (data.length < FULL_DATA_LENGTH){
                logger.warn("设备[{}]状态数据(2)长度异常:[{}]",gprsId,StringUtil.toHexString(data));
                return packDataInfo;
            }
            packDataInfo.setSwitchVol(analysisVol(data, 94));//开关电压
            packDataInfo.setbGenVol(analysisVol(data, 98));//B路电压
            packDataInfo.setbGenCur(analysisCur(data,100));//B路电流
            packDataInfo.setcGenVol(analysisVol(data, 104));//C路电压
            packDataInfo.setcGenCur(analysisCur(data, 106));//c路电流
            analysisVerifyStatus(data, packDataInfo);
        }
        return packDataInfo;
    }

    /**
     * 获取设备单体数据信息
     * @param gprsId
     * @param data
     * @param time
     * @return
     */
    private ArrayList<PackDataCellInfo> analysisCells(String gprsId,byte[] data,Date time){
        ArrayList<PackDataCellInfo> packDataCellInfoList = new ArrayList<>(34);
        int volIndex = 5;
        int temIndex = 53;
        int subDevNum = ProtocolUtil.getSubDeviceNum(gprsId);
        for (int i = 1;i<=subDevNum;i++){
            PackDataCellInfo cellInfo = new PackDataCellInfo();
            cellInfo.setGprsId(gprsId);
            cellInfo.setCellIndex(i);
            cellInfo.setCellVol(analysisSingleGenVol(volIndex, data));
            cellInfo.setCellCur(analysisGenCur(data));
            cellInfo.setCellTem(new BigDecimal(data[(temIndex++) + DATA_OFFSET] & 0xff).subtract(DECIMAL_50));//温度偏移量50
            cellInfo.setCellEqu(analysisBalanceStatu(i, data));
            cellInfo.setCellSuc(analysisSucStatu(i, data));
            cellInfo.setrcvTime(time);
            packDataCellInfoList.add(cellInfo);
            volIndex += CELL_VOL_DATA_SIZE;
        }
        return packDataCellInfoList;
    }

    /**
     * 获取单体电压，数据帧偏移量<p>DATA_OFFSET</p>
     * <p>index</p>表示单体电压的在数据帧的偏移量
     * 单体电压占<p>2</p>字节
     * @param index
     * @return
     */
    private BigDecimal analysisSingleGenVol(int index,byte[] data){
        byte[] genVol = new byte[2];
        System.arraycopy(data,index+DATA_OFFSET,genVol,0,2);
        return new BigDecimal(ByteExchangeUtil.byteArraytoInt(genVol)).divide(DECIMAL_1000);
    }


    /**
     * 获取电流，数据帧偏移量<p>DATA_OFFSET</p>
     * 电流字节在数据帧的偏移量为<p>1</p>
     * 电流数据占<p>2</p>字节
     * （服务器解析时要用300减去接收的值）
     * 例：主机电流为5A，主机发送的值为 300-5，
     * 服务器解析时300-(300-5) = 5A
     * @return
     */
    private BigDecimal analysisGenCur(byte[] data){
        byte[] genCur = new byte[2];
        System.arraycopy(data,1+DATA_OFFSET,genCur,0,2);
        return new BigDecimal(30000-ByteExchangeUtil.byteArraytoInt(genCur)).multiply(DECIMAL_10).divide(DECIMAL_1000);
    }

    /**
     * 获取总电压，数据帧偏移量<p>DATA_OFFSET</p>
     * 总电压的偏移量<p>89</p>
     * 总电压占<p>2</p>字节
     * 电压单位为<p>mV</p>所得值除以1000
     * @return
     */
    private BigDecimal analysisGenVol(byte[] data){
        byte[] genVol = new byte[2];
        System.arraycopy(data,89+DATA_OFFSET,genVol,0,2);
        return new BigDecimal(ByteExchangeUtil.byteArraytoInt(genVol)).multiply(DECIMAL_10).divide(DECIMAL_1000);
    }

    /**
     * 电源电压
     * @param data
     * @return
     */
    private BigDecimal analysisVol(byte[] data, int scrPos){
        byte[] genVol = new byte[2];
        System.arraycopy(data,scrPos + DATA_OFFSET,genVol,0,2);
        return new BigDecimal(ByteExchangeUtil.byteArraytoInt(genVol)).multiply(DECIMAL_10).divide(DECIMAL_1000);
    }
    /**
     * 获取电流，数据帧偏移量<p>DATA_OFFSET</p>
     * 电流数据占<p>2</p>字节
     * （服务器解析时要用300减去接收的值）
     * 例：主机电流为5A，主机发送的值为 300-5，
     * 服务器解析时300-(300-5) = 5A
     * @return
     */
    private BigDecimal analysisCur(byte[] data, int scrPos){
        byte[] genCur = new byte[2];
        System.arraycopy(data,scrPos + DATA_OFFSET,genCur,0,2);
        return new BigDecimal(30000-ByteExchangeUtil.byteArraytoInt(genCur)).multiply(DECIMAL_10).divide(DECIMAL_1000);
    }

    /**
     * 设置核容状态
     * Bit0:锂电池在线状态；
     * Bit1:铅酸接触器状态：0：断开；1：闭合；
     * Bit2:锂电池接触器状态：0：断开；1：闭合；
     * Bit3:核容状态；0：正常；1：故障；
     * Bit4:虚拟电源状态；0：断开；1：闭合；
     * Bit5:核容状态；0：未核容；1：正在核容；
     * Bit6~Bit8:核容从机状态；
     * @param data
     * @param packDataInfo
     */
    private void analysisVerifyStatus(byte[] data, PackDataInfo packDataInfo){
        String aStatus = getVerifyStatus(data, 96);//a路状态
        if(logger.isDebugEnabled()) {
            logger.debug("设备[{}]a路状态：[{}]", packDataInfo.getGprsId(), aStatus);
        }
        //Bit0：A路电池在线状态；0：不在线；1：在线；
        packDataInfo.setaOnline((byte)Integer.parseInt(aStatus.substring(0, 1)));
        //Bit1：A路接触器状态：  0：断开；1：闭合；
        packDataInfo.setaContactStatus((byte)Integer.parseInt(aStatus.substring(1, 2)));
        //Bit2：A路故障状态；    0：正常；1：故障；
        packDataInfo.setaError((byte)Integer.parseInt(aStatus.substring(2, 3)));
        //Bit3：A路核容状态；    0：未核容；1：正在核容；
        packDataInfo.setaVerifyStatus((byte)Integer.parseInt(aStatus.substring(3, 4)));
        //Bit4~Bit6: A路工作模式；取值范围：0~7；
        packDataInfo.setaMode((byte)Integer.parseInt(new StringBuilder(aStatus.substring(4, 7)).reverse().toString(), 2));
        //Bit7：A路复位标志；    0：未复位；1：复位；
        packDataInfo.setaReset((byte)Integer.parseInt(aStatus.substring(7, 8)));
        //Bit8~Bit10：A路故障码；故障码定义：
        packDataInfo.setaErrorCode((byte)Integer.parseInt(new StringBuilder(aStatus.substring(8, 11)).reverse().toString(), 2));

        String bStatus = getVerifyStatus(data, 102);//b路状态
        if(logger.isDebugEnabled()) {
            logger.debug("设备[{}]b路状态：[{}]", packDataInfo.getGprsId(), bStatus);
        }
        //Bit0：A路电池在线状态；0：不在线；1：在线；
        packDataInfo.setbOnline((byte)Integer.parseInt(bStatus.substring(0, 1)));
        //Bit1：A路接触器状态：  0：断开；1：闭合；
        packDataInfo.setbContactStatus((byte)Integer.parseInt(bStatus.substring(1, 2)));
        //Bit2：A路故障状态；    0：正常；1：故障；
        packDataInfo.setbError((byte)Integer.parseInt(bStatus.substring(2, 3)));
        //Bit3：A路核容状态；    0：未核容；1：正在核容；
        packDataInfo.setbVerifyStatus((byte)Integer.parseInt(bStatus.substring(3, 4)));
        //Bit4~Bit6: A路工作模式；取值范围：0~7；
        packDataInfo.setbMode((byte)Integer.parseInt(new StringBuilder(bStatus.substring(4, 7)).reverse().toString(), 2));
        //Bit7：A路复位标志；    0：未复位；1：复位；
        packDataInfo.setbReset((byte)Integer.parseInt(bStatus.substring(7, 8)));
        //Bit8~Bit10：A路故障码；故障码定义：
        packDataInfo.setbErrorCode((byte)Integer.parseInt(new StringBuilder(bStatus.substring(8, 11)).reverse().toString(), 2));

        String cStatus = getVerifyStatus(data, 108);//c路状态
        if(logger.isDebugEnabled()) {
            logger.debug("设备[{}]c路状态：[{}]", packDataInfo.getGprsId(), cStatus);
        }
        //Bit0：A路电池在线状态；0：不在线；1：在线；
        packDataInfo.setcOnline((byte)Integer.parseInt(cStatus.substring(0, 1)));
        //Bit1：A路接触器状态：  0：断开；1：闭合；
        packDataInfo.setcContactStatus((byte)Integer.parseInt(cStatus.substring(1, 2)));
        //Bit2：A路故障状态；    0：正常；1：故障；
        packDataInfo.setcError((byte)Integer.parseInt(cStatus.substring(2, 3)));
        //Bit3：A路核容状态；    0：未核容；1：正在核容；
        packDataInfo.setcVerifyStatus((byte)Integer.parseInt(cStatus.substring(3, 4)));
        //Bit4~Bit6: A路工作模式；取值范围：0~7；
        packDataInfo.setcMode((byte)Integer.parseInt(new StringBuilder(cStatus.substring(4, 7)).reverse().toString(), 2));
        //Bit7：A路复位标志；    0：未复位；1：复位；
        packDataInfo.setcReset((byte)Integer.parseInt(cStatus.substring(7, 8)));
        //Bit8~Bit10：A路故障码；故障码定义：
        packDataInfo.setcErrorCode((byte)Integer.parseInt(new StringBuilder(cStatus.substring(8, 11)).reverse().toString(), 2));

    }

    private String getVerifyStatus(byte[] data, int srcPos){
        //高低位反向，先处理后一个byte，再处理前一个byte。
        StringBuilder byteString = new StringBuilder(byteToBit(data[srcPos + 1 +DATA_OFFSET])).reverse();
        byteString.append(new StringBuilder(byteToBit(data[srcPos + DATA_OFFSET])).reverse());
        return byteString.toString();
    }

    /**
     * 是否支持被动均衡
     * @return 0 不支持 1 支持
     */
    private byte passiveBalance(String batteryStatus){
        return (byte) ("0".equals(batteryStatus.substring(2,3))?0:1);
    }

    /**
     * 是否支持主动均衡
     * @return 0 不支持 1 支持
     */
    private byte activeBalance(String batteryStatus){
        return (byte) ("0".equals(batteryStatus.substring(3,4))?0:1);
    }

    /**
     * 获取电池组状态
     * @return
     */
    private String batteryStatus(String batteryStatus){
        String result = "无效";

        switch (new StringBuilder(batteryStatus.substring(0,2)).reverse().toString()){
            case "01":
                result = "浮充";
                break;
            case "10":
                result = "放电";
                break;
            case "11":
                result = "充电";
                break;
            default:
                result = "无效";
        }
        return result;
    }

    /**
     * 电池状态转换为二进制字符串，如："01001010"
     * @return
     */
    private String analysisBatteryStatus(byte[] data){
        return byteToBit(data[0+DATA_OFFSET]);
    }

    /**
     * 获取单体均衡状态，数据帧偏移量<p>DATA_OFFSET</p>
     * 均衡状态在数据帧的偏移量<p>77</p>,
     * 公式<p>(index-1)/4+77+DATA_OFFSET</p>计算得当前单体状态所在字节序列
     * 公式<p>(index-1)%4+1</p>计算得到当前单体均衡状态所在起始bit
     * @param index
     * @return
     */
    private int analysisBalanceStatu(int index,byte[] data){
        int byteIndex = (index-1)/4+77+DATA_OFFSET;
        int bitIndex = (index-1)%4+1;
        return switchStatus(getStatus(byteIndex,bitIndex,data));
    }
    /**
     * 获取单体通讯状态，数据帧偏移量<p>DATA_OFFSET</p>
     * 通讯状态在数据帧的偏移量<p>83</p>,
     * 公式<p>(index-1)/4+83+DATA_OFFSET</p>计算得当前单体状态所在字节序列
     * 公式<p>(index-1)%4+1</p>计算得到当前单体均衡状态所在起始bit
     * @param index
     * @return
     */
    private int analysisSucStatu(int index,byte[] data){
        int byteIndex = (index-1)/4+83+DATA_OFFSET;
        int bitIndex = (index-1)%4+1;
        return switchStatus(getStatus(byteIndex,bitIndex,data));
    }

    /**
     * 根据字节序号和位序号计算当前单体对应的状态位(2bit)
     * @param byteIndex
     * @param bitIndex
     * @return
     */
    private String getStatus(int byteIndex,int bitIndex,byte[] data){
        String byteString = new StringBuilder(byteToBit(data[byteIndex])).reverse().toString();
        return byteString.substring((bitIndex-1)*2,bitIndex*2);
    }

    /**
     * 根据状态位标识装换为数据库字段
     * 如下是均衡状态转换规则：
     * <p>00</p>表示未执行任何均衡 0
     * <p>01</p>被动均衡中         1
     * <p>10</p>主动均衡放电中     2
     * <p>11</p>主动均衡充电中     3
     *
     * 如下是通讯状态转换规则：
     * <p>00</p>通讯成功率低于25%  0
     * <p>01</p>通讯成功率25%~50%  1
     * <p>10</p>通讯成功率50%~75%  2
     * <p>11</p>通讯成功率75%以上  3
     * @param status
     * @return
     */
    private int switchStatus(String status){
        int result = 0;
        switch (new StringBuilder(status).reverse().toString()){
            case "01":
                result = 1;
                break;
            case "10":
                result = 2;
                break;
            case "11":
                result = 3;
                break;
            default:
                result = 0;
        }
        return result;
    }

    /**
     * 将一字节转换为二进制字符串
     * @param b
     * @return
     */
    private String byteToBit(byte b) {
        StringBuilder sb = new StringBuilder();
        sb.append((byte) ((b >> 7) & 0x1)).append((byte) ((b >> 6) & 0x1))
                .append((byte) ((b >> 5) & 0x1)).append((byte) ((b >> 4) & 0x1))
                .append((byte) ((b >> 3) & 0x1)).append((byte) ((b >> 2) & 0x1))
                .append((byte) ((b >> 1) & 0x1)).append((byte) ((b) & 0x1));
        return sb.toString();
    }

}
