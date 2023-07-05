package com.whalefall.spi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

/**
 * @author Halcyon
 * @date 2023/6/30
 * @since 1.0.0
 */

@Component
@Slf4j
public class TestJarComponentScan {
    @PostConstruct
    public void init() {
        log.debug("TestJarComponentScan init");
    }

    public String doSome() {
        return "TestJarComponentScan doSome";
    }
}
