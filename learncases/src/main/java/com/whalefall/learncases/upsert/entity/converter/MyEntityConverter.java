package com.whalefall.learncases.upsert.entity.converter;

import com.whalefall.learncases.upsert.entity.MyEntity;
import com.whalefall.learncases.upsert.entity.MyInsertEntity;
import com.whalefall.learncases.upsert.entity.MyUpdateEntity;
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

    MyInsertEntity getInsertEntity(MyEntity myEntity, String name);

    MyUpdateEntity getUpdateEntity(MyEntity myEntity, Integer age);
}
