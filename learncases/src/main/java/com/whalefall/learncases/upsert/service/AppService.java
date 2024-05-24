package com.whalefall.learncases.upsert.service;


import com.whalefall.learncases.upsert.entity.itf.IEntity;
import com.whalefall.learncases.upsert.repository.IRepository;

/**
 * @author Halcyon
 * @date 2024/5/24 22:56
 * @since 1.0.0
 */ // 定义通用的 AppService 类
public class AppService {

    public void upsert(IEntity entity, IRepository repository) {
        repository.query(entity)
                .map(existingEntity -> entity.convertToUpdateEntity())
                .ifPresentOrElse(
                        repository::update,
                        () -> repository.insert(entity.convertToInsertEntity())
                );
    }
}
