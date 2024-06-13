package com.whalefall.callc;


import com.whalefall.learncases.callc.HelloJNI;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Halcyon
 * @date 2024/6/12 23:19
 * @since 1.0.0
 */

@Slf4j
class HelloJNITest {
    static {
        String projectRoot = System.getProperty("user.dir");
        String osName = System.getProperty("os.name").toLowerCase();
        String libPath;

        if (osName.contains("win")) {
            libPath = projectRoot + "/lib/libhello.dll";
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("mac")) {
            libPath = projectRoot + "/lib/libhello.so";
        } else {
            throw new UnsupportedOperationException("Unsupported operating system: " + osName);
        }

        System.load(libPath);
        log.info("Successfully loaded {}", libPath);
    }

    @Test
    void testSayHello() {
        HelloJNI helloJNI = new HelloJNI();
        String s = helloJNI.sayHello();
        Assertions.assertEquals("Hello from C!", s);
    }
}


