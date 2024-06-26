package com.whalefall.netty;

import com.whalefall.learncases.netty.client.ClientHandler;
import com.whalefall.learncases.netty.server.protobuf.MessageProtobuf;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import jakarta.annotation.Nullable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class NettyClientTests {

    private static EventLoopGroup group;
    private static ChannelFuture channelFuture;

    @BeforeAll
    public static void startClient() throws InterruptedException {
        group = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<>() {
                    @Override
                    protected void initChannel(@Nullable Channel ch) {
                        Assert.notNull(ch, "ch must not null");
                        ChannelPipeline p = ch.pipeline();
                        p.addLast("frameEncoder", new LengthFieldPrepender(2));
                        p.addLast(new ProtobufEncoder());
                        p.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
                        p.addLast(new ProtobufDecoder(MessageProtobuf.Msg.getDefaultInstance()));
                        p.addLast(new ClientHandler());
                    }
                });
        NettyServerTest.startServer();
        channelFuture = b.connect("127.0.0.1", 8080).sync();
        NettyServerTest.stopServer();
    }

    @AfterAll
    public static void stopClient() {
        group.shutdownGracefully();
    }

    @Test
    void testClientStart() {
        assertNotNull(channelFuture);
        assertNotNull(channelFuture.channel());
    }
}

