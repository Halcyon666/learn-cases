package com.whalefall.learncases.netty.server.handler;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.whalefall.learncases.netty.server.protobuf.MessageProtobuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Halcyon
 * @since 1.0.0
 */
@Slf4j
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(@Nullable ChannelHandlerContext ctx) throws Exception {
        Assert.notNull(ctx, "ctx is null");
        super.channelActive(ctx);
        log.info("ServerHandler channelActive{}", ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(@Nullable ChannelHandlerContext ctx) throws Exception {
        Assert.notNull(ctx, "ctx is null");
        super.channelInactive(ctx);
        log.info("ServerHandler channelInactive{}", ctx.channel().remoteAddress());
        // 用户断开连接后，移除channel
        ServerHandler.ChannelContainer.getInstance().removeChannelIfConnectNoActive(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        log.info("ServerHandler exceptionCaught()");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        log.info("ServerHandler userEventTriggered()");
    }

    @Override
    public void channelRead(@Nullable ChannelHandlerContext ctx, @Nullable Object msg) {
        Assert.notNull(ctx, "ctx not null");
        Assert.notNull(msg, "msg not null");
        MessageProtobuf.Msg message = (MessageProtobuf.Msg) msg;
        log.info("收到来自客户端的消息：{}", message);
        int msgType = message.getHead().getMsgType();
        switch (msgType) {
            // 握手消息
            case 1001: {
                String fromId = message.getHead().getFromId();
                JSONObject jsonObj = JSON.parseObject(message.getHead().getExtend());
                String token = jsonObj.getString("token");
                JSONObject resp = new JSONObject();
                if (token.equals("token_" + fromId)) {
                    resp.put("status", 1);
                    // 握手成功后，保存用户通道
                    ServerHandler.ChannelContainer.getInstance().saveChannel(new ServerHandler.NettyChannel(fromId, ctx.channel()));
                } else {
                    resp.put("status", -1);
                    ServerHandler.ChannelContainer.getInstance().removeChannelIfConnectNoActive(ctx.channel());
                }

                message = message.toBuilder().setHead(message.getHead().toBuilder().setExtend(resp.toString()).build()).build();
                ServerHandler.ChannelContainer.getInstance().getActiveChannelByUserId(fromId).getChannel().writeAndFlush(message);
                break;
            }

            // 心跳消息
            case 1002: {
                // 收到心跳消息，原样返回
                String fromId = message.getHead().getFromId();
                ServerHandler.ChannelContainer.getInstance().getActiveChannelByUserId(fromId).getChannel().writeAndFlush(message);
                break;
            }


            case 2001: {
                // 收到2001或3001消息，返回给客户端消息发送状态报告
                String fromId = message.getHead().getFromId();
                MessageProtobuf.Msg.Builder sentReportMsgBuilder = MessageProtobuf.Msg.newBuilder();
                MessageProtobuf.Head.Builder sentReportHeadBuilder = MessageProtobuf.Head.newBuilder();
                sentReportHeadBuilder.setMsgId(message.getHead().getMsgId());
                sentReportHeadBuilder.setMsgType(1010);
                sentReportHeadBuilder.setTimestamp(System.currentTimeMillis());
                sentReportHeadBuilder.setStatusReport(1);
                sentReportMsgBuilder.setHead(sentReportHeadBuilder.build());
                ServerHandler.ChannelContainer.getInstance().getActiveChannelByUserId(fromId).getChannel().writeAndFlush(sentReportMsgBuilder.build());

                // 同时转发消息到接收方
                String toId = message.getHead().getToId();
                ServerHandler.ChannelContainer.getInstance().getActiveChannelByUserId(toId).getChannel().writeAndFlush(message);
                break;
            }

            case 3001: {
                break;
            }

            default:
                break;
        }
    }

    @SuppressWarnings("all")
    public static class ChannelContainer {

        private static final ServerHandler.ChannelContainer INSTANCE = new ServerHandler.ChannelContainer();
        private final Map<String, ServerHandler.NettyChannel> channels = new ConcurrentHashMap<>();

        private ChannelContainer() {
        }

        public static ServerHandler.ChannelContainer getInstance() {
            return INSTANCE;
        }

        public void saveChannel(ServerHandler.NettyChannel channel) {
            if (channel == null) {
                return;
            }
            channels.put(channel.getChannelId(), channel);
        }

        public void removeChannelIfConnectNoActive(Channel channel) {
            if (channel == null) {
                return;
            }

            removeChannelIfConnectNoActive(channel.id().toString());
        }

        public void removeChannelIfConnectNoActive(String channelId) {
            if (channels.containsKey(channelId) && !channels.get(channelId).isActive()) {
                channels.remove(channelId);
            }

        }

        public String getUserIdByChannel(Channel channel) {
            return getUserIdByChannel(channel.id().toString());
        }

        public String getUserIdByChannel(String channelId) {
            if (channels.containsKey(channelId)) {
                return channels.get(channelId).getUserId();
            }

            return null;
        }

        public ServerHandler.NettyChannel getActiveChannelByUserId(String userId) {
            for (Map.Entry<String, ServerHandler.NettyChannel> entry : channels.entrySet()) {
                if (entry.getValue().getUserId().equals(userId) && entry.getValue().isActive()) {
                    return entry.getValue();
                }
            }
            return null;
        }
    }

    @Data
    public static class NettyChannel {

        private String userId;
        private Channel channel;

        public NettyChannel(String userId, Channel channel) {
            this.userId = userId;
            this.channel = channel;
        }

        public String getChannelId() {
            return channel.id().toString();
        }


        public boolean isActive() {
            return channel.isActive();
        }
    }
}
