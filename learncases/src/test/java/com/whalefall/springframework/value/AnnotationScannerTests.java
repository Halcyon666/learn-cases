package com.whalefall.springframework.value;

import com.whalefall.learncases.springframework.value.AnnotationScanner;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.whalefall.learncases.springframework.value.AnnotationScanner.scanPackageForValueAnnotations;

/**
 * @author Halcyon
 * @date 2024/6/24 21:00
 * @since 1.0.0
 */
@Slf4j
class AnnotationScannerTests {
    @Test
    void testScanPackageForValueAnnotations() {
        Map<String, AnnotationScanner.PropertyInfo> stringPropertyInfoMap =
                scanPackageForValueAnnotations(
                        "com.whalefall.learncases.springframework.value.withvallueclazz");

        log.info("Scanned properties:");
        stringPropertyInfoMap.forEach((key, value) -> {
            log.info("Property: {}", key);
            log.info("value: {}", value);
        });

    }
}
