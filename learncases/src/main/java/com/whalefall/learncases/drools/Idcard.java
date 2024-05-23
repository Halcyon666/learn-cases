package com.whalefall.learncases.drools;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Halcyon
 * @date 2024/5/23 22:45
 * @since 1.0.0
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Idcard {
    private String identity;
    private String name;
    private String zone;
    private Gender gender;
}
