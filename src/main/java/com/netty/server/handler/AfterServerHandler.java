package com.netty.server.handler;

import com.netty.server.model.PkgDataBean;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 数据处理
 */
public class AfterServerHandler extends SimpleChannelInboundHandler<PkgDataBean> {
    private static final String TAG = "AfterServerHandler";
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PkgDataBean msg) throws Exception {
//        System.out.println(TAG + "收到解码器处理过的数据：" + msg.toString());
//        System.out.println(TAG + "收到解码器处理过的数据 ctx：" + ctx.toString());
        ctx.fireChannelRead(msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
//        System.out.println(TAG + "channelActive：收到客户端连接:" + ctx.toString());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
//        System.out.println(TAG + "channelInactive：收到客户端断开连接" + ctx.toString());
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
//        System.out.println(TAG + "channelRegistered：" + ctx.toString());
    }
}
