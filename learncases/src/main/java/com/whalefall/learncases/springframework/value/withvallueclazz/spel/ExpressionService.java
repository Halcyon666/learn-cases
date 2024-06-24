package com.whalefall.learncases.springframework.value.withvallueclazz.spel;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ExpressionService {
    // no need to pass anything
    @SuppressWarnings("all")
    @Value("#{T(java.lang.Math).random() * 100.0}")
    private double randomValue;
}
