package com.whalefall.learncases.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.io.IOException;

/**
 * @author WhaleFall
 * @date 2022-07-25 22:11
 */
@Configuration
@Order(-1)
@WebFilter(urlPatterns = "/*", filterName = "logFilter2")
@Slf4j
public class LogFilter2 implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        long start = System.currentTimeMillis();
        filterChain.doFilter(servletRequest, servletResponse);
        log.info("Log Filter2 Execute cost={}", System.currentTimeMillis() - start);
    }
}
