package com.whalefall.learncases.threadlocal;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadLocalTest {
    private static final ThreadLocal<String> tl = new ThreadLocal<>();

    public static void main(String[] args) {
        new Thread(() -> {
            tl.set("123");
            log.info(tl.get());
        }).start();

        log.info(tl.get());
        tl.set("456");
        log.info(tl.get());
        tl.remove();
    }


}
