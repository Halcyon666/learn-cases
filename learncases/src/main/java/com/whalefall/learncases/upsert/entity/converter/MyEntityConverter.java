package com.whalefall.learncases.upsert.entity.converter;

import com.whalefall.learncases.upsert.entity.InsertEntity;
import com.whalefall.learncases.upsert.entity.MyEntity;
import com.whalefall.learncases.upsert.entity.UpdateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Halcyon
 * @date 2024/5/24 23:54
 * @since 1.0.0
 */
@Mapper
public interface MyEntityConverter {
    MyEntityConverter INSTANCE = Mappers.getMapper(MyEntityConverter.class);

    InsertEntity getInsertEntity(MyEntity myEntity, String name);

    UpdateEntity getUpdateEntity(MyEntity myEntity, Integer age);
}
