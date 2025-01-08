package org.flatscrew.latte.cream.border;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

class BorderTest {

    private static Stream<Arguments> multiCharBorderCases() {
        return Stream.of(
                // ASCII multi-character border
                Arguments.of(
                        new Border(
                                "---", "===", "|||", "|||",
                                "/\\", "\\\\", "\\\\", "//",
                                "|-", "-|", "-|-", "---", "---"
                        ),
                        3, 3, 3, 3
                ),
                // Mixed ASCII and Unicode
                Arguments.of(
                        new Border(
                                "━━", "──", "║|", "|║",
                                "┏~", "~┓", "┗~", "~┛",
                                "├─", "─┤", "─┼─", "─┬─", "─┴─"
                        ),
                        2, 2, 2, 2
                ),
                // Decorative borders
                Arguments.of(
                        new Border(
                                "•─•", "•─•", "│~│", "│~│",
                                "╭─", "─╮", "╰─", "─╯",
                                "├~", "~┤", "~┼~", "~┬~", "~┴~"
                        ),
                        3, 3, 3, 3
                ),
                // Emoji combinations
                Arguments.of(
                        new Border(
                                "🌟✨", "✨🌟", "🔥│", "│🔥",
                                "🌟", "🌟", "⭐", "⭐",
                                "🔥", "🔥", "💫", "✨", "✨"
                        ),
                        3, 3, 3, 3
                )
        );
    }

    @ParameterizedTest
    @MethodSource("multiCharBorderCases")
    void testMultiCharBorders(Border border, int expectedTop, int expectedRight,
                              int expectedBottom, int expectedLeft) {
        assertEquals(expectedTop, border.getTopSize(), "Top border size mismatch");
        assertEquals(expectedRight, border.getRightSize(), "Right border size mismatch");
        assertEquals(expectedBottom, border.getBottomSize(), "Bottom border size mismatch");
        assertEquals(expectedLeft, border.getLeftSize(), "Left border size mismatch");
    }

    @Test
    void testAsymmetricBorder() {
        Border asymmetricBorder = new Border(
                "═══", "---", "║||", "|│|",
                "╔══", "══╗", "╚══", "══╝",
                "║=", "=║", "═╬═", "═╦═", "═╩═"
        );
        assertEquals(3, asymmetricBorder.getTopSize());
        assertEquals(3, asymmetricBorder.getRightSize());
        assertEquals(3, asymmetricBorder.getBottomSize());
        assertEquals(3, asymmetricBorder.getLeftSize());
    }

    @Test
    void testVeryLongBorder() {
        Border longBorder = new Border(
                "━━━━━", "═════", "║|||║", "║|||║",
                "╔════", "════╗", "╚════", "════╝",
                "║====", "====║", "══╬══", "══╦══", "══╩══"
        );
        assertEquals(5, longBorder.getTopSize());
        assertEquals(5, longBorder.getRightSize());
        assertEquals(5, longBorder.getBottomSize());
        assertEquals(5, longBorder.getLeftSize());
    }

    @Test
    void testMixedLengthBorder() {
        Border mixedLengthBorder = new Border(
                "---", "--", "│", "║║",
                "╭─", "─╮", "╰─", "─╯",
                "├──", "──┤", "─┼", "──┬", "┴──"
        );
        assertEquals(3, mixedLengthBorder.getTopSize());
        assertEquals(2, mixedLengthBorder.getRightSize());
        assertEquals(2, mixedLengthBorder.getBottomSize());
        assertEquals(2, mixedLengthBorder.getLeftSize());
    }
}