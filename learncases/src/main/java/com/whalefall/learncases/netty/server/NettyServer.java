package com.whalefall.learncases.netty.server;


import com.whalefall.learncases.netty.server.handler.ServerHandler;
import com.whalefall.learncases.netty.server.protobuf.MessageProtobuf;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>@ProjectName:     BoChat</p>
 * <p>@ClassName:       NettyServerDemo.java</p>
 * <p>@PackageName:     com.bochat.im.netty</p>
 * <b>
 * <p>@Description:     TCP netty服务端</p>
 * </b>
 * <p>@author:          FreddyChen</p>
 * <p>@date:            2019/02/15 14:42</p>
 * <p>@email:           chenshichao@outlook.com</p>
 */
@Slf4j
public class NettyServer {

    private NettyServer() {
    }

    public static void start(int port) throws InterruptedException {

        // boss线程监听端口，worker线程负责数据读写
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            // 辅助启动类
            ServerBootstrap bootstrap = new ServerBootstrap();
            // 设置线程池
            bootstrap.group(boss, worker);

            // 设置socket工厂
            bootstrap.channel(NioServerSocketChannel.class);

            // 设置TCP参数
            // 1.链接缓冲池的大小（ServerSocketChannel的设置）
            // 维持链接的活跃，清除死链接(SocketChannel的设置)
            // 关闭延迟发送
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(@Nullable SocketChannel socketChannel) {
                    // 获取管道
                    assert socketChannel != null;
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    pipeline.addLast("frameEncoder", new LengthFieldPrepender(2))
                            .addLast(new ProtobufEncoder())
                            .addLast(new IdleStateHandler(30, 15, 0))
                            .addLast("frameDecoder",
                                    new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2))
                            .addLast(new ProtobufDecoder(MessageProtobuf.Msg.getDefaultInstance()))
                            .addLast(new ServerHandler());
                }
            });

            // 绑定端口
            ChannelFuture future = bootstrap.bind(port).sync();
            log.info("server start on port [{}]", port);

            // 等待服务端监听端口关闭
            future.channel().closeFuture().sync();
        } finally {
            // 优雅退出，释放线程池资源
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}

