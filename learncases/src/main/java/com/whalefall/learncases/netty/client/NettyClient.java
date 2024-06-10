package com.whalefall.learncases.netty.client;


import com.whalefall.learncases.netty.server.protobuf.MessageProtobuf;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

@Slf4j
@SuppressWarnings("unused")
public class NettyClient {
    private NettyClient() {
    }

    public static void start() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(@Nullable SocketChannel socketChannel) {
                            Assert.notNull(socketChannel, "socketChannel cannot be null");
                            socketChannel.pipeline().addLast("frameEncoder", new LengthFieldPrepender(2))
                                    .addLast(new ProtobufEncoder())
                                    .addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2))
                                    .addLast(new ProtobufDecoder(MessageProtobuf.Msg.getDefaultInstance()))
                                    .addLast(new ClientHandler());
                        }
                    });

            // 连接服务器
            bootstrap.connect("127.0.0.1", 5555).sync().channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}

