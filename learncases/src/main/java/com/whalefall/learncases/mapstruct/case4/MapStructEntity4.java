package com.whalefall.learncases.mapstruct.case4;

import lombok.Builder;
import lombok.Data;

/**
 * @author Halcyon
 * @date 2024/5/29 20:58
 * @since 1.0.0
 */
@Data
@Builder
public class MapStructEntity4 {
    private Status status;
    private String name;
}
