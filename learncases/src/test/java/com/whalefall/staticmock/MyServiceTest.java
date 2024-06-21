package com.whalefall.staticmock;


import com.whalefall.AbstractTest;
import com.whalefall.learncases.staticmock.MyService;
import com.whalefall.learncases.staticmock.Utils;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

/**
 * @author Halcyon
 * @date 2024/6/21 20:07
 * @since 1.0.0
 */
class MyServiceTest extends AbstractTest {

    @Resource
    private MyService myService;

    private MockedStatic<Utils> utilities;

    @BeforeEach
    public void setUp() {
        utilities = mockStatic(Utils.class);
    }

    @AfterEach
    public void tearDown() {
        if (utilities != null) {
            utilities.close();
        }
    }

    @Test
    void testProcess() {
        // Mock静态方法的返回值
        String inputStr = "test inputStr";
        when(Utils.staticMethod(inputStr)).thenReturn("Mocked: test input");

        // 调用MyService的方法
        String result = myService.process(inputStr);

        // 验证结果
        assertEquals("Mocked: test input", result);

        // 验证静态方法被调用
        utilities.verify(() -> Utils.staticMethod(inputStr));
    }

    @Test
    void testProcessWithEmptyInput() {
        // Mock静态方法处理空输入情况
        when(Utils.staticMethod("")).thenReturn("Mocked: ");

        // 调用MyService的方法
        String result = myService.process("");

        // 验证结果
        assertEquals("Mocked: ", result);

        // 验证静态方法被调用
        utilities.verify(() -> Utils.staticMethod(""));
    }

    @Test
    void testProcessThrowsException() {
        // Mock静态方法抛出异常
        when(Utils.staticMethod("exception input")).thenThrow(new RuntimeException("Mocked exception"));

        // 验证调用MyService的方法时抛出异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> myService.process("exception input"));

        // 验证异常信息
        assertEquals("Mocked exception", exception.getMessage());

        // 验证静态方法被调用
        utilities.verify(() -> Utils.staticMethod("exception input"));
    }
}

