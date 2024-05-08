package com.whalefall.learncases.netty.handler;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.whalefall.learncases.netty.protobuf.MessageProtobuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Halcyon
 * @since 1.0.0
 */
@Slf4j
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static final String TAG = ServerHandler.class.getSimpleName();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.info("ServerHandler channelActive{}", ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
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
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
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
                // todo 群聊，自己实现吧，toId可以是群id，根据群id查找所有在线用户的id，循环遍历channel发送即可。
                break;
            }

            default:
                break;
        }
    }

    public static class ChannelContainer {

        private static final ServerHandler.ChannelContainer INSTANCE = new ServerHandler.ChannelContainer();
        private final Map<String, ServerHandler.NettyChannel> CHANNELS = new ConcurrentHashMap<>();

        private ChannelContainer() {

        }

        public static ServerHandler.ChannelContainer getInstance() {
            return INSTANCE;
        }

        public void saveChannel(ServerHandler.NettyChannel channel) {
            if (channel == null) {
                return;
            }
            CHANNELS.put(channel.getChannelId(), channel);
        }

        public ServerHandler.NettyChannel removeChannelIfConnectNoActive(Channel channel) {
            if (channel == null) {
                return null;
            }

            String channelId = channel.id().toString();

            return removeChannelIfConnectNoActive(channelId);
        }

        public ServerHandler.NettyChannel removeChannelIfConnectNoActive(String channelId) {
            if (CHANNELS.containsKey(channelId) && !CHANNELS.get(channelId).isActive()) {
                return CHANNELS.remove(channelId);
            }

            return null;
        }

        public String getUserIdByChannel(Channel channel) {
            return getUserIdByChannel(channel.id().toString());
        }

        public String getUserIdByChannel(String channelId) {
            if (CHANNELS.containsKey(channelId)) {
                return CHANNELS.get(channelId).getUserId();
            }

            return null;
        }

        public ServerHandler.NettyChannel getActiveChannelByUserId(String userId) {
            for (Map.Entry<String, ServerHandler.NettyChannel> entry : CHANNELS.entrySet()) {
                if (entry.getValue().getUserId().equals(userId) && entry.getValue().isActive()) {
                    return entry.getValue();
                }
            }
            return null;
        }
    }

    public class NettyChannel {

        private String userId;
        private Channel channel;

        public NettyChannel(String userId, Channel channel) {
            this.userId = userId;
            this.channel = channel;
        }

        public String getChannelId() {
            return channel.id().toString();
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Channel getChannel() {
            return channel;
        }

        public void setChannel(Channel channel) {
            this.channel = channel;
        }

        public boolean isActive() {
            return channel.isActive();
        }
    }
}
