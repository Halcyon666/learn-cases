package com.whalefall.learncases.design.generic;

/**
 * @author WhaleFall
 * @date 2022-07-26 20:06
 */
public interface I<E, T extends E> {
    T getInstance(E e);
}
