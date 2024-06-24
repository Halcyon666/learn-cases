package com.whalefall.learncases.springframework.value;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j

public class AnnotationScanner {

    private AnnotationScanner() {
    }



    @SuppressWarnings("all")
    public static Map<String, PropertyInfo> scanPackageForValueAnnotations(String packageName) {
        Map<String, PropertyInfo> properties = new HashMap<>();

        try {
            String packagePath = packageName.replace('.', '/');
            Path path = Paths.get(ClassLoader.getSystemResource(packagePath).toURI());
            try (Stream<Path> walk = Files.walk(path)) {

                Set<Class<?>> classes = walk.filter(Files::isRegularFile)
                        .filter(p -> p.toString().endsWith(".class"))
                        .map(p -> {
                            String className = packageName + "." + getClassName(path, p);
                            try {
                                return Class.forName(className);
                            } catch (ClassNotFoundException e) {
                                log.info(e.getMessage(), e);
                                return null;
                            }
                        }).filter(Objects::nonNull)
                        .collect(Collectors.toSet());

                for (Class<?> clazz : classes) {
                    for (Field field : clazz.getDeclaredFields()) {
                        if (field.isAnnotationPresent(Value.class)) {
                            extractPlaceholders(field.getAnnotation(Value.class).value(), field.getType(), properties);
                        }
                    }

                    for (Method method : clazz.getDeclaredMethods()) {
                        // 处理方法级别的 @Value 注解
                        if (method.isAnnotationPresent(Value.class)) {
                            Value valueAnnotation = method.getAnnotation(Value.class);
                            extractPlaceholders(valueAnnotation.value(), method.getParameterTypes()[0], properties);
                        }

                        // 处理方法参数级别的 @Value 注解
                        Parameter[] parameters = method.getParameters();
                        for (Parameter parameter : parameters) {
                            if (parameter.isAnnotationPresent(Value.class)) {
                                Value valueAnnotation = parameter.getAnnotation(Value.class);
                                extractPlaceholders(valueAnnotation.value(), parameter.getType(), properties);
                            }
                        }
                    }


                    for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                        for (Parameter parameter : constructor.getParameters()) {
                            if (parameter.isAnnotationPresent(Value.class)) {
                                extractPlaceholders(parameter.getAnnotation(Value.class).value(), parameter.getType(), properties);
                            }
                        }
                    }
                }
            }

        } catch (IOException | URISyntaxException e) {
            log.error(e.getMessage(), e);
        }

        return properties;
    }

    private static void extractPlaceholders(String value, Class<?> type, Map<String, PropertyInfo> properties) {
        String placeholder = null;
        // todo support SPEL
        if (value.startsWith("#{") && value.endsWith("}")) {
            String innerValue = value.substring(2, value.length() - 1);
            if (innerValue.startsWith("${") && innerValue.endsWith("}")) {
                placeholder = innerValue.substring(2, innerValue.length() - 1);
            }
        } else if (value.startsWith("${") && value.contains(":")) {
            placeholder = value.substring(2, value.indexOf(':'));
        } else if (value.startsWith("${") && value.endsWith("}")) {
            placeholder = value.substring(2, value.length() - 1);
        }

        if (placeholder != null) {
            properties.put(placeholder, new PropertyInfo(placeholder, type));
        }
    }

    private static String getClassName(Path root, Path file) {
        String className = root.relativize(file).toString();
        return className.substring(0, className.length() - 6).replace('/', '.').replace('\\', '.');
    }

    public record PropertyInfo(String name, Class<?> type) {
    }
}
