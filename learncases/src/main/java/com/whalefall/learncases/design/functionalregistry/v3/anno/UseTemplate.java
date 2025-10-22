package com.whalefall.learncases.design.functionalregistry.v3.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface UseTemplate {
    @SuppressWarnings("all")
    Class<?> value();
}
