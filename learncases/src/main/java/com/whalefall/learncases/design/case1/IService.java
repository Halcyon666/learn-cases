package com.whalefall.learncases.design.case1;

/**
 * @author WhaleFall
 * @date 2022-07-25 19:21
 */
@SuppressWarnings("all")
public interface IService {
    boolean doService();

    default boolean doServiceBefore() {
        return true;
    }

    default boolean doServiceAfter() {
        return true;
    }

    default void doLog() {

    }
}
