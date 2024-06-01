package com.whalefall.learncases.mapstruct.case5;

import com.whalefall.learncases.mapstruct.case4.MapStructEntity3;
import com.whalefall.learncases.mapstruct.case4.MapStructEntity4;
import com.whalefall.learncases.mapstruct.case4.Status;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

/**
 * @author Halcyon
 * @date 2024/5/29 21:03
 * @since 1.0.0
 */
@SuppressWarnings("all")
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MyConverter6 {
    MyConverter6 INSTANCE = org.mapstruct.factory.Mappers.getMapper(MyConverter6.class);

    @Named("getStatus")
    static Status getStatus(String status) {
        return Status.getStatus(status);
    }
    /**
     *   because guard MyConvert5, so comment these methods, and  {@code @Builder} methods
     * {@link MapStructEntity4}
//     */
    @Mapping(target = "status", source = "entity3.status", qualifiedByName = "getStatus")
    MapStructEntity4 convert(MapStructEntity3 entity3, String haha);



    /*@BeforeMapping
    default void beforeMapping(@MappingTarget MapStructEntity4.MapStructEntity4Builder entity4, MapStructEntity3 entity3) {
        if (entity3 == null) {
            return;
        }

        // if entity3 is null, then fill it with default value A
        if (StringUtils.isEmpty(entity3.getStatus())) {
            entity3.setStatus("active");
        }
    }*/

    /*@AfterMapping
    default void afterMapping(@MappingTarget MapStructEntity4.MapStructEntity4Builder entity4) {
        // nothing here
    }*/
}
