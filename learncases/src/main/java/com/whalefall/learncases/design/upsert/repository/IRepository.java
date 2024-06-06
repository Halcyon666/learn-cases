package com.whalefall.learncases.design.upsert.repository;

import com.whalefall.learncases.design.upsert.entity.itf.IEntity;
import com.whalefall.learncases.design.upsert.entity.itf.Insert;
import com.whalefall.learncases.design.upsert.entity.itf.Update;

import java.util.Optional;

/**
 * @author Halcyon
 * @date 2024/5/24 23:00
 * @since 1.0.0
 */
public interface IRepository<E extends IEntity<I, U>, I extends Insert, U extends Update> {
    Optional<Boolean> query(E e);

    void insert(I i);

    void update(U u);
}
