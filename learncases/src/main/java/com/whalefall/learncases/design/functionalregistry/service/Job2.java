package com.whalefall.learncases.design.functionalregistry.service;

import com.whalefall.learncases.design.functionalregistry.anno.UseTemplate;
import com.whalefall.learncases.design.functionalregistry.template.TemplateImpl2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Halcyon
 * @since 2025/10/20 22:28
 */
@Service
@Slf4j
@UseTemplate(TemplateImpl2.class)
public class Job2 implements Job<Map<String, Object>> {


    @Override
    public Map<String, Object> doJob(Map<String, Object> stringObjectMap) {
        log.info("{} executed", this.getClass().getSimpleName());
        return stringObjectMap;
    }

}
