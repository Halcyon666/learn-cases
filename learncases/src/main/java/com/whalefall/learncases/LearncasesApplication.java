package com.whalefall.learncases;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan({"com.whalefall.spi","com.whalefall.learncases"})
@SpringBootApplication
@EnableScheduling
public class LearncasesApplication {

    public static void main(String[] args) {

        SpringApplication.run(LearncasesApplication.class, args);
    }

}
