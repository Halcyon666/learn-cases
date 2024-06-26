package com.whalefall.learncases.springframework.value.withvallueclazz.nonspel;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
@Slf4j
public class MethodParameterInjectionService {

    private final String appName;

    public MethodParameterInjectionService(@Value("${app.name}") String appName) {
        this.appName = appName;
    }

    @SuppressWarnings("all")
    @Value("${app.version}")
    public void setAppVersion(String appVersion) {
    }


    @SuppressWarnings("all")
    public void setAppVersion(@Value("${app.version1}") String appVersion1, String hh, @Value("${app.flagA}") Boolean flagA) {
    }
}
