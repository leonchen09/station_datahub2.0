package com.walletech.analysis;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author
 * @version 1.0
 * @description
 * @since
 */
public interface Analysis {
    void doAnalysis(byte[] data, String gprsId, ChannelHandlerContext ctx);
}
