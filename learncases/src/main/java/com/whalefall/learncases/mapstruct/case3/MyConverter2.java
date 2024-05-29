package com.whalefall.learncases.mapstruct.case3;

import com.whalefall.learncases.mapstruct.case1.MapStructEntity1;
import com.whalefall.learncases.mapstruct.case1.MapStructEntity2;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author Halcyon
 * @date 2024/5/28 23:26
 * @since 1.0.0
 */
@SuppressWarnings("unused")
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MyConverter2 {
    MyConverter2 INSTANCE = Mappers.getMapper(MyConverter2.class);

    @Mapping(target = "ssr", source = "SSr", qualifiedByName = "ssr")
    List<MapStructEntity2> entity2ToEntity1(List<MapStructEntity1> entity1);

    default String ssr(String str) {
        return "hello srr";
    }
}
