package com.whalefall.netty;

import com.whalefall.learncases.netty.server.handler.ServerHandler;
import com.whalefall.learncases.netty.server.protobuf.MessageProtobuf;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import jakarta.annotation.Nullable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class NettyServerTest {

    private static EventLoopGroup bossGroup;
    private static EventLoopGroup workerGroup;
    private static ChannelFuture channelFuture;

    @BeforeAll
    public static void startServer() throws InterruptedException {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();

        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<>() {
                    @Override
                    protected void initChannel(@Nullable Channel ch) {
                        Assert.notNull(ch, "ch must not null");
                        ChannelPipeline p = ch.pipeline();
                        p.addLast("frameEncoder", new LengthFieldPrepender(2));
                        p.addLast(new ProtobufEncoder());
                        p.addLast(new IdleStateHandler(30, 15, 0));
                        p.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
                        p.addLast(new ProtobufDecoder(MessageProtobuf.Msg.getDefaultInstance()));
                        p.addLast(new ServerHandler());
                    }
                });

        channelFuture = b.bind(8080).sync();
    }

    @AfterAll
    public static void stopServer() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    @Test
    void testServerStart() {
        assertNotNull(channelFuture);
        assertNotNull(channelFuture.channel());
    }
}


