package com.whalefall.learncases.design.functionalregistry.v3;


import com.whalefall.learncases.design.functionalregistry.v3.anno.UseTemplate;
import com.whalefall.learncases.design.functionalregistry.v3.service.Business;
import com.whalefall.learncases.design.functionalregistry.v3.template.Template;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * use @UseTemplate annotation to bind Business and Template at startup time
 * @param <T> The type of the parameter and return value of the job
 */
@Slf4j
@Service
@AllArgsConstructor
public class RegisterEnginV3<T, S extends Business<T>> {

    @SuppressWarnings("all")
    private final Map<String, S> businessesMap;
    @SuppressWarnings("all")
    private final Map<String, Template<T, S>> templates;
    private final Map<String, Function<T, T>> registry = new HashMap<>();
    private final ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        Set<String> registerMessages = new HashSet<>();
        businessesMap.forEach((txcode, businessService) -> {
            UseTemplate ann = businessService.getClass().getAnnotation(UseTemplate.class);
            if (ann == null) {
                throw new IllegalStateException("Business " + businessService.getClass() + " has no @UseTemplate annotation");
            }
            Class<?> templateClass = ann.value();
            @SuppressWarnings("unchecked")
            Template<T, S> templateToUse = (Template<T, S>) applicationContext.getBean(templateClass);
            registry.put(txcode, param -> templateToUse.handler(txcode, param, businessService));
            registerMessages.add(txcode + " with template: " + templateClass.getName());
        });
        log.info("Registered businesses: {}, length {}", registerMessages, registerMessages.size());
    }

    public T run(String businessType, T params) {
        Function<T, T> fn = registry.get(businessType);
        if (fn == null) throw new IllegalArgumentException("Unknown businessType: " + businessType);
        log.info("Executing job: {}", businessType);
        return fn.apply(params);
    }



}

