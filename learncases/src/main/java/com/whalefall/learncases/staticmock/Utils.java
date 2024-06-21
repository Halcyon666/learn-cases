package com.whalefall.learncases.staticmock;

/**
 * @author Halcyon
 * @date 2024/6/21 20:04
 * @since 1.0.0
 */
public class Utils {
    private Utils() {
    }

    public static String staticMethod(String input) {
        return "Original: " + input;
    }

    @SuppressWarnings("all")
    public static Utils createUtils() {
        return new Utils();
    }
}
