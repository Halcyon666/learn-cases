package com.whalefall.learncases.override;

import lombok.extern.slf4j.Slf4j;

/**
 * @author WhaleFall
 * @date 2022-06-29 21:54
 */
@SuppressWarnings("unused")
@Slf4j
public class Father {
    public static void work1() {
        extracted();
    }

    private static void extracted() {
        log.info("father work");
    }

    /**
     * 重载static方法
     *
     * @param s 入参
     */
    @SuppressWarnings("all")
    public static void work1(String s) {
        extracted1();
    }

    private static void extracted1() {
        log.info("son work");
    }

    public final void work() {
        extracted();
    }

    /**
     * 重载final方法
     *
     * @param s 入参
     */
    @SuppressWarnings("all")
    public final void work(String s) {
        extracted1();
    }

    @SuppressWarnings("all")
    public void print(final String s) {
        log.info("father print {}", s);
    }
}
