package com.whalefall.learncases.design.functionalregistry.v3.template;

import com.whalefall.learncases.design.functionalregistry.v3.pojo.BusinessContext;
import com.whalefall.learncases.design.functionalregistry.v3.service.BusinessType3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Halcyon
 * @since 2025/10/20 22:25
 */
@Component
@Slf4j
public class TemplateImpl3<I, O> implements Template<BusinessContext<I, O>, BusinessType3<I, O>> {

    @Override
    public void handler(String txcode, BusinessContext<I, O> param, BusinessType3<I, O> businessService) {
        businessService.doBusiness2(param);
    }
}
