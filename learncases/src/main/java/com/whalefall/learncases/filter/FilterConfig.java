package com.whalefall.learncases.filter;

import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * code from <a href="https://www.cnblogs.com/paddix/p/8365558.html">...</a>
 *
 * @author WhaleFall
 * @date 2022-07-25 22:09
 */
@SuppressWarnings("ALL")
@Configuration
public class FilterConfig {

    @SuppressWarnings("unchecked")
    @Bean
    public FilterRegistrationBean<Filter> registFilter() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new LogFilter());
        registration.addUrlPatterns("/*");
        registration.setName("LogFilter");
        registration.setOrder(1);
        return registration;
    }

}
