package com.whalefall.learncases.design.functionalregistry.v3.service;


import com.whalefall.learncases.design.functionalregistry.v3.pojo.TxData;

/**
 * @author Halcyon
 * @since 2025/10/21 22:04
 */
public interface BusinessType1 extends Business<TxData> {
    TxData doBusiness1(TxData t);
}
