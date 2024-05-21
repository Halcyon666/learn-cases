package com.whalefall.lambda;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


/**
 * @author Halcyon
 * @date 2024/5/21
 * @since 1.0.0
 */
class FunctionSampleTests {

    static final Map<String, Function<String, Exception>> FUNCTION_ACTION_MAP = new HashMap<>();

    @BeforeEach
    void before() {
        FUNCTION_ACTION_MAP.put("Exception", Exception::new);
    }


    @Test
    void testFunction() {
        String action = "Exception";
        String message = "Custom Exception Message";


        Function<String, Exception> function = FUNCTION_ACTION_MAP.get(action);
        Exception exception = function.apply(message);
        Assertions.assertThrows(Exception.class, () -> {
            throw exception;
        });
    }
}
