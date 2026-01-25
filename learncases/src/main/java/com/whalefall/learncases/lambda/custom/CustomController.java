package com.whalefall.learncases.lambda.custom;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 自定义功能控制器
 *
 * @author Halcyon
 * @since 2025/9/18 23:17
 */
@RestController
public class CustomController {
    @Resource
    private CustomUsed customUsed;

    /**
     * 测试自定义实现接口
     * <p>
     * GET /testCustImpl
     * <p>
     * 功能说明：调用CustomUsed的custom方法执行自定义业务逻辑，并返回测试结果
     *
     * @return String 返回"testCustImpl"字符串作为响应
     */
    @GetMapping("/testCustImpl")
    public String testCustImpl() {
        customUsed.custom("controller called testCustImpl");
        return "testCustImpl";
    }
}
