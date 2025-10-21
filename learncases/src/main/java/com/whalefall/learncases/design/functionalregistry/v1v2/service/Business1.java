package com.whalefall.learncases.design.functionalregistry.v1v2.service;

import com.whalefall.learncases.design.functionalregistry.v1v2.anno.UseTemplate;
import com.whalefall.learncases.design.functionalregistry.v1v2.pojo.TxData;
import com.whalefall.learncases.design.functionalregistry.v1v2.template.TemplateImpl1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Halcyon
 * @since 2025/10/20 22:28
 */
@Service("business1v1")
@Slf4j
@UseTemplate(TemplateImpl1.class)
public class Business1 implements Business<TxData> {

    @Override
    public TxData doJob(TxData txData) {
        log.info("{} executed", this.getClass().getSimpleName());
        return txData;
    }
}
