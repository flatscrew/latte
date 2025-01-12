package org.flatscrew.latte.cream.join;

import org.flatscrew.latte.cream.align.Position;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class VerticalJoinDecoratorTest {

    @ParameterizedTest
    @MethodSource("joinVerticalArguments")
    void test_ShouldJoinVertical(String caseName, Position position, String first, String second, String expected) {
        // when
        String joined = VerticalJoinDecorator.joinVertical(position, first, second);

        // then
        assertThat(joined).isEqualTo(expected);
    }

    private static Stream<Arguments> joinVerticalArguments() {
        return Stream.of(
                Arguments.of("pos0", Position.Left, "A", "BBBB", "A   \nBBBB"),
                Arguments.of("pos1", Position.Right, "A", "BBBB", "   A\nBBBB"),
                Arguments.of("pos0.25", new Position(0.25d), "A", "BBBB", " A  \nBBBB")
        );
    }
}