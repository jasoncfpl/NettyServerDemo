package com.netty.server;

import com.netty.server.decoder.MyDelimiterFrameDecoder;
import com.netty.server.handler.*;
import com.netty.server.model.PkgDataBean;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Demo {

    public static void main(String[] args) {
        Demo demo = new Demo();
        demo.startServer();
//        new Thread((Runnable) () -> {
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            demo.sendMsg(demo.getChannel());
//        }).start();


    }

    private void sendMsg(Channel channel) {
        for (int i = 0; i < 3; i++) {
            PkgDataBean bean = new PkgDataBean();
            bean.setCmd((byte) 0x05);
            bean.setData("粘包的数据：" + i);
            bean.setDataLength((byte) bean.getData().getBytes().length);
            channel.writeAndFlush(bean);
        }

    }

    private static final String TAG = "NettyServer";
    //端口
    private static final int PORT = 7010;
    private Channel channel;

    public Channel getChannel() {
        return channel;
    }

    //启动服务端
    public void startServer() {
        try {
            EventLoopGroup boosGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boosGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //添加发送数据编码
//                            pipeline.addLast("delimiter-encoder",new MyDelimiterFrameDecoder(65535));
                            pipeline.addLast("encoder",new ServerEncoder());
                            pipeline.addLast("decoder",new ServerDecoder());
                            pipeline.addLast("handler",new ServerHandler());
                            pipeline.addLast("after-handler",new AfterServerHandler());
                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(PORT).sync();
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    System.out.println("ChannelFuture operationComplete ：" + future.isSuccess());
                    if (future.isSuccess()) {
                        System.out.println("ChannelFuture 连接成功");
                    } else {
                        System.out.println("ChannelFuture 连接失败");
                    }
                }
            });
            channel = channelFuture.channel();
            System.out.println("====TCP服务器启动，端口:" + PORT + "====");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
