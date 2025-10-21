package com.whalefall.learncases.design.functionalregistry.v3.service;


import com.whalefall.learncases.design.functionalregistry.v3.pojo.BusinessDto1;

/**
 * @author Halcyon
 * @since 2025/10/21 22:04
 */
public interface BusinessType1 extends Business<BusinessDto1> {
    BusinessDto1 doBusiness1(BusinessDto1 t);
}
