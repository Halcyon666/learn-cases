package com.whalefall.drools;

import com.whalefall.AbstractTest;
import com.whalefall.learncases.drools.PersonService;
import jakarta.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Halcyon
 * @date 2024/5/24 1:06
 * @since 1.0.0
 */
class DroolsTests extends AbstractTest {
    @Resource
    private PersonService personService;

    @Test
    void testInsertTwo() {
        Assertions.assertThatThrownBy(() -> personService.kieTwoInsert())
                .hasCauseExactlyInstanceOf(Exception.class)
                .hasMessageContaining(PersonService.ERROR_MSG);

    }

}
