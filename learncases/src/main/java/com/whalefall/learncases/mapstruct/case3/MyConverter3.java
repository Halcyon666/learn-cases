package com.whalefall.learncases.mapstruct.case3;

import com.whalefall.learncases.mapstruct.case1.MapStructEntity1;
import com.whalefall.learncases.mapstruct.case1.MapStructEntity2;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author Halcyon
 * @date 2024/5/28 23:26
 * @since 1.0.0
 */
@SuppressWarnings("unused")
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MyConverter3 {
    MyConverter3 INSTANCE = Mappers.getMapper(MyConverter3.class);

    @Mapping(target = "ssr", source = "SSr", qualifiedByName = "ssr")
    MapStructEntity2 entity2ToEntity1(MapStructEntity1 entity1);

    @Named("ssr")
    default String ssr(String str) {
        return "hello srr";
    }
}
