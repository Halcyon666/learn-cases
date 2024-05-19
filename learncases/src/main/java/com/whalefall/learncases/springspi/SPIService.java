package com.whalefall.learncases.springspi;

import com.whalefall.spi.Animal;
import jakarta.annotation.Resource;
import lombok.Getter;
import org.springframework.stereotype.Component;

/**
 * @author Halcyon
 * @date 2023/6/30
 * @since 1.0.0
 */
@Getter
@Component
public class SPIService {
    @Resource
    private Animal dog;

}
