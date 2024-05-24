package com.whalefall.learncases.upsert.entity;

import com.whalefall.learncases.upsert.entity.itf.Insert;
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
public class InsertEntity implements Insert {
    private String id;
    private String data;
    private String name;
}
