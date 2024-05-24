package com.whalefall.learncases.upsert.repository;

import com.whalefall.learncases.upsert.entity.itf.IEntity;
import com.whalefall.learncases.upsert.entity.itf.Insert;
import com.whalefall.learncases.upsert.entity.itf.Update;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * @author Halcyon
 * @date 2024/5/24 22:56
 * @since 1.0.0
 */
@Slf4j
public class Repository implements IRepository {

    @Override
    public Optional<Boolean> query(IEntity entity) {
        return Optional.empty();
    }

    @Override
    public void insert(Insert entity) {
        log.info("Inserting entity: {}", entity);

    }

    @Override
    public void update(Update entity) {
        log.info("Updating entity: {}", entity);

    }

}
