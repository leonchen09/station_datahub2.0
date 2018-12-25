package com.walletech.po;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author
 * @version 1.0
 * @description
 * @since
 */
public class PackDataCellInfo {
    /**
     * 数据Id，此值由pack_data_cellinfo表insert值时生成
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
     * 单体编号
     */
    private Integer cellIndex;
    /**
     * 单体电压
     */
    private BigDecimal cellVol;
    /**
     * 单体电流
     */
    private BigDecimal cellCur;
    /**
     * 单体温度
     */
    private BigDecimal cellTem;
    /**
     * 0 未执行任何均衡;1 被动均衡中;2 主动均衡放电中;3 主动均衡充电中
     */
    private Integer cellEqu;
    /**
     * 从机的通讯成功率：
     * 0	通讯成功率低于25%
     * 1	通讯成功率25%~50%
     * 2	通讯成功率50%~75%
     * 3	通讯成功率75%以上
     */
    private Integer cellSuc;
    /**
     * 数据时间
     */
    private Date rcvTime;
    /**
     * dataInfo 关联ID
     */
    private Integer packDataInfoId;

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

    public Integer getCellIndex() {
        return cellIndex;
    }

    public void setCellIndex(Integer cellIndex) {
        this.cellIndex = cellIndex;
    }

    public BigDecimal getCellVol() {
        return cellVol;
    }

    public void setCellVol(BigDecimal cellVol) {
        this.cellVol = cellVol;
    }

    public BigDecimal getCellCur() {
        return cellCur;
    }

    public void setCellCur(BigDecimal cellCur) {
        this.cellCur = cellCur;
    }

    public BigDecimal getCellTem() {
        return cellTem;
    }

    public void setCellTem(BigDecimal cellTem) {
        this.cellTem = cellTem;
    }

    public Integer getCellEqu() {
        return cellEqu;
    }

    public void setCellEqu(Integer celEqu) {
        this.cellEqu = celEqu;
    }

    public Integer getCellSuc() {
        return cellSuc;
    }

    public void setCellSuc(Integer cellSuc) {
        this.cellSuc = cellSuc;
    }

    public Date getRcvTime() {
        return rcvTime;
    }

    public void setrcvTime(Date rcvTime) {
        this.rcvTime = rcvTime;
    }

    public Integer getPackDataInfoId() {
        return packDataInfoId;
    }

    public void setPackDataInfoId(Integer packDataInfoId) {
        this.packDataInfoId = packDataInfoId;
    }
}
