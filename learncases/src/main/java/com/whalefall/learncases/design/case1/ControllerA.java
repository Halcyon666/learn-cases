package com.whalefall.learncases.design.case1;

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
    private final ServiceA serviceA;

    public ControllerA(ServiceA serviceA) {
        this.serviceA = serviceA;
    }

    /**
     * 测试A服务接口
     * <p>
     * GET /testA
     * <p>
     * 功能说明：调用ServiceA的handle方法执行业务逻辑，通过异常处理器捕获并记录异常信息
     *
     * @return boolean 表示业务执行是否成功
     */
    @GetMapping("/testA")
    public boolean getA() {
        return serviceA.handle(e -> log.error(e.getMessage(), e));
    }
}
