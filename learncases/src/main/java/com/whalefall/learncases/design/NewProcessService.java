package com.whalefall.learncases.design;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * @author WhaleFall
 * @date 2022-07-29 21:11
 */
@Component
@Slf4j
public class NewProcessService implements IProcessService {
    @Override
    public boolean handle(IService service, Consumer<Exception> exceptionConsumer) {
        log.info("new ProcessService");
        service(service, exceptionConsumer);
        return true;
    }

    private void service(@NotNull IService service, Consumer<Exception> exceptionConsumer) {
        try {
            service.doService();
        } catch (Exception e1) {
            exceptionConsumer.accept(e1);
            throw e1;
        }
    }

}
