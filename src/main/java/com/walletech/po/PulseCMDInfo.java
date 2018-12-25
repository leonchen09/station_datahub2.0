package com.walletech.po;

import com.walletech.bo.BaseRabbitMqInfo;

import java.io.Serializable;
import java.util.Date;

public class PulseCMDInfo extends BaseRabbitMqInfo implements Serializable {
    private static final long serialVersionUID = -1790933707193820043L;
    /**
     * id
     */
    private Integer id;
    /**
     * gprsId
     */
    private String gprsId;
    /**
     * stationId
     */
    private Integer stationId;
    /**
     * 放电电池序号
     */
    private Integer pulseCell;
    /**
     * 快速采样间隔
     */
    private Integer fastSampleInterval;
    /**
     * 慢速采样间隔
     */
    private Integer slowSampleInterval;
    /**
     * 放电时间
     */
    private Integer dischargeTime;
    /**
     * 慢采集采样时间
     */
    private Integer slowSampleTime;
    /**
     * 命令状态 0未发送,1发送成功，2脉冲执行成功，3脉冲执行失败, 4发送失败,6超时
     */
    private Integer sendDone;
    /**
     * 脉冲结束时间
     */
    private Date endTime;
    /**
     * 是否发送均衡命令
     */
    private Integer balanceClose;
    /**
     * 结果反馈
     */
    private Integer resultDescribe;

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

    public Integer getPulseCell() {
        return pulseCell;
    }

    public void setPulseCell(Integer pulseCell) {
        this.pulseCell = pulseCell;
    }

    public Integer getFastSampleInterval() {
        return fastSampleInterval;
    }

    public void setFastSampleInterval(Integer fastSampleInterval) {
        this.fastSampleInterval = fastSampleInterval;
    }

    public Integer getSlowSampleInterval() {
        return slowSampleInterval;
    }

    public void setSlowSampleInterval(Integer slowSampleInterval) {
        this.slowSampleInterval = slowSampleInterval;
    }

    public Integer getDischargeTime() {
        return dischargeTime;
    }

    public void setDischargeTime(Integer dischargeTime) {
        this.dischargeTime = dischargeTime;
    }

    public Integer getSlowSampleTime() {
        return slowSampleTime;
    }

    public void setSlowSampleTime(Integer slowSampleTime) {
        this.slowSampleTime = slowSampleTime;
    }

    public Integer getSendDone() {
        return sendDone;
    }

    public void setSendDone(Integer sendDone) {
        this.sendDone = sendDone;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getBalanceClose() {
        return balanceClose;
    }

    public void setBalanceClose(Integer balanceClose) {
        this.balanceClose = balanceClose;
    }

    public Integer getResultDescribe() {
        return resultDescribe;
    }

    public void setResultDescribe(Integer resultDescribe) {
        this.resultDescribe = resultDescribe;
    }
}
