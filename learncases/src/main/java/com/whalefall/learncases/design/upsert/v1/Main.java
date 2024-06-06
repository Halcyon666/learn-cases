package com.whalefall.learncases.design.upsert.v1;

import com.whalefall.learncases.design.upsert.v1.entity.MyEntity;
import com.whalefall.learncases.design.upsert.v1.entity.MyInsertEntity;
import com.whalefall.learncases.design.upsert.v1.entity.MyUpdateEntity;
import com.whalefall.learncases.design.upsert.v1.repository.Repository;
import com.whalefall.learncases.design.upsert.v1.service.CommonUpsertService;

public class Main {
    public static void main(String[] args) {

        Repository myEntityRepository = new Repository();
        CommonUpsertService<MyEntity, MyInsertEntity, MyUpdateEntity> commonUpsertService = new CommonUpsertService<>();

        // 调用 upsert 方法
        commonUpsertService.upsertNoUsed(MyEntity.builder().id("123").hasQueryResult(true).build(), myEntityRepository);
        commonUpsertService.upsert(MyEntity.builder().id("123").hasQueryResult(false).build(), myEntityRepository);
        // output
        // Updating entity: MyUpdateEntity(id=123, data=abc, age=18)
        // Inserting entity: MyInsertEntity(id=123, data=1abc, name=Halcyon)

    }
}
