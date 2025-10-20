package com.whalefall.learncases.design.functionalregistry.template;

import com.whalefall.learncases.design.functionalregistry.pojo.TxData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

/**
 * @author Halcyon
 * @since 2025/10/20 22:25
 */
@Component("templateImpl1")
@Slf4j
public class TemplateImpl1 implements Template<TxData> {
    @Override
    public TxData handler(Supplier<TxData> businessFunction) {
        log.info("{}", this.getClass().getSimpleName());
        return businessFunction.get();
    }
}
