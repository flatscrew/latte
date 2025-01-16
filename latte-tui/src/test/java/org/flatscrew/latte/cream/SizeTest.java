package org.flatscrew.latte.cream;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SizeTest {

    @Test
    void test_HeightWithNewLines() {
        // when
        int height = Size.height("  \n  ");

        // then
        assertThat(height).isEqualTo(2);

        // when
        height = Size.height(" Foo Document\n The Foo Files\n     ");

        // then
        assertThat(height).isEqualTo(3);

        // when
        height = Size.height("\n");

        // then
        assertThat(height).isEqualTo(2);

    }
}