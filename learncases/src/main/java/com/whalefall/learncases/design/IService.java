package com.whalefall.learncases.design;

/**
 * @author WhaleFall
 * @date 2022-07-25 19:21
 */
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
