package com.whalefall.learncases.design.case2;

import lombok.Data;

/**
 * @author Halcyon
 * @date 2024/6/5 19:34
 * @since 1.0.0
 */
@SuppressWarnings("all")
@Data
public class AbstractClazz {
    public String address;
    protected String name;
    String phone;
    private String postcode;
}
