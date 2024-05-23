package com.whalefall.drools;

import com.whalefall.AbstractLearncasesApplicationTests;
import com.whalefall.learncases.drools.PersonService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Halcyon
 * @date 2024/5/24 1:06
 * @since 1.0.0
 */
class DroolsTests extends AbstractLearncasesApplicationTests {
    @Resource
    private PersonService personService;

    @Test
    void testInsertTwo() {
        String message = assertThrows(Exception.class,
                () -> personService.kieTwoInsert()).getMessage();
        Assertions.assertTrue(message.contains(PersonService.ERROR_MSG));

    }

}
