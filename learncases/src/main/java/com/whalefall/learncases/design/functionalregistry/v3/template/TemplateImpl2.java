package com.whalefall.learncases.design.functionalregistry.v3.template;

import com.whalefall.learncases.design.functionalregistry.v3.service.BusinessType2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Halcyon
 * @since 2025/10/20 22:25
 */
@Component
@Slf4j
public class TemplateImpl2 implements Template<Map<String, Object>, BusinessType2> {

    @Override
    public Map<String, Object> handler(String txcode, Map<String, Object> param, BusinessType2 businessService) {
        return businessService.doBusiness2(param);
    }
}
