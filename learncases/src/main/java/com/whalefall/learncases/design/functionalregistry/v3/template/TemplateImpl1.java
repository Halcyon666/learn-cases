package com.whalefall.learncases.design.functionalregistry.v3.template;

import com.whalefall.learncases.design.functionalregistry.v3.pojo.TxData;
import com.whalefall.learncases.design.functionalregistry.v3.service.BusinessType1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Halcyon
 * @since 2025/10/20 22:25
 */
@Component
@Slf4j
public class TemplateImpl1 implements Template<TxData, BusinessType1> {


    @Override
    public TxData handler(String txcode, TxData param, BusinessType1 businessService) {
        return businessService.doBusiness1(param);
    }
}
