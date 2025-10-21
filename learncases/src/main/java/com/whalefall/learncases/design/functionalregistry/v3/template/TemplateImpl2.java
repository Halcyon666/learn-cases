package com.whalefall.learncases.design.functionalregistry.v3.template;

import com.whalefall.learncases.design.functionalregistry.v3.pojo.BusinessDto2;
import com.whalefall.learncases.design.functionalregistry.v3.service.BusinessType2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Halcyon
 * @since 2025/10/20 22:25
 */
@Component
@Slf4j
public class TemplateImpl2 implements Template<BusinessDto2, BusinessType2> {

    @Override
    public BusinessDto2 handler(String txcode, BusinessDto2 param, BusinessType2 businessService) {
        return businessService.doBusiness2(param);
    }
}
