package com.whalefall.learncases.bytes;

import java.nio.charset.StandardCharsets;

/**
 * @author Halcyon
 * @date 2024/6/17 19:39
 * @since 1.0.0
 */
public class StringUtil {

    private StringUtil() {
    }

    public static String truncateUtf8(String input, int byteLimit) {
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
        if (bytes.length <= byteLimit) {
            return input;
        }

        int cutOffIndex = byteLimit;
        while (cutOffIndex > 0 && (bytes[cutOffIndex] & 0xC0) == 0x80) {
            cutOffIndex--;
        }

        return new String(bytes, 0, cutOffIndex, StandardCharsets.UTF_8);
    }


}
