package com.whalefall.spi;

import com.whalefall.AbstractLearncasesApplicationTests;
import com.whalefall.learncases.springspi.SPIService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Halcyon
 * @date 2024/5/19
 * @since 1.0.0
 */
class TestSPIService extends AbstractLearncasesApplicationTests {
    @Resource
    private SPIService spiService;


    @Test
    void testSPIService() {
        Animal dog = spiService.getDog();
        Assertions.assertNotNull(dog, "SPI inject Dog must be not null");
    }

}
