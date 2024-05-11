package com.whalefall.learncases.netty.client;


import com.whalefall.learncases.netty.client.handler.HeartbeatHandler;
import com.whalefall.learncases.netty.client.handler.TCPChannelInitializerHandler;
import com.whalefall.learncases.netty.client.handler.TCPReadHandler;
import com.whalefall.learncases.netty.client.timeout.MsgTimeoutTimerManager;
import com.whalefall.learncases.netty.server.protobuf.MessageProtobuf;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.internal.StringUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * <p>@ProjectName:     NettyChat</p>
 * <p>@ClassName:       NettyTcpClient.java</p>
 * <p>@PackageName:     com.freddy.im.netty</p>
 * <b>
 * <p>@Description:     基于netty实现的tcp ims</p>
 * </b>
 * <p>@author:          FreddyChen</p>
 * <p>@date:            2019/03/31 20:41</p>
 * <p>@email:           chenshichao@outlook.com</p>
 */
@Slf4j
public class NettyTcpClient implements IMSClientInterface {

    private static volatile NettyTcpClient instance;

    private Bootstrap bootstrap;
    private Channel channel;

    private boolean isClosed = false;// 标识ims是否已关闭
    private ArrayList<String> serverUrlList;// ims服务器地址组
    private OnEventListener mOnEventListener;// 与应用层交互的listener
    private IMSConnectStatusCallback mIMSConnectStatusCallback;// ims连接状态回调监听器
    private MsgDispatcher msgDispatcher;// 消息转发器
    private ExecutorServiceFactory loopGroup;// 线程池工厂

    private boolean isReconnecting = false;// 是否正在进行重连
    private int connectStatus = IMSConfig.CONNECT_STATE_FAILURE;// ims连接状态，初始化为连接失败
    // 重连间隔时长
    private int reconnectInterval = IMSConfig.DEFAULT_RECONNECT_BASE_DELAY_TIME;
    // 连接超时时长
    private int connectTimeout = IMSConfig.DEFAULT_CONNECT_TIMEOUT;
    /**
     * -- GETTER --
     * 获取心跳间隔时间
     */
    // 心跳间隔时间
    @Getter
    private int heartbeatInterval = IMSConfig.DEFAULT_HEARTBEAT_INTERVAL_FOREGROUND;
    // 应用在后台时心跳间隔时间
    private int foregroundHeartbeatInterval = IMSConfig.DEFAULT_HEARTBEAT_INTERVAL_FOREGROUND;
    // 应用在前台时心跳间隔时间
    private int backgroundHeartbeatInterval = IMSConfig.DEFAULT_HEARTBEAT_INTERVAL_BACKGROUND;
    // app前后台状态
    private int appStatus = IMSConfig.APP_STATUS_FOREGROUND;
    // 消息发送超时重发次数
    private int resendCount = IMSConfig.DEFAULT_RESEND_COUNT;
    // 消息发送失败重发间隔时长
    private int resendInterval = IMSConfig.DEFAULT_RESEND_INTERVAL;

    private String currentHost = null;// 当前连接host
    private int currentPort = -1;// 当前连接port

    private MsgTimeoutTimerManager msgTimeoutTimerManager;// 消息发送超时定时器管理

    private NettyTcpClient() {
    }

    public static NettyTcpClient getInstance() {
        if (null == instance) {
            synchronized (NettyTcpClient.class) {
                if (null == instance) {
                    instance = new NettyTcpClient();
                }
            }
        }

        return instance;
    }

    @Override
    public void init(ArrayList<String> serverUrlList, OnEventListener listener, IMSConnectStatusCallback callback) {
        close();
        isClosed = false;
        this.serverUrlList = serverUrlList;
        this.mOnEventListener = listener;
        this.mIMSConnectStatusCallback = callback;
        msgDispatcher = new MsgDispatcher();
        msgDispatcher.setOnEventListener(listener);
        loopGroup = new ExecutorServiceFactory();
        loopGroup.initBossLoopGroup();// 初始化重连线程组
        msgTimeoutTimerManager = new MsgTimeoutTimerManager(this);

        resetConnect(true);// 进行第一次连接
    }


