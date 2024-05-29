package com.whalefall.learncases.mapstruct.case2;

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
public interface MyConverter1 {
    MyConverter1 INSTANCE = Mappers.getMapper(MyConverter1.class);

    @Mapping(target = "ssr", source = "SSr")
    @Mapping(target = "xAdress", source = "XAdress")
    @Mapping(target = "aAdress", source = "AAdress")
    @Mapping(target = "sMsgHello", source = "SMsgHello")
    @Mapping(target = "smSgHello1", source = "smSgHello1")
    @Mapping(target = "sMSG", source = "SMSG")
    List<MapStructEntity2> entity2ToEntity1(List<MapStructEntity1> entity1);
}
