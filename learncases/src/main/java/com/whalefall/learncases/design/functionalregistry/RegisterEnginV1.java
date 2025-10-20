package com.whalefall.learncases.design.functionalregistry;


import com.whalefall.learncases.design.functionalregistry.anno.UseTemplate;
import com.whalefall.learncases.design.functionalregistry.service.Job;
import com.whalefall.learncases.design.functionalregistry.template.Template;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * use @UseTemplate annotation to bind Job and Template at startup time
 * @param <T> The type of the parameter and return value of the job
 */
@Slf4j
@Service
@AllArgsConstructor
public class RegisterEnginV1<T> {

    @SuppressWarnings("all")
    private final Map<String, Job<T>> jobs;
    @SuppressWarnings("all")
    private final Map<String, Template<T>> templates;
    private final Map<String, Function<T, T>> registry = new HashMap<>();
    private final ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        jobs.forEach((jobName, jobService) -> {
            UseTemplate ann = jobService.getClass().getAnnotation(UseTemplate.class);
            if (ann == null) {
                throw new IllegalStateException("Job " + jobService.getClass() + " has no @UseTemplate annotation");
            }
            Class<?> templateClass = ann.value();

            @SuppressWarnings("unchecked")
            Template<T> templateToUse = (Template<T>) applicationContext.getBean(templateClass);
            registry.put(jobName, param -> templateToUse.handler(() -> jobService.doJob(param)));
        });
    }

    public T run(String jobName, T params) {
        Function<T, T> fn = registry.get(jobName);
        if (fn == null) throw new IllegalArgumentException("Unknown jobName: " + jobName);
        log.info("Executing job: {}", jobName);
        return fn.apply(params);
    }


}

