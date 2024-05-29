package com.whalefall.learncases.mapstruct.case4;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * @author Halcyon
 * @date 2024/5/29 21:03
 * @since 1.0.0
 */
@Mapper
public interface MyConverter4 {
    MyConverter4 INSTANCE = org.mapstruct.factory.Mappers.getMapper(MyConverter4.class);

    @Named("getStatus")
    static Status getStatus(String status) {
        return Status.getStatus(status);
    }

    @SuppressWarnings("unused")
    @Mapping(target = "status", source = "status", qualifiedByName = "getStatus")
    MapStructEntity4 convert(MapStructEntity3 entity3);
}
