package com.whalefall.learncases.design.functionalregistry.v1v2.template;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Halcyon
 * @since 2025/10/20 22:25
 */
@Component("templateImpl2v2")
@Slf4j
public class TemplateImpl2 implements Template<Map<String, Object>> {
    @Override
    public Map<String, Object> handler(Supplier<Map<String, Object>> businessFunction) {
        log.info("{}", this.getClass().getSimpleName());
        return businessFunction.get();
    }
}
