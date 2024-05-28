package com.whalefall.learncases.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author Halcyon
 * @date 2024/5/28 23:26
 * @since 1.0.0
 */
@SuppressWarnings("unused")
@Mapper
public interface MyConverter {
    MyConverter INSTANCE = Mappers.getMapper(MyConverter.class);

    @Mapping(target = "ssr", source = "SSr")
    @Mapping(target = "xAdress", source = "XAdress")
    @Mapping(target = "aAdress", source = "AAdress")
    @Mapping(target = "sMsgHello", source = "SMsgHello")
    @Mapping(target = "smSgHello1", source = "smSgHello1")
    @Mapping(target = "sMSG", source = "SMSG")
    MapStructEntity2 entity2ToEntity1(MapStructEntity1 entity1);
}
