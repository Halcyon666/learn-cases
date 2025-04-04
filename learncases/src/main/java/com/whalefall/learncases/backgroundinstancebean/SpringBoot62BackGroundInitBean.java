package com.whalefall.learncases.backgroundinstancebean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import static org.springframework.context.ConfigurableApplicationContext.BOOTSTRAP_EXECUTOR_BEAN_NAME;

/**
 * @author Halcyon
 * @date 2025/4/4 14:36
 * @since 1.0.0
 */
@Configuration
public class SpringBoot62BackGroundInitBean {
    @Bean(bootstrap = Bean.Bootstrap.BACKGROUND)
    public String a() {
        return "a";
    }

    @Bean(BOOTSTRAP_EXECUTOR_BEAN_NAME)
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数 - 线程池创建时初始化的线程数
        executor.setCorePoolSize(10);
        // 最大线程数 - 线程池最大的线程数，只有在队列满了之后才会创建超出核心线程数的线程
        executor.setMaxPoolSize(20);
        // 队列容量 - 用于保存等待执行的任务的队列容量
        executor.setQueueCapacity(500);
        // 线程前缀名 - 创建线程的名称前缀
        executor.setThreadNamePrefix(BOOTSTRAP_EXECUTOR_BEAN_NAME);
        // 设置线程保持活动的时间（秒）- 当超过核心线程数的线程空闲时间达到该值时会被销毁
        executor.setKeepAliveSeconds(60);
        // 拒绝策略 - 当队列和线程池都满了时的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 初始化线程池
        executor.initialize();
        return executor;
    }
}
