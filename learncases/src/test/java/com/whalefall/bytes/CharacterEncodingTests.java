package com.whalefall.bytes;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Halcyon
 * @date 2024/6/10 20:29
 * @since 1.0.0
 */
@Slf4j
class CharacterEncodingTests {

    private static int getGBKLength(String character) {
        return character.getBytes(Charset.forName("GBK")).length;
    }

    @Test
    void testCharacterEncoding() {
        // Test 1: Unicode character length
        String unicodeString = "\uE863䶮";
        log.info("{} length: {}", unicodeString, unicodeString.length());
        assertEquals(2, unicodeString.length(), "Unicode string length should be 2");

        // Test 2: Length of bytes in GBK encoding
        String mixedString = "我a";
        log.info("{} GBK byte length: {}", mixedString, getGBKLength(mixedString));
        assertEquals(3, getGBKLength(mixedString), "Length of '我a' in GBK encoding should be 3");

        // Test 3: Length of bytes for a specific character in GBK encoding
        String chineseCharacter = "䶮";
        log.info("{} GBK byte length: {}", chineseCharacter, getGBKLength(chineseCharacter));
        assertEquals(1, getGBKLength(chineseCharacter), "Length of '䶮' in GBK encoding should be 1");

        // Test 4: Length of bytes for a specific Unicode character in GBK encoding
        String unicodeCharacter = "\uE863";
        log.info("{} GBK byte length: {}", unicodeCharacter, getGBKLength(unicodeCharacter));
        assertEquals(2, getGBKLength(unicodeCharacter), "Length of '\uE863' in GBK encoding should be 2");

        // Test 5: Length of bytes in UTF-8 encoding
        String utf8String = "\uE863䶮";
        log.info("{} UTF-8 byte length: {}", utf8String, utf8String.getBytes(StandardCharsets.UTF_8).length);
        assertEquals(6, utf8String.getBytes(StandardCharsets.UTF_8).length, "Length of '\uE863䶮' in UTF-8 encoding should be 6");
    }
}
