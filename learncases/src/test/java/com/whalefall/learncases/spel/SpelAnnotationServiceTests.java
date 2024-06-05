package com.whalefall.learncases.spel;

import com.whalefall.AbstractLearncasesApplicationTests;
import jakarta.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Halcyon
 * @date 2024/6/5 23:10
 * @since 1.0.0
 */
class SpelAnnotationServiceTests extends AbstractLearncasesApplicationTests {
    @Resource
    private SpelAnnotationService spelAnnotationService;
    @Test
    void testSpelAnnotationService() {
        String hhh = spelAnnotationService.get1("hhh");
        Assertions.assertThat(hhh).isEqualTo("Season:SPRING- UpperWorld:HHH");
    }
}
