package com.whalefall.learncases.springframework.value.withvallueclazz.spel;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SystemPropertyService {
    // no need to pass anything
    @SuppressWarnings("all")
    @Value("#{systemProperties['user.home']}")
    private String userHome;
}
