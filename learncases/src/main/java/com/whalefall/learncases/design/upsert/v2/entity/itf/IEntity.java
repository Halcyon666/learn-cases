package com.whalefall.learncases.design.upsert.v2.entity.itf;

/**
 * @author Halcyon
 * @date 2024/5/24 22:56
 * @since 1.0.0
 */
public interface IEntity {
    Insert convertToInsertEntity();

    Update convertToUpdateEntity();
}
