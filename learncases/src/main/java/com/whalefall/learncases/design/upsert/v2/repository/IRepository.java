package com.whalefall.learncases.design.upsert.v2.repository;

import com.whalefall.learncases.design.upsert.v2.entity.itf.IEntity;
import com.whalefall.learncases.design.upsert.v2.entity.itf.Insert;
import com.whalefall.learncases.design.upsert.v2.entity.itf.Update;

import java.util.Optional;

/**
 * @author Halcyon
 * @date 2024/5/24 23:00
 * @since 1.0.0
 */
public interface IRepository {
    Optional<Boolean> query(IEntity e);

    Insert insert(Insert i);

    void update(Update u);
}
