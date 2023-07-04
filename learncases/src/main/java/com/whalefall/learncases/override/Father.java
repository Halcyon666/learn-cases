package com.whalefall.learncases.override;

import lombok.extern.slf4j.Slf4j;

/**
 * @author WhaleFall
 * @date 2022-06-29 21:54
 */
@Slf4j
public class Father {
    public static void work1() {
        log.info("father work");
    }

    /**
     * 重载static方法
     *
     * @param s 入参
     */
    public static void work1(String s) {
        log.info("son work");
    }

    public final void work() {
        log.info("father work");
    }

    /**
     * 重载final方法
     *
     * @param s 入参
     */
    public final void work(String s) {
        log.info("son work");
    }

    public void print(final String s) {
        log.info("father print");
    }
}
