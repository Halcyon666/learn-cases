package com.whalefall.learncases.springspi;

import com.whalefall.spi.Animal;
import com.whalefall.spi.TestJarComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;

/**
 * @author Halcyon
 * @date 2023/6/30
 * @since 1.0.0
 */
@RestController
public class TestSPIController {
    @Resource
    private Animal dog;
    @Resource
    private TestJarComponentScan componentScan;

    @GetMapping("springspi")
    public String test() {
        return dog.eat() + " " + componentScan.doSome();
    }
}
