//package org.flatscrew.latte.ansi;
//
//
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.MethodSource;
//
//import java.util.stream.Stream;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//class TextWrapperTest {
//
//    @ParameterizedTest(name = "{0}")
//    @MethodSource("wrapCases")
//    void testWrap(String name, String input, int limit, String breakpoints, String expected) {
//        String result = TextWrapper2.wrap(input, limit, breakpoints);
////
////                String result = TextWrapper.wrap(input, limit, breakpoints);
////        String result = WordWrap
////                .from(input)
////                .maxWidth(limit)
////                .insertHyphens(false)
////                .wrap();
//        assertEquals(expected, result, "Test case '" + name + "' failed");
//    }
//
//    public static Stream<Arguments> wrapCases() {
//        return Stream.of(
//                // Basic cases
//                Arguments.of("empty string", "", 0, "", ""),
//                Arguments.of("passthrough", "foobar\n ", 0, "", "foobar\n "),
//                Arguments.of("pass", "foo", 3, "", "foo"),
//                Arguments.of("longer", "the quick brown foxxxxxxxxxxxxxxxx jumped over the lazy dog.", 16, "",
//                        "the quick brown\nfoxxxxxxxxxxxxxx\nxx jumped over\nthe lazy dog."),
//
//                // Whitespace handling
//                Arguments.of("white space", "foo bar foo", 4, "", "foo\nbar\nfoo"),
//                Arguments.of("broken_at_spaces", "foo bars foobars", 4, "", "foo\nbars\nfoob\nars"),
//                Arguments.of("double space", "f  bar foobaz", 6, "", "f  bar\nfoobaz"),
//                Arguments.of("remove white spaces", "foo    \nb   ar   ", 4, "", "foo\nb\nar"),
//
//                // Special characters
//                Arguments.of("hyphen break", "foo-bar", 5, "-", "foo-\nbar"),
//                Arguments.of("tab", "foo\tbar", 3, "", "foo\nbar"),
//                Arguments.of("emoji", "foo🫧foobar", 4, "", "foo\n🫧fo\nobar"),
//
//                // ANSI escape sequences
//                Arguments.of(
//                        "style_code_dont_affect_length",
//                        "\u001B[38;2;249;38;114mfoo\u001B[0m\u001B[38;2;248;248;242m \u001B[0m\u001B[38;2;230;219;116mbar\u001B[0m",
//                        7,
//                        "",
//                        "\u001B[38;2;249;38;114mfoo\u001B[0m\u001B[38;2;248;248;242m \u001B[0m\u001B[38;2;230;219;116mbar\u001B[0m"
//                ),
//                Arguments.of(
//                        "style_code_dont_get_wrapped",
//                        "\u001B[38;2;249;38;114m(\u001B[0m\u001B[38;2;248;248;242mjust another test\u001B[38;2;249;38;114m)\u001B[0m",
//                        7,
//                        "",
//                        "\u001B[38;2;249;38;114m(\u001B[0m\u001B[38;2;248;248;242mjust\nanother\ntest\u001B[38;2;249;38;114m)\u001B[0m"
//                ),
//
//                // Unicode and special scripts
//                Arguments.of(
//                        "asian",
//                        "こんにち",
//                        7,
//                        "",
//                        "こんに\nち"
//                ),
//
//                // Complex text
//                Arguments.of(
//                        "paragraph with styles",
//                        "Lorem ipsum dolor \u001B[1msit\u001B[m amet, consectetur adipiscing elit",
//                        20,
//                        "",
//                        "Lorem ipsum dolor \u001B[1msit\u001B[m\namet, consectetur\nadipiscing elit"
//                ),
//
//                Arguments.of(
//                        "osc8_wrap",
//                        "สวัสดีสวัสดี\u001B]8;;https://example.com\u001B\\ สวัสดีสวัสดี\u001B]8;;\u001B\\",
//                        8,
//                        "",
//                        "สวัสดีสวัสดี\u001B]8;;https://example.com\u001B\\\nสวัสดีสวัสดี\u001B]8;;\u001B\\"
//                )
//        );
//    }
//}