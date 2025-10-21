package com.whalefall.learncases.design.functionalregistry.v1v2.service;

/**
 * @author Halcyon
 * @since 2025/10/20 22:28
 */
public interface Business<T> {
    T doJob(T t);
}
