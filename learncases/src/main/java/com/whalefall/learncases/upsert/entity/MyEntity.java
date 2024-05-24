package com.whalefall.learncases.upsert.entity;

import com.whalefall.learncases.upsert.entity.converter.MyEntityConverter;
import com.whalefall.learncases.upsert.entity.itf.IEntity;
import lombok.Builder;
import lombok.Getter;

/**
 * @author Halcyon
 * @date 2024/5/24 22:56
 * @since 1.0.0
 */
@Builder
@Getter
public class MyEntity implements IEntity<MyInsertEntity, MyUpdateEntity> { // Note: Use Object as a placeholder for now
    private String id;
    private boolean hasQueryResult;

    @Override
    public MyInsertEntity convertToInsertEntity() {
        return MyEntityConverter.INSTANCE.getInsertEntity(this, "Halcyon");
    }

    @Override
    public MyUpdateEntity convertToUpdateEntity() {
        return MyEntityConverter.INSTANCE.getUpdateEntity(this, 18);
    }

}
