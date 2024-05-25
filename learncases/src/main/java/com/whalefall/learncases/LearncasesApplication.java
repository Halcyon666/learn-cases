package com.whalefall.learncases;

import com.whalefall.spi.ScanPackageConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

//@ComponentScan({"com.whalefall.spi", "com.whalefall.learncases"})
@SpringBootApplication
@EnableScheduling
@Import(ScanPackageConfig.class)
public class LearncasesApplication {

    public static void main(String[] args) {

        SpringApplication.run(LearncasesApplication.class, args);
    }

}
