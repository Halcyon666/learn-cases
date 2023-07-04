package com.whalefall.learncases.design;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 直接使用 ProcessService 不进行二次封装
 *
 * @author WhaleFall
 * @date 2022-07-24 6:34
 */
@Slf4j
@RestController
public class ControllerB implements IService {
    /**
     * 构造方法注入时，变量名会首先默认匹配类名；
     * 如果不如任何类名相符则会报错；
     */
    private final IProcessService newProcessService;

    public ControllerB(IProcessService newProcessService) {
        this.newProcessService = newProcessService;
    }

    @GetMapping("testB")
    public String test() {
        newProcessService.handle(this, e -> log.error(e.getMessage(), e));
        return "testb";
    }

    @Override
    public boolean doService() {
        log.info("do B service");
        if (true)
            throw new RuntimeException("B Exception msg1");
        return true;
    }

    @Override
    public void doLog() {
        if (1 == 1)
            throw new RuntimeException("B Exception msg2");
        log.error("B do log");
    }
}
