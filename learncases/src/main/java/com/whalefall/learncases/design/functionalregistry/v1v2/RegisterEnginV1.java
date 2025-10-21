package com.whalefall.learncases.design.functionalregistry.v1v2;


import com.whalefall.learncases.design.functionalregistry.v1v2.anno.UseTemplate;
import com.whalefall.learncases.design.functionalregistry.v1v2.service.Business;
import com.whalefall.learncases.design.functionalregistry.v1v2.template.Template;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * use @UseTemplate annotation to bind Business and Template at startup time
 * @param <T> The type of the parameter and return value of the job
 */
@Slf4j
@Service
@AllArgsConstructor
public class RegisterEnginV1<T> {

    @SuppressWarnings("all")
    private final Map<String, Business<T>> jobs;
    @SuppressWarnings("all")
    private final Map<String, Template<T>> templates;
    private final Map<String, Function<T, T>> registry = new HashMap<>();
    private final ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        jobs.forEach((jobName, businessService) -> {
            UseTemplate ann = businessService.getClass().getAnnotation(UseTemplate.class);
            if (ann == null) {
                throw new IllegalStateException("Business " + businessService.getClass() + " has no @UseTemplate annotation");
            }
            Class<?> templateClass = ann.value();

            @SuppressWarnings("unchecked")
            Template<T> templateToUse = (Template<T>) applicationContext.getBean(templateClass);
            registry.put(jobName, param -> templateToUse.handler(() -> businessService.doJob(param)));
        });
    }

    public T run(String jobName, T params) {
        Function<T, T> fn = registry.get(jobName);
        if (fn == null) throw new IllegalArgumentException("Unknown jobName: " + jobName);
        log.info("Executing job: {}", jobName);
        return fn.apply(params);
    }


}

