package com.whalefall.learncases.design.case1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * @author WhaleFall
 * @date 2022-07-24 6:26
 */
@Slf4j
@Component
public class ProcessService implements IProcessService {

    public boolean handle(IService service, Consumer<Exception> exceptionConsumer) {
        log.info("old ProcessService");
        service.doServiceBefore();
        try {
            service.doService();
        } catch (Exception e) {
            exceptionConsumer.accept(e);
            throw e;
        }
        service.doServiceAfter();
        service.doLog();

        return true;
    }

}
