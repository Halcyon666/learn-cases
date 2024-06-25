package com.whalefall.learncases.design.case1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * 定义一个封装processService 实现IService的对象 进行复用
 * {@link this#handle(Consumer)}
 * @author WhaleFall
 * @date 2022-07-24 6:34
 */
@Slf4j
@Component
public class ServiceA implements IService {

    //    @Resource(name="processService")
    private final IProcessService processService;

    public ServiceA(IProcessService processService) {
        this.processService = processService;
    }

    public boolean handle(Consumer<Exception> exceptionConsumer) {
        return processService.handle(this, exceptionConsumer);
    }

    @SuppressWarnings("all")
    @Override
    public boolean doService() {
        log.info("do A service");
//        if (true)
//            throw new RuntimeException("Exception msg1");
        return true;
    }

    @SuppressWarnings("all")
    @Override
    public void doLog() {
        if (true)
            throw new RuntimeException("Exception msg2");
        log.error("A do log");
    }
}
