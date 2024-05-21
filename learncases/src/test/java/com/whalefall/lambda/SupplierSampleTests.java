package com.whalefall.lambda;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;


/**
 * @author Halcyon
 * @date 2024/5/21
 * @since 1.0.0
 */
class SupplierSampleTests {

    static final Map<String, Supplier<Exception>> SUPPLIER_ACTION_MAP = new HashMap<>();

    @BeforeEach
    void before() {
        SUPPLIER_ACTION_MAP.put("Exception", Exception::new);
    }

    @Test
    void testFunction() {
        String action = "Exception";

        Supplier<Exception> supplier = SUPPLIER_ACTION_MAP.get(action);
        Exception exception = supplier.get();

        Assertions.assertThrows(Exception.class, () -> {
            throw exception;
        });
    }
}
