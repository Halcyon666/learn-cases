package com.whalefall.learncases.netty.server.task;

import com.whalefall.learncases.netty.server.NettyServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Halcyon
 * @since 1.0.0
 */
@Component
@Slf4j
public class NettyServerTask {
    @Scheduled(fixedRate = 5000) // 每隔5秒执行一次
    public void startNettyServer() {
        // 执行定时任务的逻辑
        try {
            NettyServer.start(5555);
        } catch (InterruptedException e) {
            log.error("launch netty server occurs exception {} ", e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
    }
}
