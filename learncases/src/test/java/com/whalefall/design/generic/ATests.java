package com.whalefall.design.generic;

import com.whalefall.learncases.design.generic.A;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Halcyon
 * @date 2024/6/10 22:48
 * @since 1.0.0
 */
class ATests {


    @Test
    void testWild1() {
        Collection<String> c = A.testWild1(new ArrayList<>());
        assertTrue(c.contains("1231"), "Collection should contain the string '1231'");
        c.forEach(System.out::println);
    }

    @Test
    void testGenericMethod() {
        // 泛型方法
        A<Integer> aq = new A<>();
        Assertions.assertThatNoException().isThrownBy(() -> aq.doLog(100));
        Assertions.assertThatNoException().isThrownBy(() -> aq.doLog(null));
    }

}
