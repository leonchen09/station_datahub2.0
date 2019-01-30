package com.walletech.util;

import com.walletech.po.GprsStateSnapshot;

import java.util.HashMap;

/**
 * @author
 * @version 1.0
 * @description
 * @since
 */
public class ProtocolUtil {
    private static final int MIN_FRAME_LENGTH = 11;
    /***
     * 从上传数据中获得GprsID
     * @param msg
     * @return
     */
    public static String getGprsId(byte[] msg) {
        if (msg.length < MIN_FRAME_LENGTH) {
            return null;
        }
        //取主机地址
        byte[] gprs = new byte[6];
        System.arraycopy(msg, 4, gprs, 0, 6);
        //gprsId数字位
        byte[] n = new byte[3];
        System.arraycopy(gprs, 3, n, 0, 3);
//        String gprsId = "" + (char) (gprs[0] & 0xff) + (char) (gprs[1] & 0xff) + (char) (gprs[2] & 0xff);
        String gprsId = new String(new byte[]{gprs[0],gprs[1],gprs[2]});
        String idNum = "" + ByteExchangeUtil.byteArraytoInt(n);
        gprsId += String.format("%6s", idNum.trim()).replaceAll("\\s", "0");
        return gprsId;
    }

    /**
     * 将gprsHostId转为byte[]
     * @param gprsId
     * @return
     */
    public static byte[] getGprsIdBytes(String gprsId){
        if (gprsId.length() != 9){
            return null;
        }
        byte[] bytes = new byte[6];
        String idStr = gprsId.substring(0,3);
        System.arraycopy(idStr.getBytes(),0,bytes,0,3);
        Integer idNum = Integer.valueOf(gprsId.substring(3));
        System.arraycopy(ByteExchangeUtil.intToUInt24Bytes(idNum),0,bytes,3,3);
        return bytes;
    }
    /**
     * 将gprsSlaveId转为byte[]
     */
    public static byte[] getSlaveIdBytes(String slaveId){
        if (slaveId.length() != 9){
            return null;
        }
        byte[] bytes = new byte[4];
        String idStr = slaveId.substring(0,1);
        byte[] midBytes = ByteExchangeUtil.intToByteArray(Integer.valueOf(slaveId.substring(1,7)));
        byte type = (byte)((idStr.charAt(0) - 'A' + 1)<<4 );
        bytes[0] = (byte) (type + midBytes[1]);
        bytes[1] = midBytes[2];
        bytes[2] = midBytes[3];
        bytes[3] = Integer.valueOf(slaveId.substring(7)).byteValue();
        return bytes;
    }


    /**
     * 根据gprsId获取stationId
     * @param gprsId
     * @return
     */
    public static Integer getStationId(String gprsId){
        Integer stationId = (Integer) RedisUtil.get("srv_battery_station_gatewayAndBackground_"+gprsId);
        return stationId;
    }

    /**
     * 通过gprsId 从Redis中获取设备状态
     * @param gprsId
     * @return
     */
    public static GprsStateSnapshot getStateSnapshotByGprsId(String gprsId){
        return CacheUtil.getGprsStateSnapshotByGprsId(gprsId);
    }

    /**
     * 通过stationId 从Redis中获取设备状态
     * @param stationId
     * @return
     */
    public static GprsStateSnapshot getStateSnapshotByStationId(Integer stationId){
        return CacheUtil.getGprsStateSnapshotByStationId(stationId);
    }


    /**
     * 判断设备类型
     * @param gprsId
     * @return
     */
    public static int getDeviceType(String gprsId){
        char[] id = gprsId.toCharArray();
        int devType;
        switch (id[2])
        {
            case 'A':
                devType = 2;
                break;
            case 'B':
                devType = 1;
                break;
            case 'C':
                devType = 3;
                break;
            case 'D':
                devType = 4;
                break;
            case 'E':
                devType = 5;
                break;
            default:
                devType = 0;
                break;
        }
        return devType;
    }

    /**
     * 根据gprsId 判断从机个数
     * @param gprsId
     * @return
     */
    public static int getSubDeviceNum(String gprsId){
        int num = 24;
        if (getDeviceType(gprsId) == 4){
            num = 4;
        }
        return num;
    }

    /**
     * 获取版本号
     * @param bytes
     * @return
     */
    public static String getVersionInfo(byte[] bytes){
        int pub = bytes[2] >> 4;
        int child = bytes[2] & 0b0000_1111;
        int modify = bytes[3] >> 4;
        int year = Integer.valueOf(Integer.toHexString(bytes[4]));
        int month = Integer.valueOf(Integer.toHexString(bytes[5]));
        int day = Integer.valueOf(Integer.toHexString(bytes[6]));
        return pub + "." + child + "." + modify + " 20" + year + "-" + month + "-" +day;
    }

    /**
     * 校验接受到的数据
     * @param data
     * @return
     */
    public static boolean isRealData(byte[] data) {
        if (data[0] != (byte) 0x7f || data[1] != (byte) 0xf7){
            return false;
        }
        byte bcc = 0x00;
        for (int i = 0; i < data.length - 1; i++) {
            bcc ^= data[i];
        }
        return bcc == data[data.length - 1];
    }

    /**
     * 返回bcc数据校验
     * @param data
     * @return
     */
    public static byte bcc(byte[] data) {
        byte ans=0x00;
        for (int i = 0; i < data.length; i++) {
            ans^=data[i];
        }
        return ans;
    }

    /**
     * 补0
     * @param str
     * @param i
     * @return
     */
    public static String supplementZero(String str,int i){
        return String.format("%"+i+"s", str.trim()).replaceAll("\\s", "0");
    }

    /**
     * 生成数据头尾
     * @param bytes
     * @return
     */
    public static byte[] beforeSend(byte[] bytes){
        bytes[0] = (byte) 0x7f;
        bytes[1] = (byte) 0xf7;
        bytes[2] = (byte) (bytes.length-3);
        bytes[bytes.length-1] = bcc(bytes);
        return bytes;
    }
}
