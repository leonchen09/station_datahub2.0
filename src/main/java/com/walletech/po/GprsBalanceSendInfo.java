package com.walletech.po;

import com.walletech.bo.BaseRabbitMqInfo;

import java.io.Serializable;
import java.util.Date;

public class GprsBalanceSendInfo extends BaseRabbitMqInfo implements Serializable {
    private static final long serialVersionUID = 200832895619378086L;

    private Integer id;

    private String gprsId;

    private Byte cell1;

    private Byte cell2;

    private Byte cell3;

    private Byte cell4;

    private Byte cell5;

    private Byte cell6;

    private Byte cell7;

    private Byte cell8;

    private Byte cell9;

    private Byte cell10;

    private Byte cell11;

    private Byte cell12;

    private Byte cell13;

    private Byte cell14;

    private Byte cell15;

    private Byte cell16;

    private Byte cell17;

    private Byte cell18;

    private Byte cell19;

    private Byte cell20;

    private Byte cell21;

    private Byte cell22;

    private Byte cell23;

    private Byte cell24;

    private Integer duration;

    private Integer mode;

    private Byte sendDone;

    private Date sendTime;

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
        this.gprsId = gprsId == null ? null : gprsId.trim();
    }

    public Byte getCell1() {
        return cell1;
    }

    public void setCell1(Byte cell1) {
        this.cell1 = cell1;
    }

    public Byte getCell2() {
        return cell2;
    }

    public void setCell2(Byte cell2) {
        this.cell2 = cell2;
    }

    public Byte getCell3() {
        return cell3;
    }

    public void setCell3(Byte cell3) {
        this.cell3 = cell3;
    }

    public Byte getCell4() {
        return cell4;
    }

    public void setCell4(Byte cell4) {
        this.cell4 = cell4;
    }

    public Byte getCell5() {
        return cell5;
    }

    public void setCell5(Byte cell5) {
        this.cell5 = cell5;
    }

    public Byte getCell6() {
        return cell6;
    }

    public void setCell6(Byte cell6) {
        this.cell6 = cell6;
    }

    public Byte getCell7() {
        return cell7;
    }

    public void setCell7(Byte cell7) {
        this.cell7 = cell7;
    }

    public Byte getCell8() {
        return cell8;
    }

    public void setCell8(Byte cell8) {
        this.cell8 = cell8;
    }

    public Byte getCell9() {
        return cell9;
    }

    public void setCell9(Byte cell9) {
        this.cell9 = cell9;
    }

    public Byte getCell10() {
        return cell10;
    }

    public void setCell10(Byte cell10) {
        this.cell10 = cell10;
    }

    public Byte getCell11() {
        return cell11;
    }

    public void setCell11(Byte cell11) {
        this.cell11 = cell11;
    }

    public Byte getCell12() {
        return cell12;
    }

    public void setCell12(Byte cell12) {
        this.cell12 = cell12;
    }

    public Byte getCell13() {
        return cell13;
    }

    public void setCell13(Byte cell13) {
        this.cell13 = cell13;
    }

    public Byte getCell14() {
        return cell14;
    }

    public void setCell14(Byte cell14) {
        this.cell14 = cell14;
    }

    public Byte getCell15() {
        return cell15;
    }

    public void setCell15(Byte cell15) {
        this.cell15 = cell15;
    }

    public Byte getCell16() {
        return cell16;
    }

    public void setCell16(Byte cell16) {
        this.cell16 = cell16;
    }

    public Byte getCell17() {
        return cell17;
    }

    public void setCell17(Byte cell17) {
        this.cell17 = cell17;
    }

    public Byte getCell18() {
        return cell18;
    }

    public void setCell18(Byte cell18) {
        this.cell18 = cell18;
    }

    public Byte getCell19() {
        return cell19;
    }

    public void setCell19(Byte cell19) {
        this.cell19 = cell19;
    }

    public Byte getCell20() {
        return cell20;
    }

    public void setCell20(Byte cell20) {
        this.cell20 = cell20;
    }

    public Byte getCell21() {
        return cell21;
    }

    public void setCell21(Byte cell21) {
        this.cell21 = cell21;
    }

    public Byte getCell22() {
        return cell22;
    }

    public void setCell22(Byte cell22) {
        this.cell22 = cell22;
    }

    public Byte getCell23() {
        return cell23;
    }

    public void setCell23(Byte cell23) {
        this.cell23 = cell23;
    }

    public Byte getCell24() {
        return cell24;
    }

    public void setCell24(Byte cell24) {
        this.cell24 = cell24;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public Byte getSendDone() {
        return sendDone;
    }

    public void setSendDone(Byte sendDone) {
        this.sendDone = sendDone;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

}