package com.whalefall.learncases.design.functionalregistry.v3.service;

import java.util.Map;

/**
 * @author Halcyon
 * @since 2025/10/21 22:04
 */
public interface BusinessType2 extends Business<Map<String, Object>> {
    Map<String, Object> doBusiness2(Map<String, Object> t);

}
