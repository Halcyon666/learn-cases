package com.whalefall.learncases.netty.client.handler;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.whalefall.learncases.netty.client.NettyTcpClient;
import com.whalefall.learncases.netty.server.protobuf.MessageProtobuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * <p>@ProjectName:     NettyChat</p>
 * <p>@ClassName:       LoginAuthRespHandler.java</p>
 * <p>@PackageName:     com.freddy.im</p>
 * <b>
 * <p>@Description:     握手认证消息响应处理handler</p>
 * </b>
 * <p>@author:          FreddyChen</p>
 * <p>@date:            2019/04/07 23:11</p>
 * <p>@email:           chenshichao@outlook.com</p>
 */
@Slf4j
public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter {

    @SuppressWarnings("all")
    private NettyTcpClient imsClient;

    public LoginAuthRespHandler(NettyTcpClient imsClient) {
        this.imsClient = imsClient;
    }

    @Override
    public void channelRead(@Nullable ChannelHandlerContext ctx, @Nullable Object msg) {
        Assert.notNull(ctx, "ctx is null");
        Assert.notNull(msg, "msg is null");

        MessageProtobuf.Msg handshakeRespMsg = (MessageProtobuf.Msg) msg;
        if (handshakeRespMsg == null) {
            return;
        }

        MessageProtobuf.Msg handshakeMsg = imsClient.getHandshakeMsg();
        if (handshakeMsg == null) {
            return;
        }

        int handshakeMsgType = handshakeMsg.getHead().getMsgType();
        if (handshakeMsgType == handshakeRespMsg.getHead().getMsgType()) {
            log.info("收到服务端握手响应消息，message={}", handshakeRespMsg);
            int status = -1;
            try {
                JSONObject jsonObj = JSON.parseObject(handshakeRespMsg.getHead().getExtend());
                status = jsonObj.getIntValue("status");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                if (status == 1) {
                    // 握手成功，马上先发送一条心跳消息，至于心跳机制管理，交由HeartbeatHandler
                    MessageProtobuf.Msg heartbeatMsg = imsClient.getHeartbeatMsg();

                    Optional.of(heartbeatMsg).ifPresent(msg1 -> {
                        // 握手成功，检查消息发送超时管理器里是否有发送超时的消息，如果有，则全部重发
                        imsClient.getMsgTimeoutTimerManager().onResetConnected();

                        log.info("发送心跳消息：{}当前心跳间隔为：{}ms\n", msg1, imsClient.getHeartbeatInterval());
                        imsClient.sendMsg(msg1);

                        // 添加心跳消息管理handler
                        imsClient.addHeartbeatHandler();
                    });

                } else {
                    //imsClient.resetConnect(false);// 握手失败，触发重连
                    //握手失败且返回了消息一定是服务端认证没通过 所以这里需要关闭客户端
                    imsClient.close();
                }
            }
        } else {
            // 消息透传
            ctx.fireChannelRead(msg);
        }
    }
}
