package com.whalefall.learncases.lambda.custom;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * @author Halcyon
 * @since 2025/9/18 23:05
 */
@Slf4j
@Component
public class CustomUsed {
    @SuppressWarnings("unused")
    public static void customUse() {
        Consumer<Custom> consumer = custom -> custom.custom("Hello");
        consumer.accept(log::info);
    }

    @SuppressWarnings("unused")
    public static void customUse(Consumer<String> consumer, boolean flag) {
        if (flag) {
            consumer.accept(Custom.class.getSimpleName());
        }
    }

    @SuppressWarnings("all")
    public static void main(String[] args) {
        // customUse(log::info, true);
        // customUse();
    }

    @Resource
    private Custom customImpl;

    /**
     * 在Jar包中定义{@link FunctionalInterface}<br/>
     * 当然Jar中的使用类是实现ApplicationContextAware,然后拿到对应用注入对象，接着写好函数在哪里触发<br/>
     * 用户定义实现类，并注入到容器中。<br/>
     * @param str function入参
     */
    public void custom(String str) {
        Consumer<Custom> consumer = custom -> custom.custom(str);
        consumer.accept(customImpl);
    }

}
