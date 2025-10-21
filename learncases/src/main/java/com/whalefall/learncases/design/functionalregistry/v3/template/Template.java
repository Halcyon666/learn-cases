package com.whalefall.learncases.design.functionalregistry.v3.template;

import com.whalefall.learncases.design.functionalregistry.v3.service.Business;

@FunctionalInterface
public interface Template<T, S extends Business<T>> {
    T handler(String txcode, T param, S businessService);
}
