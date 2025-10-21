package com.whalefall.learncases.design.functionalregistry.v3.template;

import com.whalefall.learncases.design.functionalregistry.v3.pojo.BusinessDto1;
import com.whalefall.learncases.design.functionalregistry.v3.service.BusinessType1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Halcyon
 * @since 2025/10/20 22:25
 */
@Component
@Slf4j
public class TemplateImpl1 implements Template<BusinessDto1, BusinessType1> {


    @Override
    public BusinessDto1 handler(String txcode, BusinessDto1 param, BusinessType1 businessService) {
        return businessService.doBusiness1(param);
    }
}
