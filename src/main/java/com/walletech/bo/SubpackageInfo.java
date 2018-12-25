package com.walletech.bo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 存储分包发送数据的实体类
 */
public class SubpackageInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * gprsId
     */
    private String gprsId;
    /**
     * 数据帧类型
     */
    private String dataType;
    /**
     * 总包数
     */
    private Integer sumPackageNum;
    /**
     * 总数据长度
     */
    private Integer sumDataLength;
    /**
     * 数据
     */
    private ArrayList<Byte> data = new ArrayList<>();
    /**
     * 当前包编号
     */
    private Integer packageIndex = 0;
    /**
     * 附加数据
     */
    private byte[] additionalData ;

    public String getGprsId() {
        return gprsId;
    }

    public void setGprsId(String gprsId) {
        this.gprsId = gprsId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Integer getSumPackageNum() {
        return sumPackageNum;
    }

    public void setSumPackageNum(Integer sumPackageNum) {
        this.sumPackageNum = sumPackageNum;
    }

    public Integer getSumDataLength() {
        return sumDataLength;
    }

    public void setSumDataLength(Integer sumDataLength) {
        this.sumDataLength = sumDataLength;
    }

    public ArrayList<Byte> getData() {
        return data;
    }

    public void setData(ArrayList<Byte> data) {
        this.data = data;
    }

    public Integer getPackageIndex() {
        return packageIndex;
    }

    public void setPackageIndex(Integer packageIndex) {
        this.packageIndex = packageIndex;
    }

    public byte[] getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(byte[] additionalData) {
        this.additionalData = additionalData;
    }
}
