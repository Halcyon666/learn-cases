package com.whalefall.learncases.generic;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

/**
 * @author WhaleFall
 * @date 2022-07-26 20:05
 */
@SuppressWarnings("all")
@Slf4j
public class A<T> {

    @SuppressWarnings("unchecked")
    public static <F> Collection<F> testWild1(Collection<F> c) {
        // ClassCastException class java.lang.Integer cannot be cast to class java.lang.String
        // c.add((F) new Integer("1"));
        c.add((F) "1231");
        return c;
    }

    public <E extends Number> void doLog(E e) {
        log.error(this.getClass() + " : " + e);
    }


}
