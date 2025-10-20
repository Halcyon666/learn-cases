package com.whalefall.learncases.design.functionalregistry.service;

/**
 * @author Halcyon
 * @since 2025/10/20 22:28
 */
public interface Job<T> {
    T doJob(T t);
}
