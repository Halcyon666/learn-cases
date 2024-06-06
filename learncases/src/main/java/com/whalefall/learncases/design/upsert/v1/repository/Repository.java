package com.whalefall.learncases.design.upsert.v1.repository;

import com.whalefall.learncases.design.upsert.v1.entity.MyEntity;
import com.whalefall.learncases.design.upsert.v1.entity.MyInsertEntity;
import com.whalefall.learncases.design.upsert.v1.entity.MyUpdateEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * This class is special used, so type is concreted
 * @author Halcyon
 * @date 2024/5/24 22:56
 * @since 1.0.0
 */
@Slf4j
public class Repository implements IRepository<MyEntity, MyInsertEntity, MyUpdateEntity> {

    @Override
    public Optional<Boolean> query(MyEntity entity) {
        if (entity.isHasQueryResult()) {
            return Optional.of(Boolean.TRUE);
        }
        // Optional
        return Optional.empty();
    }

    @Override
    public void insert(MyInsertEntity myInsertEntity) {
        log.info("Inserting entity: {}", myInsertEntity);
    }

    @Override
    public void update(MyUpdateEntity myUpdateEntity) {
        log.info("Updating entity: {}", myUpdateEntity);

    }

}