    /**
     * 重置连接，也就是重连
     * 首次连接也可认为是重连
     */
    @Override
    public void resetConnect() {
        this.resetConnect(false);
    }

    /**
     * 重置连接，也就是重连
     * 首次连接也可认为是重连
     * 重载
     *
     * @param isFirst 是否首次连接
     */
    @Override
    public void resetConnect(boolean isFirst) {
        if (!isFirst) {
            try {
                Thread.sleep(IMSConfig.DEFAULT_RECONNECT_INTERVAL);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }

        // 只有第一个调用者才能赋值并调用重连
        if (!isClosed && !isReconnecting) {
            synchronized (this) {
                if (!isClosed && !isReconnecting) {
                    // 标识正在进行重连
                    isReconnecting = true;
                    // 回调ims连接状态
                    onConnectStatusCallback(IMSConfig.CONNECT_STATE_CONNECTING);
                    // 先关闭channel
                    closeChannel();
                    // 执行重连任务
                    loopGroup.execBossTask(new ResetConnectRunnable(isFirst));
                }
            }
        }
    }

    /**
     * 关闭连接，同时释放资源
     */
    @Override
    public void close() {
        if (isClosed) {
            return;
        }

        isClosed = true;

        // 关闭channel
        try {
            closeChannel();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // 关闭bootstrap
        try {
            if (bootstrap != null) {
                bootstrap.group().shutdownGracefully();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            // 释放线程池
            if (loopGroup != null) {
                loopGroup.destroy();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (serverUrlList != null) {
                    serverUrlList.clear();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            isReconnecting = false;
            channel = null;
            bootstrap = null;
        }
    }

    /**
     * 标识ims是否已关闭
     *
     * @return
     */
    @Override
    public boolean isClosed() {
        return isClosed;
    }

    /**
     * 发送消息
     *
     * @param msg
     */
    @Override
    public void sendMsg(MessageProtobuf.Msg msg) {
        this.sendMsg(msg, true);
    }

    /**
     * 发送消息
     * 重载
     *
     * @param msg
     * @param isJoinTimeoutManager 是否加入发送超时管理器
     */
    @Override
    public void sendMsg(MessageProtobuf.Msg msg, boolean isJoinTimeoutManager) {
        if (msg == null) {
            log.error("发送消息失败，消息为空\tmessage={}", msg);
            return;
        }

        if (!StringUtil.isNullOrEmpty(msg.getHead().getMsgId()) && isJoinTimeoutManager) {
            msgTimeoutTimerManager.add(msg);
        }

        if (channel == null) {
            log.error("发送消息失败，channel为空\tmessage={}", msg);
        }

        try {
            assert channel != null;
            channel.writeAndFlush(msg);
        } catch (Exception ex) {
            log.error("发送消息失败，reason:{}\tmessage={}", ex.getMessage(), msg);
        }
    }

    /**
     * 获取重连间隔时长
     */
    @Override
    public int getReconnectInterval() {
        if (mOnEventListener != null) {
            int reconnectInterval1 = mOnEventListener.getReconnectInterval();
            if (reconnectInterval1 > 0) {
                return reconnectInterval1;
            }
        }

        return reconnectInterval;
    }

    /**
     * 获取连接超时时长
     */
    @Override
    public int getConnectTimeout() {
        if (mOnEventListener != null) {
            int timeout = mOnEventListener.getConnectTimeout();
            if (timeout > 0) {
                return timeout;
            }
        }

        return connectTimeout;
    }

    /**
     * 获取应用在前台时心跳间隔时间
     */
    @Override
    public int getForegroundHeartbeatInterval() {
        if (mOnEventListener != null && mOnEventListener.getForegroundHeartbeatInterval() > 0) {
            return mOnEventListener.getForegroundHeartbeatInterval();
        }

        return foregroundHeartbeatInterval;
    }

    /**
     * 获取应用在前台时心跳间隔时间
     */
    @Override
    public int getBackgroundHeartbeatInterval() {
        if (mOnEventListener != null && mOnEventListener.getBackgroundHeartbeatInterval() > 0) {
            return mOnEventListener.getBackgroundHeartbeatInterval();
        }

        return backgroundHeartbeatInterval;
    }

    /**
     * 设置app前后台状态
     */
    @Override
    public void setAppStatus(int appStatus) {
        this.appStatus = appStatus;
        if (this.appStatus == IMSConfig.APP_STATUS_FOREGROUND) {
            heartbeatInterval = foregroundHeartbeatInterval;
        } else if (this.appStatus == IMSConfig.APP_STATUS_BACKGROUND) {
            heartbeatInterval = backgroundHeartbeatInterval;
        }

        addHeartbeatHandler();
    }

    /**
     * 获取由应用层构造的握手消息
     */
    @Override
    public MessageProtobuf.Msg getHandshakeMsg() {
        if (mOnEventListener != null) {
            return mOnEventListener.getHandshakeMsg();
        }

        return null;
    }

    /**
     * 获取由应用层构造的心跳消息
     */
    @Override
    public MessageProtobuf.Msg getHeartbeatMsg() {
        if (mOnEventListener != null) {
            return mOnEventListener.getHeartbeatMsg();
        }

        return null;
    }

    /**
     * 获取应用层消息发送状态报告消息类型
     */
    @Override
    public int getServerSentReportMsgType() {
        if (mOnEventListener != null) {
            return mOnEventListener.getServerSentReportMsgType();
        }

        return 0;
    }

    /**
     * 获取应用层消息接收状态报告消息类型
     */
    @Override
    public int getClientReceivedReportMsgType() {
        if (mOnEventListener != null) {
            return mOnEventListener.getClientReceivedReportMsgType();
        }

        return 0;
    }

    /**
     * 获取应用层消息发送超时重发次数
     */
    @Override
    public int getResendCount() {
        if (mOnEventListener != null && mOnEventListener.getResendCount() != 0) {
            return mOnEventListener.getResendCount();
        }

        return resendCount;
    }

    /**
     * 获取应用层消息发送超时重发间隔
     */
    @Override
    public int getResendInterval() {
        if (mOnEventListener != null && mOnEventListener.getReconnectInterval() != 0) {
            return mOnEventListener.getResendInterval();
        }

        return resendInterval;
    }

    /**
     * 获取线程池
     */
    public ExecutorServiceFactory getLoopGroup() {
        return loopGroup;
    }

    /**
     * 获取消息转发器
     */
    @Override
    public MsgDispatcher getMsgDispatcher() {
        return msgDispatcher;
    }

    /**
     * 获取消息发送超时定时器
     */
    @Override
    public MsgTimeoutTimerManager getMsgTimeoutTimerManager() {
        return msgTimeoutTimerManager;
    }

    /**
     * 初始化bootstrap
     */
    private void initBootstrap() {
        EventLoopGroup loopGroup = new NioEventLoopGroup(4);
        bootstrap = new Bootstrap();
        bootstrap.group(loopGroup).channel(NioSocketChannel.class);
        // 设置该选项以后，如果在两小时内没有数据的通信时，TCP会自动发送一个活动探测数据报文
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        // 设置禁用nagle算法
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        // 设置连接超时时长
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, getConnectTimeout());
        // 设置初始化Channel
        bootstrap.handler(new TCPChannelInitializerHandler(this));
    }

    /**
     * 回调ims连接状态
     *
     * @param connectStatus
     */
    private void onConnectStatusCallback(int connectStatus) {
        this.connectStatus = connectStatus;
        switch (connectStatus) {
            case IMSConfig.CONNECT_STATE_CONNECTING: {
                log.info("ims连接中...");
                if (mIMSConnectStatusCallback != null) {
                    mIMSConnectStatusCallback.onConnecting();
                }
                break;
            }

            case IMSConfig.CONNECT_STATE_SUCCESSFUL: {
                log.info(String.format("ims连接成功，host『%s』, port『%s』", currentHost, currentPort));
                if (mIMSConnectStatusCallback != null) {
                    mIMSConnectStatusCallback.onConnected();
                }
                // 连接成功，发送握手消息
                MessageProtobuf.Msg handshakeMsg = getHandshakeMsg();
                if (handshakeMsg != null) {
                    log.info("发送握手消息，message={}", handshakeMsg);
                    sendMsg(handshakeMsg, false);
                } else {
                    log.error("请应用层构建握手消息！");
                }
                break;
            }

            case IMSConfig.CONNECT_STATE_FAILURE:
            default: {
                log.info("ims连接失败");
                if (mIMSConnectStatusCallback != null) {
                    mIMSConnectStatusCallback.onConnectFailed();
                }
                break;
            }
        }
    }

    /**
     * 添加心跳消息管理handler
     */
    public void addHeartbeatHandler() {
        if (channel == null || !channel.isActive() || channel.pipeline() == null) {
            return;
        }

        try {
            // 之前存在的读写超时handler，先移除掉，再重新添加
            if (channel.pipeline().get(IdleStateHandler.class.getSimpleName()) != null) {
                channel.pipeline().remove(IdleStateHandler.class.getSimpleName());
            }
            // 3次心跳没响应，代表连接已断开
            channel.pipeline().addFirst(IdleStateHandler.class.getSimpleName(), new IdleStateHandler(
                    heartbeatInterval * 3, heartbeatInterval, 0, TimeUnit.MILLISECONDS));

            // 重新添加HeartbeatHandler
            if (channel.pipeline().get(HeartbeatHandler.class.getSimpleName()) != null) {
                channel.pipeline().remove(HeartbeatHandler.class.getSimpleName());
            }
            if (channel.pipeline().get(TCPReadHandler.class.getSimpleName()) != null) {
                channel.pipeline().addBefore(TCPReadHandler.class.getSimpleName(), HeartbeatHandler.class.getSimpleName(),
                        new HeartbeatHandler(this));
            }
        } catch (Exception e) {
            log.error("添加心跳消息管理handler失败，reason：{}", e.getMessage());
        }
    }

    /**
     * 移除指定handler
     *
     * @param handlerName
     */
    private void removeHandler(String handlerName) {
        try {
            if (channel.pipeline().get(handlerName) != null) {
                channel.pipeline().remove(handlerName);
            }
        } catch (Exception e) {
            log.error("移除handler失败，handlerName={}", handlerName);
            /* Clean up whatever needs to be handled before interrupting  */
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 关闭channel
     */
    private void closeChannel() {
        try {
            if (channel != null) {
                try {
                    removeHandler(HeartbeatHandler.class.getSimpleName());
                    removeHandler(TCPReadHandler.class.getSimpleName());
                    removeHandler(IdleStateHandler.class.getSimpleName());
                } finally {
                    try {
                        channel.close();
                    } catch (Exception ex) {
                        log.error(ex.getMessage(), ex);
                    }
                    try {
                        channel.eventLoop().shutdownGracefully();
                    } catch (Exception ex) {
                        log.error(ex.getMessage(), ex);
                    }

                    channel = null;
                }
            }
        } catch (Exception ex) {
            log.error("关闭channel出错，reason:{}", ex.getMessage());
        }
    }

    /**
     * 从应用层获取网络是否可用
     *
     * @return
     */
    private boolean isNetworkAvailable() {
        if (mOnEventListener != null) {
            return mOnEventListener.isNetworkAvailable();
        }

        return false;
    }

    /**
     * 真正连接服务器的地方
     */
    private void toServer() {
        try {
            channel = bootstrap.connect(currentHost, currentPort).sync().channel();
        } catch (Exception e) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            log.error(String.format("连接Server(ip[%s], port[%s])失败", currentHost, currentPort));
            channel = null;
        }
    }

    /**
     * 重连任务
     */
    private class ResetConnectRunnable implements Runnable {

        private boolean isFirst;

        public ResetConnectRunnable(boolean isFirst) {
            this.isFirst = isFirst;
        }

        @Override
        public void run() {
            // 非首次进行重连，执行到这里即代表已经连接失败，回调连接状态到应用层
            if (!isFirst) {
                onConnectStatusCallback(IMSConfig.CONNECT_STATE_FAILURE);
            }

            try {
                // 重连时，释放工作线程组，也就是停止心跳
                loopGroup.destroyWorkLoopGroup();

                while (!isClosed) {
                    if (!isNetworkAvailable()) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            log.error(e.getMessage(), e);
                            /* Clean up whatever needs to be handled before interrupting  */
                            Thread.currentThread().interrupt();
                        }
                        continue;
                    }

                    // 网络可用才进行连接
                    int status;
                    if ((status = reConnect()) == IMSConfig.CONNECT_STATE_SUCCESSFUL) {
                        onConnectStatusCallback(status);
                        // 连接成功，跳出循环
                        break;
                    }

                    if (status == IMSConfig.CONNECT_STATE_FAILURE) {
                        onConnectStatusCallback(status);
                        try {
                            Thread.sleep(IMSConfig.DEFAULT_RECONNECT_INTERVAL);
                        } catch (InterruptedException e) {
                            log.error(e.getMessage(), e);
                            /* Clean up whatever needs to be handled before interrupting  */
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            } finally {
                // 标识重连任务停止
                isReconnecting = false;
            }
        }

        /**
         * 重连，首次连接也认为是第一次重连
         *
         * @return
         */
        private int reConnect() {
            // 未关闭才去连接
            if (!isClosed) {
                try {
                    // 先释放EventLoop线程组
                    if (bootstrap != null) {
                        bootstrap.config().group().shutdownGracefully();
                    }
                } finally {
                    bootstrap = null;
                }

                // 初始化bootstrap
                initBootstrap();
                return connectServer();
            }
            return IMSConfig.CONNECT_STATE_FAILURE;
        }

        /**
         * 连接服务器
         *
         * @return
         */
        private int connectServer() {
            // 如果服务器地址无效，直接回调连接状态，不再进行连接
            // 有效的服务器地址示例：127.0.0.1 8860
            if (serverUrlList == null || serverUrlList.isEmpty()) {
                return IMSConfig.CONNECT_STATE_FAILURE;
            }

            for (int i = 0; (!isClosed && i < serverUrlList.size()); i++) {
                String serverUrl = serverUrlList.get(i);
                // 如果服务器地址无效，直接回调连接状态，不再进行连接
                if (StringUtil.isNullOrEmpty(serverUrl)) {
                    return IMSConfig.CONNECT_STATE_FAILURE;
                }

                String[] address = serverUrl.split(" ");
                for (int j = 1; j <= IMSConfig.DEFAULT_RECONNECT_COUNT; j++) {
                    // 如果ims已关闭，或网络不可用，直接回调连接状态，不再进行连接
                    if (isClosed || !isNetworkAvailable()) {
                        return IMSConfig.CONNECT_STATE_FAILURE;
                    }

                    // 回调连接状态
                    if (connectStatus != IMSConfig.CONNECT_STATE_CONNECTING) {
                        onConnectStatusCallback(IMSConfig.CONNECT_STATE_CONNECTING);
                    }
                    log.info(String.format("正在进行『%s』的第『%d』次连接，当前重连延时时长为『%dms』", serverUrl, j, j * getReconnectInterval()));

                    try {
                        currentHost = address[0];// 获取host
                        currentPort = Integer.parseInt(address[1]);// 获取port
                        toServer();// 连接服务器

                        // channel不为空，即认为连接已成功
                        if (channel != null) {
                            return IMSConfig.CONNECT_STATE_SUCCESSFUL;
                        } else {
                            // 连接失败，则线程休眠n * 重连间隔时长
                            Thread.sleep((long) j * getReconnectInterval());
                        }
                    } catch (InterruptedException e) {
                        close();
                        Thread.currentThread().interrupt();
                        break;// 线程被中断，则强制关闭
                    }
                }
            }

            // 执行到这里，代表连接失败
            return IMSConfig.CONNECT_STATE_FAILURE;
        }
    }
}
