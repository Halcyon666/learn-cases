package com.whalefall.spi;

import org.springframework.stereotype.Component;

/**
 * @author Halcyon
 * @date 2023/6/29
 * @since 1.0.0
 */
@Component
public class Dog implements Animal {
    @Override
    public String eat() {
        return "the dog is eating";
    }
}
