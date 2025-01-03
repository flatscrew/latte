package org.flatscrew.latte.cream;

import org.flatscrew.latte.cream.color.ColorProfile;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.flatscrew.latte.cream.Renderer.defaultRenderer;
import static org.flatscrew.latte.cream.Style.newStyle;
import static org.flatscrew.latte.cream.color.Color.color;

class RendererTest {

    @ParameterizedTest(name = "color profile {0}")
    @MethodSource("provideTestData")
    void test_ShouldRenderExpectedOutputForGivenColorProfile(String name, ColorProfile colorProfile, String expectedOutput) {
        // given
        Renderer renderer = defaultRenderer();
        String input = "hello";

        // when
        renderer.setColorProfile(colorProfile);
        Style style = newStyle().foreground(color("#5A56E0"));
        String output = style.render(input);

        // then
        assertThat(output).isEqualTo(expectedOutput);
    }

    public static Stream<Arguments> provideTestData() {
        return Stream.of(
                Arguments.of("ascii", ColorProfile.Ascii, "hello"),
                Arguments.of("ansi", ColorProfile.ANSI, "\u001B[94mhello\u001B[0m"),
                Arguments.of("ansi256", ColorProfile.ANSI256, "\u001b[38;5;62mhello\u001b[0m"),
                Arguments.of("truecolor", ColorProfile.TrueColor, "\u001b[38;2;90;86;224mhello\u001b[0m")
        );
    }
}