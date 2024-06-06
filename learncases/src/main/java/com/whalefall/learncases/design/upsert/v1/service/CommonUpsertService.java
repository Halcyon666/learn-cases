package com.whalefall.learncases.design.upsert.v1.service;


import com.whalefall.learncases.design.upsert.v1.entity.itf.IEntity;
import com.whalefall.learncases.design.upsert.v1.entity.itf.Insert;
import com.whalefall.learncases.design.upsert.v1.entity.itf.Update;
import com.whalefall.learncases.design.upsert.v1.repository.IRepository;

/**
 * This is common used class, so type is generic type
 * @author Halcyon
 * @date 2024/5/24 22:56
 * @since 1.0.0
 */
public class CommonUpsertService<E extends IEntity<I, U>, I extends Insert, U extends Update> {

    public void upsertNoUsed(E entity, IRepository<E, I, U> repository) {
        repository.query(entity).ifPresentOrElse(
                hasQueryResult -> repository.update(entity.convertToUpdateEntity()),
                () -> repository.insert(entity.convertToInsertEntity())
        );
    }

    public void upsert(E entity, IRepository<E, I, U> repository) {
        repository.query(entity)
                .map(hasQueryResult -> entity.convertToUpdateEntity())
                .ifPresentOrElse(
                        repository::update,
                        () -> repository.insert(entity.convertToInsertEntity())
                );
    }
}
