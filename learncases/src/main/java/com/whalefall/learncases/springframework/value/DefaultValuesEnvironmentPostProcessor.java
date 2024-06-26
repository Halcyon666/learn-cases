package com.whalefall.learncases.springframework.value;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@Slf4j
public class DefaultValuesEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Map<String, Object> defaultValues = new HashMap<>();

        // 扫描所有@Value注解
        Map<String, AnnotationScanner.PropertyInfo> properties =
                AnnotationScanner.scanPackageForValueAnnotations(
                        "com.whalefall.learncases.springframework.value.withvallueclazz");

        log.info("DefaultValuesEnvironmentPostProcessor: Scanned properties: {}", properties.keySet());

        // 为所有扫描到的属性设置默认值
        properties.forEach((key, propertyInfo) -> {
            if (!environment.containsProperty(key)) {
                Object defaultValue = getDefaultValueForType(propertyInfo.type());
                defaultValues.put(key, defaultValue);
                log.info("DefaultValuesEnvironmentPostProcessor: Setting default value for {} = {}", key, defaultValue);
            }
        });

        if (!defaultValues.isEmpty()) {
            MapPropertySource defaultPropertySource = new MapPropertySource("defaultValues", defaultValues);
            MutablePropertySources propertySources = environment.getPropertySources();

            // 这段代码的目的是确保 defaultPropertySource 的插入位置合理，使其既能作为默认值的提供者，又不会覆盖已有的系统属性
            // 添加默认值源到第一位，确保它有最高优先级
            if (propertySources.contains("systemProperties")) {
                propertySources.addAfter("systemProperties", defaultPropertySource);
            } else {
                propertySources.addFirst(defaultPropertySource);
            }

            log.info("DefaultValuesEnvironmentPostProcessor: Added default values to environment");
        }
    }

    private Object getDefaultValueForType(Class<?> type) {
        if (type == String.class) {
            return "defaultString";
        }

        return switch (type.getSimpleName()) {
            case "int", "Integer" -> 0;
            case "boolean", "Boolean" -> false;
            case "long", "Long" -> 0L;
            case "double", "Double" -> 0.0;
            case "float", "Float" -> 0.0f;
            case "byte", "Byte" -> (byte) 0;
            case "List" -> Collections.emptyList();
            case "Map" -> Collections.emptyMap();
            case "Set" -> Collections.emptySet();
            case "Queue" -> Collections.asLifoQueue(new LinkedList<>());
            default -> {
                if (type.isArray()) {
                    yield Array.newInstance(type.getComponentType(), 0);
                } else {
                    yield null;
                }
            }
        };
    }
}
