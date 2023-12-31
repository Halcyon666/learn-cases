package com.whalefall.learncases.design;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 使用二次封装的AService <br/>
 * 而不是直接引入ProcessService
 *
 * @author WhaleFall
 * @date 2022-07-24 6:37
 */
@RestController
@Slf4j
public class ControllerA {
    private final AService aService;

    public ControllerA(AService aService) {
        this.aService = aService;
    }

    @GetMapping("/testA")
    public boolean getA() {
        return aService.handle(e -> log.error(e.getMessage(), e));
    }
}
