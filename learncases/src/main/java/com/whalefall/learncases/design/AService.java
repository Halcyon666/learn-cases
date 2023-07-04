package com.whalefall.learncases.design;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * 定义一个封装processService 实现IService的对象 进行复用
 *
 * @author WhaleFall
 * @date 2022-07-24 6:34
 */
@Slf4j
@Component
public class AService implements IService {

    //    @Resource(name="processService")
    private final IProcessService processService;

    public AService(IProcessService processService) {
        this.processService = processService;
    }

    public boolean handle(Consumer<Exception> exceptionConsumer) {
        return processService.handle(this, exceptionConsumer);
    }

    @Override
    public boolean doService() {
        log.info("do A service");
//        if (true)
//            throw new RuntimeException("Exception msg1");
        return true;
    }

    @Override
    public void doLog() {
        if (true)
            throw new RuntimeException("Exception msg2");
        log.error("A do log");
    }
}
