package org.flatscrew.latte.spice.runeutil;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SanitizerTest {

    @ParameterizedTest
    @MethodSource("provideTestData")
    void testSanitize(String input, String expectedOutput) {
        // Create the Sanitizer instance with custom replacements
        Sanitizer sanitizer = new Sanitizer(
                Sanitizer.replaceNewlines("XX"),
                Sanitizer.replaceTabs("")
        );

        char[] inputRunes = input.toCharArray();
        char[] resultRunes = sanitizer.sanitize(inputRunes);
        String result = new String(resultRunes);

        assertEquals(expectedOutput, result,
                String.format("Input: %s | Expected: %s | Got: %s", input, expectedOutput, result));
    }

    // Method source for test data
    private static Stream<Object[]> provideTestData() {
        return Stream.of(
                new Object[]{"Some poliśh łetters", "Some poliśh łetters"},
                new Object[]{"some spaces are here", "some spaces are here"},
                new Object[]{"width", "width"},
                new Object[]{"\n", "XX"},
                new Object[]{"\na\n", "XXaXX"},
                new Object[]{"\n\n", "XXXX"},
                new Object[]{"\t", ""},
                new Object[]{"hello", "hello"},
                new Object[]{"hel\nlo", "helXXlo"},
                new Object[]{"hel\rlo", "helXXlo"},
                new Object[]{"hel\tlo", "hello"},
                new Object[]{"he\n\nl\tlo", "heXXXXllo"},
                new Object[]{"he\tl\n\nlo", "helXXXXlo"},
                new Object[]{new String(new char[]{'h', 'e', 'l', 'l', 'o', (char) 65533}), "hello"}
        );
    }
}
