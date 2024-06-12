package com.whalefall.callc;


import com.whalefall.learncases.callc.HelloJNI;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 * @author Halcyon
 * @date 2024/6/12 23:19
 * @since 1.0.0
 */

@Slf4j
public class HelloJNITest {
    static {
        String projectRoot = System.getProperty("user.dir");
        String libPath = projectRoot + "/lib/libhello.dll";
        System.load(libPath);
        log.info("successfully loaded {}", libPath);
    }

    @Test
    public void testSayHello() {
        HelloJNI helloJNI = new HelloJNI();
        String s = helloJNI.sayHello();
        Assertions.assertEquals("Hello from C!", s);
    }
}

