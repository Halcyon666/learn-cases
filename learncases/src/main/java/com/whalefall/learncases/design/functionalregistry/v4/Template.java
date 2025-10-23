package com.whalefall.learncases.design.functionalregistry.v4;

import com.whalefall.learncases.design.functionalregistry.v3.service.Business;

@FunctionalInterface
public interface Template<T, S extends Business<T>> {
    void handler(String txcode, T param, S businessService);
}
