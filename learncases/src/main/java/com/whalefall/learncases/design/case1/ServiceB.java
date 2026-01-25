
package com.whalefall.learncases.design.case1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * B服务控制器
 * <p>
 * 直接使用ProcessService不进行二次封装的实现方式
 *
 * @author WhaleFall
 * @date 2022-07-24 6:34
 */
@Slf4j
@RestController
public class ServiceB implements IService {
    /**
     * 构造方法注入时，变量名会首先默认匹配类名；
     * 如果不如任何类名相符则会报错；
     */
    private final IProcessService newProcessService;

    public ServiceB(IProcessService newProcessService) {
        this.newProcessService = newProcessService;
    }

    /**
     * 测试B服务接口
     * <p>
     * GET /testB
     * <p>
     * 功能说明：调用ProcessService执行B服务业务逻辑，通过异常处理器捕获并记录异常信息
     * 该服务实现了IService接口，包含doService和doLog方法
     *
     * @return String 返回"testb"字符串作为响应
     */
    @GetMapping("testB")
    public String test() {
        newProcessService.handle(this, e -> log.error(e.getMessage(), e));
        return "testb";
    }

    @SuppressWarnings("all")
    @Override
    public boolean doService() {
        log.info("do B service");
        if (true)
            throw new RuntimeException("B Exception msg1");
        return true;
    }

}
