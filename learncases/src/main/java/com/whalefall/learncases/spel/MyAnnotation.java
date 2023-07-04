package com.whalefall.learncases.spel;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAnnotation {

    TypeEnum value();

    String spel() default "";
}
