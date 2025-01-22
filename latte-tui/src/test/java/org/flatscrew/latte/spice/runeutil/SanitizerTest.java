package org.flatscrew.latte.spice.runeutil;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.charset.StandardCharsets;
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

        // Convert input string to a char array, decoding UTF-8 runes
        char[] inputRunes = decodeToRunes(input);

        // Sanitize the input and convert the result back to a string
        char[] resultRunes = sanitizer.sanitize(inputRunes);
        String result = new String(resultRunes);

        // Assert the sanitized output matches the expected output
        assertEquals(expectedOutput, result,
                String.format("Input: %s | Expected: %s | Got: %s", input, expectedOutput, result));
    }

    // Method source for test data
    private static Stream<Object[]> provideTestData() {
        return Stream.of(
                new Object[]{"", ""},
                new Object[]{"x", "x"},
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
                new Object[]{"hello\u00C2", "hello"}  // Invalid UTF-8 sequence represented as a single char
        );
    }

    // Helper method to decode UTF-8 runes into a char array
    private char[] decodeToRunes(String input) {
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
        StringBuilder runesBuilder = new StringBuilder();
        for (int i = 0; i < bytes.length; ) {
            int r = Character.codePointAt(new String(bytes, i, bytes.length - i, StandardCharsets.UTF_8), 0);
            if (Character.isValidCodePoint(r)) {
                runesBuilder.appendCodePoint(r);
                i += Character.charCount(r);
            } else {
                // Skip invalid UTF-8 characters
                i++;
            }
        }
        return runesBuilder.toString().toCharArray();
    }
}
