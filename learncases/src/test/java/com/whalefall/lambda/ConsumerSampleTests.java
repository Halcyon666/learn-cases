package com.whalefall.lambda;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Halcyon
 * @date 2024/5/21 23:48
 * @since 1.0.0
 */
class ConsumerSampleTests {

    static final Map<String, Consumer<String>> CONSUMER_ACTION_MAP = new HashMap<>();

    @BeforeEach
    void before() {
        CONSUMER_ACTION_MAP.put("print", System.out::println);
        CONSUMER_ACTION_MAP.put("uppercase", str -> System.out.println(str.toUpperCase()));
        CONSUMER_ACTION_MAP.put("lowercase", str -> System.out.println(str.toLowerCase()));
    }

    @Test
    void testPrintAction() {
        String action = "print";
        String input = "Hello, World!";

        Consumer<String> consumer = CONSUMER_ACTION_MAP.getOrDefault(action, str -> System.out.println("Unknown action: " + str));
        consumer.accept(input);
        Assertions.assertNotNull(consumer);
    }
}
