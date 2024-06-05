package com.whalefall.learncases.design.case1;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@Slf4j
@SuppressWarnings("unused")
public class SProcessService {

    // Handles the service lifecycle
    public boolean handle(IService service, Consumer<Exception> exceptionConsumer) {
        serviceBefore(service);
        try {
            service(service);
        } catch (Exception e) {
            exceptionConsumer.accept(e);
            return false;
        }
        serviceAfter(service);
        log(service, exceptionConsumer);
        return true;
    }

    // Executes pre-service actions
    private void serviceBefore(@NotNull IService service) {
        service.doServiceBefore();
    }

    // Executes the main service actions
    private void service(@NotNull IService service) {
        service.doService();
    }

    // Executes post-service actions
    private void serviceAfter(@NotNull IService service) {
        service.doServiceAfter();
    }

    // Logs the service actions and handles logging exceptions separately
    private void log(@NotNull IService service, Consumer<Exception> exceptionConsumer) {
        try {
            service.doLog();
        } catch (Exception e) {
            log.error("Failed to do log", e);
            exceptionConsumer.accept(e);
        }
    }
}
