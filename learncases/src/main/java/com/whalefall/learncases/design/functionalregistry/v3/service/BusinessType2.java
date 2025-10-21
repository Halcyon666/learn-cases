package com.whalefall.learncases.design.functionalregistry.v3.service;

import com.whalefall.learncases.design.functionalregistry.v3.pojo.BusinessDto2;

/**
 * @author Halcyon
 * @since 2025/10/21 22:04
 */
public interface BusinessType2 extends Business<BusinessDto2> {
    BusinessDto2 doBusiness2(BusinessDto2 t);

}
