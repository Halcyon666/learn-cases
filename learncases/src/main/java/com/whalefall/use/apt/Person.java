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

    private String name;

    private int age;

    private boolean active;

    private double score;

    private String address;

    private String email;

    private String city;

    @BuilderProperty(required = true, order = 1)
    public void setName(String name) {
        this.name = name;
    }

    @BuilderProperty(required = true, order = 2)
    public void setAge(int age) {
        this.age = age;
    }

    @BuilderProperty(required = false, defaultValue = "true", order = 3)
    public void setActive(boolean active) {
        this.active = active;
    }

    @BuilderProperty(required = false, defaultValue = "0.0", order = 4)
    public void setScore(double score) {
        this.score = score;
    }

    @BuilderProperty(required = false, defaultValue = "Unknown", order = 5)
    public void setAddress(String address) {
        this.address = address;
    }

    @BuilderProperty(required = false, order = 6)
    public void setEmail(String email) {
        this.email = email;
    }

    @BuilderProperty(required = false, defaultValue = "New York", order = 7)
    public void setCity(String city) {
        this.city = city;
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onEvent(Person p) {
        log.info("p = {}", p);
    }
}
