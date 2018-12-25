package com.walletech.po;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author
 * @version 1.0
 * @description
 * @since
 */
public class PackDataInfo {
    /**
     * 数据Id，此值由pack_data_info表insert值时生成
     */
    private Integer id;
    /**
     * 基站ID
     */
    private String gprsId;
    /**
     * 电池组ID
     */
    private Integer stationId;
    /**
     * 数据接收时间
     */
    private Date rcvTime;
    /**
     * 设备状态
     */
    private String state;
    /**
     * 总电压
     */
    private BigDecimal genVol;
    /**
     * 总电流
     */
    private BigDecimal genCur;
    /**
     * 环境温度
     */
    private Long environTem;

    private BigDecimal soc;
    /**
     * 被动均衡状态
     */
    private Byte passiveBalance;
    /**
     * 主动均衡状态
     */
    private Byte initiativeBalance;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGprsId() {
        return gprsId;
    }

    public void setGprsId(String gprsId) {
        this.gprsId = gprsId;
    }

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    public Date getRcvTime() {
        return rcvTime;
    }

    public void setRcvTime(Date rcvTime) {
        this.rcvTime = rcvTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public BigDecimal getGenVol() {
        return genVol;
    }

    public void setGenVol(BigDecimal genVol) {
        this.genVol = genVol;
    }

    public BigDecimal getGenCur() {
        return genCur;
    }

    public void setGenCur(BigDecimal genCur) {
        this.genCur = genCur;
    }

    public Long getEnvironTem() {
        return environTem;
    }

    public void setEnvironTem(Long environTem) {
        this.environTem = environTem;
    }

    public BigDecimal getSoc() {
        return soc;
    }

    public void setSoc(BigDecimal soc) {
        this.soc = soc;
    }

    public Byte getPassiveBalance() {
        return passiveBalance;
    }

    public void setPassiveBalance(Byte passiveBalance) {
        this.passiveBalance = passiveBalance;
    }

    public Byte getInitiativeBalance() {
        return initiativeBalance;
    }

    public void setInitiativeBalance(Byte initiativeBalance) {
        this.initiativeBalance = initiativeBalance;
    }
}
