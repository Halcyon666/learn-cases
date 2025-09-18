package com.whalefall.learncases.lambda.custom;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Halcyon
 * @since 2025/9/18 23:17
 */
@RestController
public class CustomController {
    @Resource
    private CustomUsed customUsed;
    @GetMapping("/testCustImpl")
    public String testCustImpl() {
        customUsed.custom("controller called testCustImpl");
        return "testCustImpl";
    }
}
