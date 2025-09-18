package com.whalefall.learncases.lambda.custom;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Halcyon
 * @since 2025/9/18 23:12
 */
@Component
@Slf4j
public class CustomImpl implements Custom {
    @Override
    public void custom(String str) {
        log.info("{}-{}", this.getClass().getSimpleName(), str);
    }
}
