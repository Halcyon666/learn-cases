package com.whalefall.learncases.design.upsert.v2.repository;

import com.whalefall.learncases.design.upsert.v2.entity.MyEntity;
import com.whalefall.learncases.design.upsert.v2.entity.itf.IEntity;
import com.whalefall.learncases.design.upsert.v2.entity.itf.Insert;
import com.whalefall.learncases.design.upsert.v2.entity.itf.Update;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * This class is special used, so type is concreted
 * @author Halcyon
 * @date 2024/5/24 22:56
 * @since 1.0.0
 */
@Slf4j
public class Repository implements IRepository {

    @Override
    public Optional<Boolean> query(IEntity e) {
        if (e instanceof MyEntity entity && entity.isHasQueryResult()) {
            return Optional.of(Boolean.TRUE);
        }
        // Optional
        return Optional.empty();
    }

    @Override
    public Insert insert(Insert i) {
        log.info("Inserting entity: {}", i);
        return i;
    }

    @Override
    public void update(Update u) {
        log.info("Updating entity: {}", u);
    }
}
