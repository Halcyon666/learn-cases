package com.whalefall.learncases.oraclerpad;

public final class OracleRpadLikeUtils {

    private OracleRpadLikeUtils() {
    }

    public static String rpad(String str, int targetWidth) {
        return rpad(str, targetWidth, ' ');
    }

    public static String rpad(String str, int targetWidth, char padChar) {
        if (str == null) {
            return null;
        }
        if (targetWidth <= 0) {
            return "";
        }

        String truncated = truncateByDisplayWidth(str, targetWidth);
        int currentWidth = displayWidth(truncated);

        StringBuilder sb = new StringBuilder(truncated);
        int padWidth = codePointDisplayWidth(padChar);

        while (currentWidth + padWidth <= targetWidth) {
            sb.append(padChar);
            currentWidth += padWidth;
        }

        while (currentWidth < targetWidth) {
            sb.append(' ');
            currentWidth++;
        }

        return sb.toString();
    }

    private static String truncateByDisplayWidth(String str, int maxWidth) {
        if (str == null) {
            return null;
        }
        if (str.isEmpty() || maxWidth <= 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        int width = 0;
        int index = 0;

        while (index < str.length()) {
            int codePoint = str.codePointAt(index);
            int cpWidth = codePointDisplayWidth(codePoint);
            if (width + cpWidth > maxWidth) {
                break;
            }
            sb.appendCodePoint(codePoint);
            width += cpWidth;
            index += Character.charCount(codePoint);
        }

        return sb.toString();
    }

    private static int displayWidth(String str) {
        if (str == null || str.isEmpty()) {
            return 0;
        }

        int width = 0;
        int index = 0;
        while (index < str.length()) {
            int codePoint = str.codePointAt(index);
            width += codePointDisplayWidth(codePoint);
            index += Character.charCount(codePoint);
        }
        return width;
    }

    private static int codePointDisplayWidth(int codePoint) {
        return isWideCodePoint(codePoint) ? 2 : 1;
    }

    private static boolean isWideCodePoint(int codePoint) {
        return isCjkIdeograph(codePoint)
                || isCjkPunctuation(codePoint)
                || isFullWidthForm(codePoint);
    }

    private static boolean isCjkIdeograph(int codePoint) {
        return (codePoint >= 0x4E00 && codePoint <= 0x9FFF)
                || (codePoint >= 0x3400 && codePoint <= 0x4DBF)
                || (codePoint >= 0x20000 && codePoint <= 0x2A6DF)
                || (codePoint >= 0x2A700 && codePoint <= 0x2B73F)
                || (codePoint >= 0x2B740 && codePoint <= 0x2B81F)
                || (codePoint >= 0x2B820 && codePoint <= 0x2CEAF)
                || (codePoint >= 0x2CEB0 && codePoint <= 0x2EBEF)
                || (codePoint >= 0x30000 && codePoint <= 0x3134F)
                || (codePoint >= 0xF900 && codePoint <= 0xFAFF)
                || (codePoint >= 0x2F800 && codePoint <= 0x2FA1F);
    }

    private static boolean isCjkPunctuation(int codePoint) {
        return codePoint >= 0x3000 && codePoint <= 0x303F;
    }

    private static boolean isFullWidthForm(int codePoint) {
        return codePoint >= 0xFF00 && codePoint <= 0xFFEF;
    }
}