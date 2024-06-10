package com.whalefall.override;

import com.whalefall.learncases.override.Father;
import com.whalefall.learncases.override.Son;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TestClassTest {

    @Test
    void testSonStaticWork1() {
        // 测试静态方法work1()的输出
        String expectedOutputSon = "son work";
        String actualOutputSon = captureOutput(Son::work1);
        assertTrue(actualOutputSon.contains(expectedOutputSon));
    }

    @Test
    void testFatherStaticWork1() {
        String expectedOutputFather = "father work";
        String actualOutputFather = captureOutput(Father::work1);
        assertTrue(actualOutputFather.contains(expectedOutputFather));
    }

    @Test
    void testSonNonStatic() {
        // 测试实例方法print()的输出
        Son son = new Son();
        String expectedOutputSon = "son, hello world";
        String actualOutputSon = captureOutput(() -> son.print(expectedOutputSon));
        assertTrue(actualOutputSon.contains(expectedOutputSon));

    }

    @Test
    void testFatherNonStatic() {
        Father father = new Father();
        String expectedOutputFather = "Father Hello World";
        String actualOutputFather = captureOutput(() -> father.print(expectedOutputFather));
        assertTrue(actualOutputFather.contains(expectedOutputFather));
    }

    private String captureOutput(Runnable runnable) {
        // 重定向输出到字符串
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        runnable.run();
        // 恢复原始输出流
        System.setOut(originalOut);
        return outputStream.toString().trim();
    }
}
