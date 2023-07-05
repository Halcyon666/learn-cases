package com.whalefall.learncases;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"com.whalefall.spi","com.whalefall.learncases"})
@SpringBootApplication
public class LearncasesApplication {

    public static void main(String[] args) {

        SpringApplication.run(LearncasesApplication.class, args);
    }

}
