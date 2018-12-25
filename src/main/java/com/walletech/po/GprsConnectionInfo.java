package com.walletech.po;

import io.netty.channel.Channel;

import java.util.Date;

public class GprsConnectionInfo {

    private Integer id;
    /**
     * channel
     */
    private Channel channel;
    /**
     * GprsID
     */
    private String gprsId;
    /**
     * 在线状态
     */
    private Boolean linkStatus;
    /**
     * 上次在线时间
     */
    private Date lastActiveTime;
    /**
     * 设备所连接的服务器端口
     */
    private Integer port;
    /**
     * 设备所连接的服务器编号
     */
    private Integer serverNum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getGprsId() {
        return gprsId;
    }

    public void setGprsId(String gprsId) {
        this.gprsId = gprsId == null ? null : gprsId.trim();
    }

    public Boolean getLinkStatus() {
        return linkStatus;
    }

    public void setLinkStatus(Boolean linkStatus) {
        this.linkStatus = linkStatus;
    }

    public Date getLastActiveTime() {
        return lastActiveTime;
    }

    public void setLastActiveTime(Date lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getServerNum() {
        return serverNum;
    }

    public void setServerNum(Integer serverNum) {
        this.serverNum = serverNum;
    }
}