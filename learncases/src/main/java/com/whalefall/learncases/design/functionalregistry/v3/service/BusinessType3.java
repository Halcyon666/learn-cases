package com.whalefall.learncases.design.functionalregistry.v3.service;

import com.whalefall.learncases.design.functionalregistry.v3.pojo.BusinessContext;

/**
 * @author Halcyon
 * @since 2025/10/21 22:04
 */
public interface BusinessType3<I, O> extends Business<BusinessContext<I, O>> {
    BusinessContext<I, O> doBusiness2(BusinessContext<I, O> businessContext);

}
