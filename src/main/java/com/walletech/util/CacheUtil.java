package com.walletech.util;

import com.walletech.bo.SubpackageInfo;
import com.walletech.po.*;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class CacheUtil {

    public static final ConcurrentHashMap<String,Long> MQ_DELIVERY_TAG_MAP = new ConcurrentHashMap<>();

    public static final AttributeKey<String> GPRS = AttributeKey.valueOf("GPRS");
    //保存连接到本机的设备信息 key:gprsId
    private static final ConcurrentHashMap<String,GprsConnectionInfo> GPRS_MAP = new ConcurrentHashMap<>();
    //保存连接本机的设备和channel key:gprsId
    private static final ConcurrentHashMap<String,Channel> GPRS_CHANNEL_MAP = new ConcurrentHashMap<>();
    //保存分包发送的数据信息(脉冲放电数据) key:gprsId
    private static final ConcurrentHashMap<String,SubpackageInfo> SUBPKGINFO_MAP = new ConcurrentHashMap<>();
    //保存正在处理的脉冲放电信息 key:gprsId
    private static final ConcurrentHashMap<String,PulseCMDInfo> PULSE_CMD_MAP = new ConcurrentHashMap<>();
    //保存正在处理的均衡指令 key:gprsId
    private static final ConcurrentHashMap<String,GprsBalanceSendInfo> BALANCE_SEND_MAP = new ConcurrentHashMap<>();
    //保存正在处理的修改容量指令 key:gprsId
    private static final ConcurrentHashMap<String,ModifyCapacityInfo> MODIFY_CAPACITY_MAP = new ConcurrentHashMap<>();
    //保存正在处理的修改均衡策略指令 key:gprsId
    private static final ConcurrentHashMap<String,ModifyBalanceStrategyInfo> MODIFY_BALANCE_STRATEGY_MAP = new ConcurrentHashMap<>();
    //保存正在处理的GprsID修改信息 key:gprsId
    private static final ConcurrentHashMap<String,ModifyGprsIdInfo> MODIFY_GPRSID_MAP = new ConcurrentHashMap<>();
    //保存发送MQ失败的修改id应答消息
    public static final List<ModifyGprsIdInfo> SEND_MODIFYGPRSID_ACK_FAILED_LIST = new ArrayList<>();
    //保存设备状态快照 by gprsId
    private static final ConcurrentHashMap<String,GprsStateSnapshot> GPRS_STATE_SNAPSHOT_BYGPRSID = new ConcurrentHashMap<>();
    //保存设备状态快照 by stationId
    private static final ConcurrentHashMap<Integer,GprsStateSnapshot> GPRS_STATE_SNAPSHOT_BYSTATIONID = new ConcurrentHashMap<>();
    //保存发送的均衡从机配置指令
    private static final ConcurrentHashMap<String,SubBalanceConfigSend> SUB_BALANCE_CONFIG_SEND_MAP = new ConcurrentHashMap<>();
    //保存发送的均衡从机状态修改指令
    private static final ConcurrentHashMap<String,SubBalanceStatusSend> SUB_BALANCE_STATUS_SEND_MAP = new ConcurrentHashMap<>();
    /**
     * 保存分包数据信息 (集群部署修改部分)
     * @param gprsId
     * @param subpackageInfo
     */
    public static void putSubPkgInfo(String gprsId,SubpackageInfo subpackageInfo){
        SUBPKGINFO_MAP.put(gprsId,subpackageInfo);
    }
    /**
     * 获取分包数据信息 (集群部署修改部分)
     * @param gprsId
     * @return
     */
    public static SubpackageInfo getSubPkgInfo(String gprsId){
        return SUBPKGINFO_MAP.get(gprsId);
    }
    /**
     * 删除分包数据信息 (集群部署修改部分)
     * @param gprsId
     */
    public static void removeSubPkgInfo(String gprsId){
        SUBPKGINFO_MAP.remove(gprsId);
    }
    /**
     * 保存脉冲请求信息 (集群部署修改部分)
     * @param gprsId
     * @param pulseCMDInfo
     */
    public static void putPulseCMDInfo(String gprsId, PulseCMDInfo pulseCMDInfo){
        PULSE_CMD_MAP.put(gprsId,pulseCMDInfo);
    }
    /**
     * 获取脉冲请求信息 (集群部署修改部分)
     * @param gprsId
     * @return
     */
    public static PulseCMDInfo getPulseCMDInfo(String gprsId){
        return PULSE_CMD_MAP.get(gprsId);
    }
    /**
     * 删除脉冲请求信息 (集群部署修改部分)
     * @param gprsId
     */
    public static void removePulseCMDInfo(String gprsId){
        PULSE_CMD_MAP.remove(gprsId);
    }
    /**
     * 获取均衡指令信息 (集群部署修改部分)
     * @param gprsId
     * @return
     */
    public static GprsBalanceSendInfo getBalanceSendInfo(String gprsId) {
        return BALANCE_SEND_MAP.get(gprsId);
    }
    /**
     * 保存均衡指令信息 (集群部署修改部分)
     * @param gprsId
     * @param info
     */
    public static void putBalanceSendInfo(String gprsId ,GprsBalanceSendInfo info) {
        BALANCE_SEND_MAP.put(gprsId,info);
    }
    /**
     * 删除均衡指令信息 (集群部署修改部分)
     * @param gprsId
     */
    public static void removeBalanceSendInfo(String gprsId){
        BALANCE_SEND_MAP.remove(gprsId);
    }
    /**
     * 获取均衡策略修改信息 (集群部署修改部分)
     * @param gprsId
     * @return
     */
    public static ModifyBalanceStrategyInfo getBalanceStrategyInfo(String gprsId) {
        return MODIFY_BALANCE_STRATEGY_MAP.get(gprsId);
    }
    /**
     * 保存均衡策略修改信息 (集群部署修改部分)
     * @param gprsId
     * @param info
     */
    public static void putBalanceStrategyInfo(String gprsId ,ModifyBalanceStrategyInfo info) {
        MODIFY_BALANCE_STRATEGY_MAP.put(gprsId,info);
    }
    /**
     * 删除均衡策略修改信息 (集群部署修改部分)
     * @param gprsId
     */
    public static void removeBalanceStrategyInfo(String gprsId){
        MODIFY_BALANCE_STRATEGY_MAP.remove(gprsId);
    }
    /**
     * 保存电池额定容量修改信息 (集群部署修改部分)
     * @param gprsId
     * @param info
     */
    public static void putModifyCapacityInfo(String gprsId,ModifyCapacityInfo info){
        MODIFY_CAPACITY_MAP.put(gprsId,info);
    }
    /**
     * 获取电池额定容量修改信息 (集群部署修改部分)
     * @param gprsId
     * @return
     */
    public static ModifyCapacityInfo getModifyCapacityInfo(String gprsId){
        return MODIFY_CAPACITY_MAP.get(gprsId);
    }
    /**
     * 删除电池额定容量修改信息 (集群部署修改部分)
     * @param gprsId
     */
    public static void removeModifyCapacityInfo(String gprsId){
        MODIFY_CAPACITY_MAP.remove(gprsId);
    }
    /**
     * 保存GprsId修改信息 (集群部署修改部分)
     * @param gprsId
     * @param info
     */
    public static void putModifyGprsIdInfo(String gprsId , ModifyGprsIdInfo info){
        MODIFY_GPRSID_MAP.put(gprsId,info);
    }
    /**
     * 获取GprsId修改信息 (集群部署修改部分)
     * @param gprsId
     * @return
     */
    public static ModifyGprsIdInfo getModifyGprsIdInfo(String gprsId){
        return MODIFY_GPRSID_MAP.get(gprsId);
    }
    /**
     * 删除GprsId修改信息 (集群部署修改部分)
     * @param gprsId
     */
    public static void removeModifyGprsIdInfo(String gprsId){
        MODIFY_GPRSID_MAP.remove(gprsId);
    }

    /**
     * 保存发送的均衡从机配置指令 (集群部署修改部分)
     * @param gprsId
     * @param info
     */
    public static void putSubBalanceConfigSend(String gprsId , SubBalanceConfigSend info){
        SUB_BALANCE_CONFIG_SEND_MAP.put(gprsId,info);
    }
    /**
     * 获取发送的均衡从机配置指令 (集群部署修改部分)
     * @param gprsId
     * @return
     */
    public static SubBalanceConfigSend getSubBalanceConfigSend(String gprsId){
        return SUB_BALANCE_CONFIG_SEND_MAP.get(gprsId);
    }
    /**
     * 删除发送的均衡从机配置指令 (集群部署修改部分)
     * @param gprsId
     */
    public static void removeSubBalanceConfigSend(String gprsId){
        SUB_BALANCE_CONFIG_SEND_MAP.remove(gprsId);
    }

    /**
     * 保存发送的均衡从机状态修改指令 (集群部署修改部分)
     * @param gprsId
     * @param info
     */
    public static void putSubBalanceStatusSend(String gprsId , SubBalanceStatusSend info){
        SUB_BALANCE_STATUS_SEND_MAP.put(gprsId,info);
    }
    /**
     * 获取发送的均衡从机状态修改指令 (集群部署修改部分)
     * @param gprsId
     * @return
     */
    public static SubBalanceStatusSend getSubBalanceStatusSend(String gprsId){
        return SUB_BALANCE_STATUS_SEND_MAP.get(gprsId);
    }
    /**
     * 删除发送的均衡从机状态修改指令 (集群部署修改部分)
     * @param gprsId
     */
    public static void removeSubBalanceStatusSend(String gprsId){
        SUB_BALANCE_STATUS_SEND_MAP.remove(gprsId);
    }

    public static void putGprsStateSnapshotByGprsId(String gprsId,GprsStateSnapshot gprsStateSnapshot){
        GPRS_STATE_SNAPSHOT_BYGPRSID.put(gprsId,gprsStateSnapshot);
    }

    public static GprsStateSnapshot getGprsStateSnapshotByGprsId(String gprsId){
        GprsStateSnapshot gprsStateSnapshot = GPRS_STATE_SNAPSHOT_BYGPRSID.get(gprsId);
        if (gprsStateSnapshot == null){
            HashMap<String,GprsStateSnapshot> map =
                    (HashMap<String,GprsStateSnapshot>) RedisUtil.get("gateway_station_state_gprsId");
            gprsStateSnapshot = map.get(gprsId);
        }
        return gprsStateSnapshot;
    }

    public static void putGprsStateSnapshotByStationId(Integer stationId,GprsStateSnapshot gprsStateSnapshot){
        if (stationId == null) return;
        GPRS_STATE_SNAPSHOT_BYSTATIONID.put(stationId,gprsStateSnapshot);
    }

    public static GprsStateSnapshot getGprsStateSnapshotByStationId(Integer stationId){
        GprsStateSnapshot gprsStateSnapshot = GPRS_STATE_SNAPSHOT_BYSTATIONID.get(stationId);
        if (gprsStateSnapshot == null){
            HashMap<Integer,GprsStateSnapshot> map =
                    (HashMap<Integer, GprsStateSnapshot>) RedisUtil.get("gateway_station_state_stationId");
            gprsStateSnapshot = map.get(stationId);
        }
        return gprsStateSnapshot;
    }



    public static ConcurrentHashMap<String, GprsConnectionInfo> getGprsMap() {
        return GPRS_MAP;
    }

    public static ConcurrentHashMap<String, Channel> getGprsChannelMap() {
        return GPRS_CHANNEL_MAP;
    }

    public static ConcurrentHashMap<String, SubpackageInfo> getSubpkginfoMap() {
        return SUBPKGINFO_MAP;
    }

    public static ConcurrentHashMap<String, PulseCMDInfo> getPulseCmdMap() {
        return PULSE_CMD_MAP;
    }

    public static ConcurrentHashMap<String, GprsBalanceSendInfo> getBalanceSendMap() {
        return BALANCE_SEND_MAP;
    }

    public static ConcurrentHashMap<String, ModifyCapacityInfo> getModifyCapacityMap() {
        return MODIFY_CAPACITY_MAP;
    }

    public static ConcurrentHashMap<String, ModifyBalanceStrategyInfo> getModifyBalanceStrategyMap() {
        return MODIFY_BALANCE_STRATEGY_MAP;
    }
    public static ConcurrentHashMap<String, ModifyGprsIdInfo> getModifyGprsidMap() {
        return MODIFY_GPRSID_MAP;
    }

    public static ConcurrentHashMap<String, SubBalanceConfigSend> getSubBalanceConfigSendMap() {
        return SUB_BALANCE_CONFIG_SEND_MAP;
    }

    public static ConcurrentHashMap<String, SubBalanceStatusSend> getSubBalanceStatusSendMap() {
        return SUB_BALANCE_STATUS_SEND_MAP;
    }
}
