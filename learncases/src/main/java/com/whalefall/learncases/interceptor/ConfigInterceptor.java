package com.whalefall.learncases.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author WhaleFall
 * @date 2022-07-25 21:48
 */
@Configuration
public class ConfigInterceptor implements WebMvcConfigurer {
    private final LogInterceptor interceptor;

    public ConfigInterceptor(LogInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor)
                .addPathPatterns("/**");
    }
}
