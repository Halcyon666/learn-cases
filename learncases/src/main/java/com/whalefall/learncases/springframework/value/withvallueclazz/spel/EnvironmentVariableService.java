package com.whalefall.learncases.springframework.value.withvallueclazz.spel;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EnvironmentVariableService {
    @SuppressWarnings("all")
    @Value("#{systemEnvironment['JAVA_HOME']}")
    private String javaHome;
}
