package com.whalefall.learncases.spel;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpelAnnotationService {

    @MyAnnotation(value = TypeEnum.TYPE1, spel = "#value.toUpperCase()")
    public String get1(String value) {
        return value;
    }
}
