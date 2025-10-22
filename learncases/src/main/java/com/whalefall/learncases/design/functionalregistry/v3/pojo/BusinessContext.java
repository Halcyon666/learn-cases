package com.whalefall.learncases.design.functionalregistry.v3.pojo;

import lombok.Data;

/**
 * @author Halcyon
 * @since 2025/10/22 21:35
 */
@Data
public class BusinessContext<I, O> {

    private I input;
    private O output;

    private OtherDto3 otherDto;
}
