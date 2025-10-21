package com.whalefall.learncases.design.functionalregistry.v1v2.template;

import java.util.function.Supplier;

@FunctionalInterface
public interface Template<T> {
    T handler(Supplier<T> businessFunction);
}
