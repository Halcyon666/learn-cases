package com.whalefall.learncases.design.upsert.v2.service;


import com.whalefall.learncases.design.upsert.v2.entity.itf.IEntity;
import com.whalefall.learncases.design.upsert.v2.repository.IRepository;

/**
 * This is common used class, so type is generic type
 * @author Halcyon
 * @date 2024/5/24 22:56
 * @since 1.0.0
 */
public class CommonUpsertService{

    public void upsertNoUsed(IEntity entity, IRepository repository) {
        repository.query(entity).ifPresentOrElse(
                hasQueryResult -> repository.update(entity.convertToUpdateEntity()),
                () -> repository.insert(entity.convertToInsertEntity())
        );
    }

    public void upsert(IEntity entity, IRepository repository) {
        repository.query(entity)
                .map(hasQueryResult -> entity.convertToUpdateEntity())
                .ifPresentOrElse(
                        repository::update,
                        () -> repository.insert(entity.convertToInsertEntity())
                );
    }
}
