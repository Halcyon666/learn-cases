package com.whalefall.learncases.springframework.value.withvallueclazz.spel;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class MapService {
    @SuppressWarnings("all")
    @Value("#{${app.settings:#{null}} ?: {'key1':'default1', 'key2':'default2', 'key3':'default3'}}")
    private Map<String, String> appSettings;

    @SuppressWarnings("all")
    @Value("#{${app.settings1}}")
    private Map<String, String> appSettings1;
    @PostConstruct
    public void printSettings() {
        appSettings.forEach((key, value) -> log.info("{}: {}", key, value));
    }
}
