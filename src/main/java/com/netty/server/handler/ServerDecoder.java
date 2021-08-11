package com.netty.server.handler;

import com.netty.server.model.PkgDataBean;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.Arrays;
import java.util.List;

/**
 * 数据解码器
 */
public class ServerDecoder extends ByteToMessageDecoder {
    private static final String TAG = "ServerDecoder";

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println(TAG + "decode");
//        //收到的数据长度
//        int length = in.readableBytes();
//        //创建 ByteBuf存储接收到的数据 --TODO Unpooled是什么意思
//        ByteBuf byteBuf = Unpooled.buffer(length);
//        in.readBytes(byteBuf);
//
//        String data1 = Arrays.toString(byteBuf.array());
//        String data = new String(byteBuf.array());
//        System.out.println(TAG + "--data:" + data);
//        System.out.println(TAG + "--data1:" + data1);
//
//        out.add(data);
        decodePkgDataBean(ctx, in, out);
    }

    private void decodePkgDataBean (ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        int length = in.readableBytes();
        //收到的数据包
//        byte[] data = in.readBytes(length).array();
        ByteBuf byteBuf = Unpooled.buffer(length);
        in.readBytes(byteBuf);
        byte[] data = byteBuf.array();
        //判断数据包是不是一个正确的数据包
        if (data[0] == 0x2A && data[0] == data[data.length - 1]) {
            PkgDataBean bean = new PkgDataBean();
            bean.setCmd(data[1]);
            bean.setDataLength(data[2]);
            byte[] bytes = Arrays.copyOfRange(data, 3, 3 + bean.getDataLength());
            bean.setData(new String(bytes));

            out.add(bean);
        }
        System.out.println(TAG + "收到了客户端发送的数据：" + new String(data));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);

        System.out.println("exceptionCaught:" + cause.getCause());
    }
}
