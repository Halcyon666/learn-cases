package com.whalefall.learncases.design.functionalregistry.template;

import java.util.function.Supplier;

@FunctionalInterface
public interface Template<T> {
    T handler(Supplier<T> businessFunction);
}
