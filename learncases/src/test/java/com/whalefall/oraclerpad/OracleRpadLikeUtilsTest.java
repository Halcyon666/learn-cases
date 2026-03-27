package com.whalefall.oraclerpad;

import com.whalefall.learncases.oraclerpad.OracleRpadLikeUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class OracleRpadLikeUtilsTest {

    @Test
    void shouldReturnNullWhenInputIsNull() {
        assertNull(OracleRpadLikeUtils.rpad(null, 10));
        assertNull(OracleRpadLikeUtils.rpad(null, 10, '0'));
    }

    @Test
    void shouldReturnEmptyWhenTargetWidthIsZeroOrNegative() {
        assertEquals("", OracleRpadLikeUtils.rpad("abc", 0));
        assertEquals("", OracleRpadLikeUtils.rpad("abc", -1));
    }

    @Test
    void shouldPadAsciiWithSpaces() {
        assertEquals("ABC   ", OracleRpadLikeUtils.rpad("ABC", 6));
    }

    @Test
    void shouldPadChineseWithSpaces() {
        assertEquals("中文  ", OracleRpadLikeUtils.rpad("中文", 6));
    }

    @Test
    void shouldPadMixedTextWithSpaces() {
        assertEquals("A中B  ", OracleRpadLikeUtils.rpad("A中B", 6));
    }

    @Test
    void shouldPadChinesePunctuationAsWideChar() {
        assertEquals("中，  ", OracleRpadLikeUtils.rpad("中，", 6));
    }

    @Test
    void shouldPadFullWidthCharsAsWideChar() {
        assertEquals("Ａ  ", OracleRpadLikeUtils.rpad("Ａ", 4));
        assertEquals("ＡＢ  ", OracleRpadLikeUtils.rpad("ＡＢ", 6));
        assertEquals("１２  ", OracleRpadLikeUtils.rpad("１２", 6));
    }

    @Test
    void shouldPadWithCustomAsciiChar() {
        assertEquals("ABC000", OracleRpadLikeUtils.rpad("ABC", 6, '0'));
    }

    @Test
    void shouldPadWithWideCharAndFillRemainderWithSpace() {
        assertEquals("A中 ", OracleRpadLikeUtils.rpad("A", 4, '中'));
    }

    @Test
    void shouldKeepOriginalWhenWidthExactlyMatches() {
        assertEquals("中文A", OracleRpadLikeUtils.rpad("中文A", 5));
        assertEquals("ＡB中", OracleRpadLikeUtils.rpad("ＡB中", 5));
    }

    @Test
    void shouldTruncateAsciiWhenSourceExceedsTargetWidth() {
        assertEquals("ABC", OracleRpadLikeUtils.rpad("ABCDE", 3));
    }

    @Test
    void shouldTruncateChineseWhenSourceExceedsTargetWidth() {
        assertEquals("中文", OracleRpadLikeUtils.rpad("中文A", 4));
    }

    @Test
    void shouldTruncateFullWidthCharsWhenSourceExceedsTargetWidth() {
        assertEquals("Ａ", OracleRpadLikeUtils.rpad("ＡＢ", 2));
        assertEquals("ＡＢ", OracleRpadLikeUtils.rpad("ＡＢC", 4));
    }

    @Test
    void shouldSupportRareChineseCharacterInSupplementaryPlane() {
        String rare = new String(Character.toChars(0x20000));
        assertEquals(rare + "  ", OracleRpadLikeUtils.rpad(rare, 4));
        assertEquals(rare + "A ", OracleRpadLikeUtils.rpad(rare + "A", 4));
    }

    @Test
    void shouldHandleRareChineseCharacterCorrectly() {
        String rare = new String(Character.toChars(0x20000));

        assertEquals(rare + "  ", OracleRpadLikeUtils.rpad(rare, 4));
        assertEquals(rare + "A", OracleRpadLikeUtils.rpad(rare + "A", 3));
        assertEquals(rare + "A", OracleRpadLikeUtils.rpad(rare + "AB", 3));
        assertEquals(" ", OracleRpadLikeUtils.rpad(rare, 1));
    }

    @Test
    void shouldNotBreakSurrogatePairWhenTruncating() {
        String emoji = "\uD83D\uDE00";
        assertEquals("", OracleRpadLikeUtils.rpad(emoji, 0));
        assertEquals(emoji, OracleRpadLikeUtils.rpad(emoji, 1));
    }
}