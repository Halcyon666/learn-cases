package com.whalefall.learncases.design.functionalregistry.v3.service;

import com.whalefall.learncases.design.functionalregistry.v3.anno.UseTemplate;
import com.whalefall.learncases.design.functionalregistry.v3.template.TemplateImpl2;
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
public class Business2 implements BusinessType2 {

    @Override
    public Map<String, Object> doBusiness2(Map<String, Object> t) {
        return t;
    }
}
