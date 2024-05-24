package com.whalefall.learncases.upsert.entity;

import com.whalefall.learncases.upsert.entity.itf.Update;
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
public class MyUpdateEntity implements Update {
    private String id;
    private boolean hasQueryResult;
    private int age;

}
