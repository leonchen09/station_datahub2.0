package com.walletech.util;

public class ByteExchangeUtil {

    /**
     * 字节数组转换为int值
     */

    public static int byteArraytoInt(byte[] n) {
        byte[] x = new byte[4];
        System.arraycopy(n, 0, x, 4 - n.length, n.length);
        int ans = ((x[0] & 0xFF) << 24) + ((x[1] & 0xFF) << 16) + ((x[2] & 0xFF) << 8) + (x[3] & 0xFF);
        return ans;
    }

    /**
     * 取int值的前十六位
     * @param crc
     * @return
     */
    public static byte[] intToUInt16Bytes(int crc) {
        byte[] ans = new byte[2];
        byte[] bytes = intToByteArray(crc);
        ans[0] = bytes[2];
        ans[1] = bytes[3];
        return ans;
    }

    /**
     * 取int值的前二十四位
     * @param crc
     * @return
     */
    public static byte[] intToUInt24Bytes(int crc) {
        byte[] ans = new byte[3];
        byte[] bytes = intToByteArray(crc);
        ans[0] = bytes[1];
        ans[1] = bytes[2];
        ans[2] = bytes[3];
        return ans;
    }

    /**
     * 将int类型数字转换为字节数组
     * @param num
     * @return
     */
    public static byte[] intToByteArray(int num) {
        byte[] ans = new byte[4];
        ans[0] = (byte) ((num >>> 24) & (byte) 0xff);
        ans[1] = (byte) ((num >>> 16) & (byte) 0xff);
        ans[2] = (byte) ((num >>> 8) & (byte) 0xff);
        ans[3] = (byte) (num & (byte) 0xff);
        return ans;
    }

    /**
     * 倒序传入的字节数据
     * @param object
     * @return
     */
    public static byte[] reverse(byte[] object) {
        byte[] ans = new byte[object.length];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = object[ans.length - 1 - i];
        }
        return ans;
    }

    /**
     * 按照接收数据的高位在前，低位在后交换高低位后转换为short
     */
    public static short byteToShort(byte[] b) {
        short s;
        short s0 = (short) (b[0] & 0xff);
        short s1 = (short) (b[1] & 0xff);
        s0 <<= 8;
        s = (short) (s0 | s1);
        return s;
    }

    /**
     * 从byte[]中截取出int
     */
    public static int getIntFromBytes(byte[] data,int startIndex,int length){
        byte[] bytes = new byte[length];
        System.arraycopy(data,startIndex,bytes,0,length);
        return byteArraytoInt(bytes);
    }


    /**
     * 从byte[]中截取出short
     */
    public static short getShortFromBytes(byte[] data,int startIndex,int length){
        byte[] bytes = new byte[length];
        System.arraycopy(data,startIndex,bytes,0,length);
        return byteToShort(bytes);
    }

    /**
     * short到字节数组的转换
     * @param number
     * @return
     */

    public static byte[] shortToByte(short number){
        byte[] b = new byte[2];
        for(int i = 0; i < 2; i++){
            int offset = 16 - (i+1)*8;
            b[i] = (byte)((number >> offset)&0xff);
        }
        return b;
    }

}
