package com.whalefall.learncases.springframework.value.withvallueclazz.spel;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListService {
    @SuppressWarnings("all")
    @Value("#{'${app.supported.languages:en,ch}'.split(',')}")
    private List<String> supportedLanguages;
}
