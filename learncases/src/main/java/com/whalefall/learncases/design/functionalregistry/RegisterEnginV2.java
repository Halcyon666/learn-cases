package com.whalefall.learncases.design.functionalregistry;


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
 * use parameter to bind Job and Template at runtime time
 * @param <T>
 */
@Slf4j
@Service
@AllArgsConstructor
public class RegisterEnginV2<T> {

    @SuppressWarnings("all")
    private final Map<String, Job<T>> jobs;
    @SuppressWarnings("all")
    private final Map<String, Template<T>> templates;
    private final Map<String, Function<T, T>> registry = new HashMap<>();
    private final ApplicationContext applicationContext;

    // if only a few times to call run method, so pass template class each time
    @PostConstruct
    public void init() {
        jobs.forEach((jobName, jobService) -> registry.put(jobName, jobService::doJob));
    }

    public T run(String jobName, T params, Class<? extends Template<T>> templateClass) {
        Function<T, T> fn = registry.get(jobName);
        if (fn == null) throw new IllegalArgumentException("Unknown jobName: " + jobName);
        log.info("Executing job: {}", jobName);
        return applicationContext.getBean(templateClass).handler(() -> fn.apply(params));
    }

}

