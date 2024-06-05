package com.whalefall.learncases.design.case2;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Halcyon
 * @date 2024/6/5 19:34
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ConcreteImpl extends AbstractClazz {
    private int age;
}
