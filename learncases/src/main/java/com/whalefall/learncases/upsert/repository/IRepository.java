package com.whalefall.learncases.upsert.repository;

import com.whalefall.learncases.upsert.entity.itf.IEntity;
import com.whalefall.learncases.upsert.entity.itf.Insert;
import com.whalefall.learncases.upsert.entity.itf.Update;

import java.util.Optional;

/**
 * @author Halcyon
 * @date 2024/5/24 23:00
 * @since 1.0.0
 */
public interface IRepository {
    Optional<Boolean> query(IEntity entity);

    void insert(Insert entity);

    void update(Update entity);
}
