package com.whalefall.staticmock;


import com.whalefall.AbstractTest;
import com.whalefall.learncases.staticmock.MyService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Halcyon
 * @date 2024/6/21 20:07
 * @since 1.0.0
 */
class MyService1Test extends AbstractTest {

    @Resource
    private MyService myService;

    @Test
    void testProcessWithDefaultStaticMethod() {
        // 不Mock静态方法，使用默认行为

        // 调用MyService的方法
        String result = myService.process("default input");

        // 验证结果
        assertEquals("Original: default input", result);
    }

}

