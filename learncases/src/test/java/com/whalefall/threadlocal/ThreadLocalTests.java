package com.whalefall.threadlocal;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Halcyon
 * @date 2024/6/9 10:09
 * @since 1.0.0
 */
@Slf4j
class ThreadLocalTests {

    private static final ThreadLocal<String> tl = new ThreadLocal<>();

    @Test
    void test() throws InterruptedException {
        Thread thread = new Thread(() -> {
            tl.set("123");
            Assertions.assertThat(tl.get()).isEqualTo("123");
            log.info(tl.get());
        });
        thread.start();
        thread.join();
        Assertions.assertThat(tl.get()).isNull();
        tl.set("456");
        Assertions.assertThat(tl.get()).isEqualTo("456");
        tl.remove();
    }
}
