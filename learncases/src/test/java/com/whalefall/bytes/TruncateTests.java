package com.whalefall.bytes;

import com.whalefall.learncases.bytes.StringUtil;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * substring according to uft-8 byte sequence length
 * @author Halcyon
 * @date 2024/6/17 19:41
 * @since 1.0.0
 */
class StringUtilTest {

    @Test
    void testTruncateUtf8_noTruncationNeeded()  {
        String input = "Short string";
        int byteLimit = 36;
        String result = StringUtil.truncateUtf8(input, byteLimit);
        assertEquals(input, result, "If the input string is shorter than the byte limit, it should remain unchanged.");
    }

    @Test
    void testTruncateUtf8_truncateAscii()  {
        String input = "This is a test string for UTF-8 truncation.";
        int byteLimit = 36;
        String expected = "This is a test string for UTF-8 trun";
        String result = StringUtil.truncateUtf8(input, byteLimit);
        assertEquals(expected, result, "The string should be truncated correctly at the byte limit.");
    }

    @Test
    void testTruncateUtf8_truncateMultibyte()  {
        String input = "这是一个测试字符串，用于测试UTF-8截取功能。";
        int byteLimit = 36;
        String expected = "这是一个测试字符串，用于";
        String result = StringUtil.truncateUtf8(input, byteLimit);
        assertEquals(expected, result, "The string should be truncated correctly at the byte limit without breaking multibyte characters.");
    }

    @Test
    void testTruncateUtf8_multibyteBoundary()  {
        String input = "这是一个测试字符串，用于测试UTF-8截取功能。";
        int byteLimit = 37;
        String expected = "这是一个测试字符串，用于";
        String result = StringUtil.truncateUtf8(input, byteLimit);
        assertEquals(expected, result, "The string should be truncated at a position that avoids breaking multibyte characters.");
    }

    @Test
    void testTruncateUtf8_emptyString()  {
        String input = "";
        int byteLimit = 36;
        String result = StringUtil.truncateUtf8(input, byteLimit);
        assertEquals(input, result, "An empty string should remain unchanged.");
    }

    @Test
    void testTruncateUtf8_exactLimit()  {
        String input = "这是一个测试字符串，用于测试UTF-8截取功能。";
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
        int byteLimit = bytes.length; // Exact byte length of the input string
        String result = StringUtil.truncateUtf8(input, byteLimit);
        assertEquals(input, result, "If the byte limit is exactly the byte length of the input string, it should remain unchanged.");
    }
}
