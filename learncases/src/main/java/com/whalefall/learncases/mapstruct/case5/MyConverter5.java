package com.whalefall.learncases.mapstruct.case5;

import com.whalefall.learncases.mapstruct.case4.MapStructEntity3;
import com.whalefall.learncases.mapstruct.case4.MapStructEntity4;
import com.whalefall.learncases.mapstruct.case4.Status;
import org.mapstruct.*;
import org.springframework.util.StringUtils;

/**
 * @author Halcyon
 * @date 2024/5/29 21:03
 * @since 1.0.0
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MyConverter5 {
    MyConverter5 INSTANCE = org.mapstruct.factory.Mappers.getMapper(MyConverter5.class);

    @Named("getStatus")
    static Status getStatus(String status) {
        return Status.getStatus(status);
    }

    @Mapping(target = "status", source = "entity3.status", qualifiedByName = "getStatus")
    MapStructEntity4 convert(MapStructEntity3 entity3, String haha);

    @SuppressWarnings("unused")
    @BeforeMapping
    default void beforeMapping(@MappingTarget MapStructEntity4 entity4, MapStructEntity3 entity3) {
        if (entity3 == null) {
            return;
        }
        // if entity3 is null, then fill it with default value A
        if (StringUtils.hasText(entity3.getStatus())) {
            entity3.setStatus("active");
        }
    }

    @AfterMapping
    default void afterMapping(@MappingTarget MapStructEntity4 entity4) {
        if (StringUtils.hasText(entity4.getName())) {
            entity4.setName("whalefall");
        }
        if (entity4.getStatus() == null) {
            entity4.setStatus(Status.A);
        }
    }
}
