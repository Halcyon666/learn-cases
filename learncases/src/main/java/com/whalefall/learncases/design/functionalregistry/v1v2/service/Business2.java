package com.whalefall.learncases.design.functionalregistry.v1v2.service;

import com.whalefall.learncases.design.functionalregistry.v1v2.anno.UseTemplate;
import com.whalefall.learncases.design.functionalregistry.v1v2.template.TemplateImpl2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Halcyon
 * @since 2025/10/20 22:28
 */
@Service("business2v1")
@Slf4j
@UseTemplate(TemplateImpl2.class)
public class Business2 implements Business<Map<String, Object>> {


    @Override
    public Map<String, Object> doJob(Map<String, Object> stringObjectMap) {
        log.info("{} executed", this.getClass().getSimpleName());
        return stringObjectMap;
    }

}
