package com.whalefall.learncases.mapstruct.case5;

import com.whalefall.learncases.mapstruct.case4.MapStructEntity4;
import com.whalefall.learncases.mapstruct.case4.Status;

/**
 * @author Halcyon
 * @date 2024/6/1 23:27
 * @since 1.0.0
 */
@SuppressWarnings("all")
public class TestCallBuilderBuildTwice {
    public static void main(String[] args) {
        MapStructEntity4.MapStructEntity4Builder mapStructEntity4 = MapStructEntity4.builder();
        mapStructEntity4.name("aa");
        MapStructEntity4 build = getBuild(mapStructEntity4);
        mapStructEntity4.status(Status.C);
        MapStructEntity4 build1 = getBuild(mapStructEntity4);
        System.out.println(build == build1);
        System.out.println("mapStructEntity4 = " + build);
        System.out.println("mapStructEntity4 = " + build1);
    }

    private static MapStructEntity4 getBuild(MapStructEntity4.MapStructEntity4Builder mapStructEntity4) {
        return mapStructEntity4.build();
    }
}
