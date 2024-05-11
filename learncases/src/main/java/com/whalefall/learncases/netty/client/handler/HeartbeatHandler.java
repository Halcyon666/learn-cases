package com.whalefall.learncases.netty.client.handler;


import com.whalefall.learncases.netty.client.NettyTcpClient;
import com.whalefall.learncases.netty.server.protobuf.MessageProtobuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>@ProjectName:     NettyChat</p>
 * <p>@ClassName:       HeartbeatHandler.java</p>
 * <p>@PackageName:     com.freddy.im</p>
 * <b>
 * <p>@Description:     心跳任务管理器</p>
 * </b>
 * <p>@author:          FreddyChen</p>
 * <p>@date:            2019/04/08 01:34</p>
 * <p>@email:           chenshichao@outlook.com</p>
 */
@Slf4j
public class HeartbeatHandler extends ChannelInboundHandlerAdapter {

    private final NettyTcpClient imsClient;
    private HeartbeatTask heartbeatTask;

    public HeartbeatHandler(NettyTcpClient imsClient) {
        this.imsClient = imsClient;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        // This pattern is introduced in Java 14 as a preview feature and becomes a standard feature in Java 16.
        if (evt instanceof IdleStateEvent idlestateevent) {
            IdleState state = idlestateevent.state();
            switch (state) {
                case READER_IDLE: {
                    // 规定时间内没收到服务端心跳包响应，进行重连操作
                    imsClient.resetConnect(false);
                    break;
                }

                case WRITER_IDLE: {
                    // 规定时间内没向服务端发送心跳包，即发送一个心跳包
                    if (heartbeatTask == null) {
                        heartbeatTask = new HeartbeatTask(ctx);
                    }

                    imsClient.getLoopGroup().execWorkTask(heartbeatTask);
                    break;
                }
                default:
                    break;
            }
        }
    }

    private class HeartbeatTask implements Runnable {

        private final ChannelHandlerContext ctx;

        public HeartbeatTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            if (ctx.channel().isActive()) {
                MessageProtobuf.Msg heartbeatMsg = imsClient.getHeartbeatMsg();
                if (heartbeatMsg == null) {
                    return;
                }
                log.info("发送心跳消息，message={}当前心跳间隔为：{}ms\n", heartbeatMsg, imsClient.getHeartbeatInterval());
                imsClient.sendMsg(heartbeatMsg, false);
            }
        }
    }
}
