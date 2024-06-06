package com.whalefall.use.apt;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.greenrobot.eventbus.Subscribe;

/**
 * <a href="https://www.baeldung.com/java-annotation-processing-builder">Code Cp From</a>
 *
 * @author Halcyon
 * @date 2023/7/1
 * @since 1.0.0
 */
@Getter
@Slf4j
public class Person {

    private int age;

    private String name;

    @BuilderProperty
    @SuppressWarnings("unused")
    public void setAge(int age) {
        this.age = age;
    }

    @BuilderProperty
    public void setName(String name) {
        this.name = name;
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onEvent(Person p) {
        log.info("p = {}", p);
    }
}
