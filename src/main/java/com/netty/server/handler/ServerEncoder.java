package com.netty.server.handler;

import com.netty.server.model.PkgDataBean;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 数据编码器
 */
public class ServerEncoder extends MessageToByteEncoder<PkgDataBean> {
    private static final String TAG = "ServerEncoder";
    @Override
    protected void encode(ChannelHandlerContext ctx, PkgDataBean data, ByteBuf byteBuf) throws Exception {
        //将要发送的数据进行编码
//        out.writeBytes(msg.toString().getBytes());

        //根据数据包协议，生成byte数组
        byte[] bytes = {0x2A, data.getCmd(), data.getDataLength()};
        byte[] dataBytes = data.getData().getBytes();
        //分隔符
        byte[] delimiter = "$".getBytes();
        //将所有数据合并成一个byte数组
        byte[] all = byteMergerAll(bytes, dataBytes, new byte[]{0x2A}, delimiter);
        //发送数据
        byteBuf.writeBytes(all);
    }

    private static byte[] byteMergerAll(byte[]... values) {
        int length_byte = 0;
        for (int i = 0; i < values.length; i++) {
            length_byte += values[i].length;
        }
        byte[] all_byte = new byte[length_byte];
        int countLength = 0;
        for (int i = 0; i < values.length; i++) {
            byte[] b = values[i];
            System.arraycopy(b, 0, all_byte, countLength, b.length);
            countLength += b.length;
        }
        return all_byte;
    }
}
