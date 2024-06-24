package com.whalefall.learncases.springframework.value.withvallueclazz.nonspel;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class NestedObjectService {

    private final DatabaseProperties databaseProperties;

    @SuppressWarnings("all")
    public NestedObjectService(DatabaseProperties databaseProperties) {
        this.databaseProperties = databaseProperties;
    }

    @PostConstruct
    public void printDatabaseUrl() {
        log.info(databaseProperties.getUrl());
    }
}

@Data
@Component
@ConfigurationProperties(prefix = "database")
@Slf4j
class DatabaseProperties {

    private String url;
    private String username;
    private String password;
    private Map<String, String> settings;
}
