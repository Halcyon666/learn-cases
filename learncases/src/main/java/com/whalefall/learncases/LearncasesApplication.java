package com.whalefall.learncases;

import com.whalefall.spi.ScanPackageConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * don't want to import the package, just put {@link LearncasesApplication} to
 * {@code com.whalefall.LearncasesApplication}, auto scan all the classes
 * in the sub-directroy {@code com.whalefall.LearncasesApplication} .
 *
 * <P>but change spi base directory to {@code com.whalefall} does not work.
 *
 * <p>Maybe some better way to do this.
 */
@SpringBootApplication
@EnableScheduling
@Import(ScanPackageConfig.class)
public class LearncasesApplication {

    public static void main(String[] args) {

        SpringApplication.run(LearncasesApplication.class, args);
    }

}
