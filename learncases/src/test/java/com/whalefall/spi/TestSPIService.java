package com.whalefall.spi;

import com.whalefall.AbstractTest;
import com.whalefall.learncases.springspi.SPIService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Halcyon
 * @date 2024/5/19
 * @since 1.0.0
 */
class TestSPIService extends AbstractTest {
    @Resource
    private SPIService spiService;


    @Test
    void testSPIService() {
        Animal dog = spiService.getDog();
        Assertions.assertNotNull(dog, "SPI inject Dog must be not null");
    }

}
