package com.walletech.po;

import java.util.Date;

public class WarningInfo {
    /**
     * 自增主键
     */
    private Integer id;
    /**
     * 电池组id
     */
    private Integer stationId;
    /**
     * 设备id
     */
    private String gprsId;
    /**
     * 数据时间
     */
    private Date rcvTime;
    /**
     * 单体温度过高
     */
    private Byte cellTemHigh = 0;
    /**
     * 环境温度过高
     */
    private Byte envTemHigh = 0;
    /**
     * 单体温度过低
     */
    private Byte cellTemLow = 0;
    /**
     * 环境温度过低
     */
    private Byte envTemLow = 0;
    /**
     * 总电压过压
     */
    private Byte genVolHigh = 0;
    /**
     * 总电压欠压
     */
    private Byte genVolLow = 0;
    /**
     * 掉电告警
     */
    private Byte lossElectricity = 0;
    /**
     * SOC过低告警
     */
    private Byte socLow = 0;
    /**
     * 异常电流
     */
    private Integer abnormalCurrent = 0;
    /**
     * 单体电压过高
     */
    private Integer singleVolHigh = 0;
    /**
     * 单体电压过低
     */
    private Integer SingleVolLow = 0;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    public String getGprsId() {
        return gprsId;
    }

    public void setGprsId(String gprsId) {
        this.gprsId = gprsId;
    }

    public Date getRcvTime() {
        return rcvTime;
    }

    public void setRcvTime(Date rcvTime) {
        this.rcvTime = rcvTime;
    }

    public Byte getCellTemHigh() {
        return cellTemHigh;
    }

    public void setCellTemHigh(Byte cellTemHigh) {
        this.cellTemHigh = cellTemHigh;
    }

    public Byte getEnvTemHigh() {
        return envTemHigh;
    }

    public void setEnvTemHigh(Byte envTemHigh) {
        this.envTemHigh = envTemHigh;
    }

    public Byte getCellTemLow() {
        return cellTemLow;
    }

    public void setCellTemLow(Byte cellTemLow) {
        this.cellTemLow = cellTemLow;
    }

    public Byte getGenVolHigh() {
        return genVolHigh;
    }

    public void setGenVolHigh(Byte genVolHigh) {
        this.genVolHigh = genVolHigh;
    }

    public Byte getGenVolLow() {
        return genVolLow;
    }

    public void setGenVolLow(Byte genVolLow) {
        this.genVolLow = genVolLow;
    }

    public Byte getLossElectricity() {
        return lossElectricity;
    }

    public void setLossElectricity(Byte lossElectricity) {
        this.lossElectricity = lossElectricity;
    }

    public Byte getSocLow() {
        return socLow;
    }

    public void setSocLow(Byte socLow) {
        this.socLow = socLow;
    }

    public Integer getAbnormalCurrent() {
        return abnormalCurrent;
    }

    public void setAbnormalCurrent(Integer abnormalCurrent) {
        this.abnormalCurrent = abnormalCurrent;
    }

    public Integer getSingleVolHigh() {
        return singleVolHigh;
    }

    public void setSingleVolHigh(Integer singleVolHigh) {
        this.singleVolHigh = singleVolHigh;
    }

    public Integer getSingleVolLow() {
        return SingleVolLow;
    }

    public void setSingleVolLow(Integer SingleVolLow) {
        this.SingleVolLow = SingleVolLow;
    }

    public Byte getEnvTemLow() {
        return envTemLow;
    }

    public void setEnvTemLow(Byte envTemLow) {
        this.envTemLow = envTemLow;
    }



}
