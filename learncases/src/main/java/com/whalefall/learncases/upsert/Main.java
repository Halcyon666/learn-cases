package com.whalefall.learncases.upsert;

import com.whalefall.learncases.upsert.entity.MyEntity;
import com.whalefall.learncases.upsert.repository.Repository;
import com.whalefall.learncases.upsert.service.AppService;

public class Main {
    public static void main(String[] args) {

        // 创建 Repository 和 AppService 实例
        Repository myEntityRepository = new Repository();
        AppService appService = new AppService();

        // 创建一个具体的实体对象
        MyEntity entity = MyEntity.builder().id("123").data("ads").build();

        // 调用 upsert 方法
        appService.upsert(entity, myEntityRepository);


    }
}
