package org.flatscrew.latte.spice.key;

import org.flatscrew.latte.input.key.Key;
import org.flatscrew.latte.input.key.KeyType;
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
                withKeys("k", "up"),
                withHelp("↑/k", "move up")
        );

        // when
        KeyPressMessage keyPressMessage = new KeyPressMessage(new Key(KeyType.KeyUp));
        boolean matches = Binding.matches(keyPressMessage, binding);

        // then
        assertThat(matches).isTrue();

        // when
        keyPressMessage = new KeyPressMessage(new Key(KeyType.KeyRunes, new char[]{'k'}));
        matches = Binding.matches(keyPressMessage, binding);

        // then
        assertThat(matches).isTrue();

        // when
        keyPressMessage = new KeyPressMessage(new Key(KeyType.KeyRunes, new char[]{'x'}));
        matches = Binding.matches(keyPressMessage, binding);

        // then
        assertThat(matches).isFalse();
    }
}