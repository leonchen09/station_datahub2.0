package com.walletech.server.handler;

import com.walletech.analysis.*;
import com.walletech.util.ProtocolUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {
    private static Logger logger = LoggerFactory.getLogger(ServerHandler.class);

    @Autowired
    private DeviceStatusTransAnalysis deviceStatusTranAnalysis;
    @Autowired
    private WarningInfoTranAnalysis warningInfoTranAnalysis;
    @Autowired
    private SubpkgRequestAnalysis subpkgRequestAnalysis;
    @Autowired
    private SubpkgDataAnalysis subpkgDataAnalysis;
    @Autowired
    private ACKAnalysis ackAnalysis;
    @Autowired
    private GprsDeviceReadAnalysis gprsDeviceReadAnalysis;
    @Autowired
    private TimeSyncAnalysis timeSyncAnalysis;
    @Autowired
    private ConnectAddrReadAnalysis connectAddrReadAnalysis;
    @Autowired
    private ResistanceTypeReadAnalysis resistanceTypeReadAnalysis;
    @Autowired
    private VersionBalanceAnalysis versionBalanceAnalysis;
    @Autowired
    private SalveVersionAnalysis salveVersionAnalysis;
    @Autowired
    private ConnectFailedAddrAnalysis connectFailedAddrAnalysis;
    @Autowired
    private ErrorACKAnalysis errorACKAnalysis;
    @Autowired
    private SubBalanceConfigAnalysis subBalanceConfigAnalysis;
    @Autowired
    private SubBalanceBaseStatusAnalysis subBalanceStatusAnalysis;
    @Autowired
    private SubBalanceSupportAnalysis subBalanceSupportAnalysis;

    /***
     * 打印传入服务器的数据 交由处理类处理
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String gprsId = null;
        ByteBuf buf = null;
        try {
            buf = (ByteBuf) msg;
            byte[] req =new byte[buf.readableBytes()];
            buf.readBytes(req);
            boolean ansIsReal = ProtocolUtil.isRealData(req);
            if (!ansIsReal) {
                logger.warn(" 未知数据,来源:[{}] ,数据 :[{}] ,关闭连接",ctx.channel().remoteAddress(),StringUtil.toHexString(req));
                ctx.close();
                return;
            }
            gprsId = ProtocolUtil.getGprsId(req);
            if (gprsId == null){
                logger.error("获取设备GprsID失败,数据 :[{}]",StringUtil.toHexString(req));
                return;
            }
            if (logger.isDebugEnabled()){
                StringBuilder sb = new StringBuilder();
                for (byte b : req) {
                    sb.append(Integer.toHexString(b & 0xff));
                    sb.append(" ");
                }
                logger.debug("接收到设备GprsID[{}],数据 :[{}]",gprsId,sb.toString());
            }
            ctx.fireChannelRead(gprsId);
            dealData(req,gprsId,ctx);
        } catch (Exception e) {
            logger.error("channelRead Exception", e);
        } finally {
            buf.release();
        }
    }

    /**
     *  根据数据类型分析处理数据
     * @param data
     */
    private void dealData(byte[] data,String gprsId,ChannelHandlerContext ctx){
        switch (data[3]){
            case (byte) 0x10: //设备状态帧 10H
                deviceStatusTranAnalysis.doAnalysis(data,gprsId,ctx);
                break;
            case (byte) 0xf5: //告警帧 F5H
                warningInfoTranAnalysis.doAnalysis(data,gprsId,ctx);
                break;
            case (byte) 0xf0: //保活连接帧 F0H
                byte[] response = new byte[] {(byte) 0x7f,(byte) 0xf7,(byte) 0x02 ,
                        (byte) 0xf0 , (byte) 0x00};
                response[4] = ProtocolUtil.bcc(response);
                ByteBuf buf = ctx.alloc().directBuffer(5);
                buf.writeBytes(response);
                ctx.writeAndFlush(buf);
                break;
            case (byte) 0xf1: //应答帧
                ackAnalysis.doAnalysis(data,gprsId,ctx);
                break;
            case (byte) 0xf2: //分包发送请求帧 F2H
                subpkgRequestAnalysis.doAnalysis(data,gprsId,ctx);
                break;
            case (byte) 0xf3: //分包数据帧 F3H
                subpkgDataAnalysis.doAnalysis(data,gprsId,ctx);
                break;
            case (byte) 0x11: //参数读取帧 11H
                gprsDeviceReadAnalysis.doAnalysis(data,gprsId,ctx);
                break;
            case (byte) 0x13: //时间同步帧 13H
                timeSyncAnalysis.doAnalysis(data,gprsId,ctx);
                break;
            case (byte) 0x26: //读取网络地址配置 26H
                connectAddrReadAnalysis.doAnalysis(data,gprsId,ctx);
                break;
            case (byte) 0x2b: //读取内阻计算参数 2bH
                resistanceTypeReadAnalysis.doAnalysis(data,gprsId,ctx);
                break;
            case (byte) 0x23: //远程读取主从机信息帧 23H
                versionBalanceAnalysis.doAnalysis(data,gprsId,ctx);
                break;
            case (byte) 0x24: //远程读取从机版本信息帧 24H
                salveVersionAnalysis.doAnalysis(data,gprsId,ctx);
                break;
            case (byte) 0x28: //错误连接地址帧 28H
                connectFailedAddrAnalysis.doAnalysis(data,gprsId,ctx);
                break;
            case (byte) 0xe0: //错误回复帧 e0H
                errorACKAnalysis.doAnalysis(data,gprsId,ctx);
                break;
            case (byte) 0x31: //读取均衡从机均衡参数帧 31H
                subBalanceConfigAnalysis.doAnalysis(data,gprsId,ctx);
                break;
            case (byte) 0x2f: //均衡从机实时数据 2FH
                subBalanceStatusAnalysis.doAnalysis(data,gprsId,ctx);
                break;
            case (byte) 0x34: //读取当前支持的均衡从机编号 34H
                subBalanceSupportAnalysis.doAnalysis(data,gprsId,ctx);
                break;
            case (byte) 0x2e: //均衡从机版本号 2eH
                logger.info("收到[{}]均衡从机版本信息 ,[{}]",gprsId,StringUtil.toHexString(data));
                break;
            default:
                logger.warn("[{}]消息类型不匹配,[{}]",gprsId,StringUtil.toHexString(data));
        }
    }

}
