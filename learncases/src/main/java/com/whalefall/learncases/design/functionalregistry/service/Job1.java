package com.whalefall.learncases.design.functionalregistry.service;

import com.whalefall.learncases.design.functionalregistry.anno.UseTemplate;
import com.whalefall.learncases.design.functionalregistry.pojo.TxData;
import com.whalefall.learncases.design.functionalregistry.template.TemplateImpl1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Halcyon
 * @since 2025/10/20 22:28
 */
@Service
@Slf4j
@UseTemplate(TemplateImpl1.class)
public class Job1 implements Job<TxData> {

    @Override
    public TxData doJob(TxData txData) {
        log.info("{} executed", this.getClass().getSimpleName());
        return txData;
    }
}
