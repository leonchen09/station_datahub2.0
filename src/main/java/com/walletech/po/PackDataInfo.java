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
    //开关电源电压
    private BigDecimal switchVol;
    //a路在线状态
    private Byte aOnline;
    //a路接触器状态
    private Byte aContactStatus;
    //a路故障状态
    private Byte aError;
    //a路核容状态
    private Byte aVerifyStatus;
    //a路工作模式
    private Byte aMode;
    //a路故障码
    private Byte aErrorCode;
    //b路电压
    private BigDecimal bGenVol;
    //b路电流
    private BigDecimal bGenCur;
    //b路在线状态
    private Byte bOnline;
    //b路接触器状态
    private Byte bContactStatus;
    //b路故障状态
    private Byte bError;
    //b路核容状态
    private Byte bVerifyStatus;
    //b路工作模式
    private Byte bMode;
    //b路故障码
    private Byte bErrorCode;
    //c路电压
    private BigDecimal cGenVol;
    //c路电流
    private BigDecimal cGenCur;
    //c路在线状态
    private Byte cOnline;
    //c路接触器状态
    private Byte cContactStatus;
    //c路故障状态
    private Byte cError;
    //c路核容状态
    private Byte cVerifyStatus;
    //c路工作模式
    private Byte cMode;
    //c路故障码
    private Byte cErrorCode;
    //a路复位
    private Byte aReset;
    //b路复位
    private Byte bReset;
    //c路复位
    private Byte cReset;

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

    public BigDecimal getSwitchVol() {
        return switchVol;
    }

    public void setSwitchVol(BigDecimal switchVol) {
        this.switchVol = switchVol;
    }

    public Byte getaOnline() {
        return aOnline;
    }

    public void setaOnline(Byte aOnline) {
        this.aOnline = aOnline;
    }

    public Byte getaContactStatus() {
        return aContactStatus;
    }

    public void setaContactStatus(Byte aContactStatus) {
        this.aContactStatus = aContactStatus;
    }

    public Byte getaError() {
        return aError;
    }

    public void setaError(Byte aError) {
        this.aError = aError;
    }

    public Byte getaVerifyStatus() {
        return aVerifyStatus;
    }

    public void setaVerifyStatus(Byte aVerifyStatus) {
        this.aVerifyStatus = aVerifyStatus;
    }

    public Byte getaMode() {
        return aMode;
    }

    public void setaMode(Byte aMode) {
        this.aMode = aMode;
    }

    public Byte getaErrorCode() {
        return aErrorCode;
    }

    public void setaErrorCode(Byte aErrorCode) {
        this.aErrorCode = aErrorCode;
    }

    public BigDecimal getbGenVol() {
        return bGenVol;
    }

    public void setbGenVol(BigDecimal bGenVol) {
        this.bGenVol = bGenVol;
    }

    public BigDecimal getbGenCur() {
        return bGenCur;
    }

    public void setbGenCur(BigDecimal bGenCur) {
        this.bGenCur = bGenCur;
    }

    public Byte getbOnline() {
        return bOnline;
    }

    public void setbOnline(Byte bOnline) {
        this.bOnline = bOnline;
    }

    public Byte getbContactStatus() {
        return bContactStatus;
    }

    public void setbContactStatus(Byte bContactStatus) {
        this.bContactStatus = bContactStatus;
    }

    public Byte getbError() {
        return bError;
    }

    public void setbError(Byte bError) {
        this.bError = bError;
    }

    public Byte getbVerifyStatus() {
        return bVerifyStatus;
    }

    public void setbVerifyStatus(Byte bVerifyStatus) {
        this.bVerifyStatus = bVerifyStatus;
    }

    public Byte getbMode() {
        return bMode;
    }

    public void setbMode(Byte bMode) {
        this.bMode = bMode;
    }

    public Byte getbErrorCode() {
        return bErrorCode;
    }

    public void setbErrorCode(Byte bErrorCode) {
        this.bErrorCode = bErrorCode;
    }

    public BigDecimal getcGenVol() {
        return cGenVol;
    }

    public void setcGenVol(BigDecimal cGenVol) {
        this.cGenVol = cGenVol;
    }

    public BigDecimal getcGenCur() {
        return cGenCur;
    }

    public void setcGenCur(BigDecimal cGenCur) {
        this.cGenCur = cGenCur;
    }

    public Byte getcOnline() {
        return cOnline;
    }

    public void setcOnline(Byte cOnline) {
        this.cOnline = cOnline;
    }

    public Byte getcContactStatus() {
        return cContactStatus;
    }

    public void setcContactStatus(Byte cContactStatus) {
        this.cContactStatus = cContactStatus;
    }

    public Byte getcError() {
        return cError;
    }

    public void setcError(Byte cError) {
        this.cError = cError;
    }

    public Byte getcVerifyStatus() {
        return cVerifyStatus;
    }

    public void setcVerifyStatus(Byte cVerifyStatus) {
        this.cVerifyStatus = cVerifyStatus;
    }

    public Byte getcMode() {
        return cMode;
    }

    public void setcMode(Byte cMode) {
        this.cMode = cMode;
    }

    public Byte getcErrorCode() {
        return cErrorCode;
    }

    public void setcErrorCode(Byte cErrorCode) {
        this.cErrorCode = cErrorCode;
    }

    public Byte getaReset() {
        return aReset;
    }

    public void setaReset(Byte aReset) {
        this.aReset = aReset;
    }

    public Byte getbReset() {
        return bReset;
    }

    public void setbReset(Byte bReset) {
        this.bReset = bReset;
    }

    public Byte getcReset() {
        return cReset;
    }

    public void setcReset(Byte cReset) {
        this.cReset = cReset;
    }
}
