package com.whalefall.spi;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author Halcyon
 * @date 2024/5/25 15:53
 * @since 1.0.0
 */
@Slf4j
class SpiTests {
    @Test
    void testSpi() {

        List<Animal> animals = SpringFactoriesLoader.loadFactories(Animal.class,
                Thread.currentThread().getContextClassLoader());
        animals.forEach(s -> log.info(s.eat()));
        Assertions.assertFalse(CollectionUtils.isEmpty(animals), "Animal must not be null");
    }

}
