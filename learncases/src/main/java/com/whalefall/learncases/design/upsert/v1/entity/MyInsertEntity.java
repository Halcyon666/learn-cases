package com.whalefall.learncases.design.upsert.v1.entity;

import com.whalefall.learncases.design.upsert.v1.entity.itf.Insert;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Halcyon
 * @date 2024/5/24 23:31
 * @since 1.0.0
 */
@Builder
@Getter
@ToString
public class MyInsertEntity implements Insert {
    private String id;
    private boolean hasQueryResult;
    private String name;
}
