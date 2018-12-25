package com.walletech.queue.message;

import com.walletech.po.PackDataCellInfo;
import com.walletech.po.PackDataInfo;

import java.util.List;

/**
 * 实现消息接口，并实现相应类型消息的载体<p>packDataInfo</p>、<p>packDataCellInfoList</p>
 * @author
 * @version 1.0
 * @description
 * @since
 */
public class DeviceStatusMessage extends Message{

    private PackDataInfo packDataInfo;
    private List<PackDataCellInfo> packDataCellInfoList;

    /**
     * 获取电池组单体的状态列表
     * @return
     */
    public List<PackDataCellInfo> getPackDataCellInfoList() {
        return packDataCellInfoList;
    }

    /**
     * 设置电池组单体的状态
     * @param packDataCellInfoList
     */
    public void setPackDataCellInfoList(List<PackDataCellInfo> packDataCellInfoList) {
        this.packDataCellInfoList = packDataCellInfoList;
    }

    /**
     * 获取电池组信息
     * @return
     */
    public PackDataInfo getPackDataInfo() {
        return packDataInfo;
    }

    /**
     * 设置电池组信息
     * @param packDataInfo
     */
    public void setPackDataInfo(PackDataInfo packDataInfo) {
        this.packDataInfo = packDataInfo;
    }

}
