package com.whalefall.learncases.netty.client;

/**
 * @author Halcyon
 * @date 2024/6/8 9:21
 * @since 1.0.0
 */
import com.whalefall.learncases.netty.server.protobuf.MessageProtobuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("ClientHandler channelActive");

        // 发送握手消息
        MessageProtobuf.Msg.Builder msgBuilder = MessageProtobuf.Msg.newBuilder();
        MessageProtobuf.Head.Builder headBuilder = MessageProtobuf.Head.newBuilder();
        headBuilder.setMsgId("1");
        headBuilder.setMsgType(1001);
        headBuilder.setFromId("client1");
        headBuilder.setExtend("{\"token\":\"token_client1\"}");

        msgBuilder.setHead(headBuilder);
        msgBuilder.setBody("Hello Server!");

        ctx.writeAndFlush(msgBuilder.build());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        MessageProtobuf.Msg message = (MessageProtobuf.Msg) msg;
        log.info("收到来自服务器的消息：{}", message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("ClientHandler exceptionCaught", cause);
        ctx.close();
    }
}
