package org.flatscrew.latte.spice.key;

import org.flatscrew.latte.message.KeyPressMessage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.flatscrew.latte.spice.key.Binding.withHelp;
import static org.flatscrew.latte.spice.key.Binding.withKeys;

class BindingTest {

    @Test
    void test_ShouldTestEnabledScenario() {
        // given
        Binding binding = new Binding(
                withKeys('k', 65),
                withHelp("↑/k", "move up")
        );

        KeyPressMessage keyPressMessage = new KeyPressMessage(65);

        // when
        boolean matches = Binding.matches(keyPressMessage, binding);

        // then
        assertThat(matches).isTrue();
    }
}