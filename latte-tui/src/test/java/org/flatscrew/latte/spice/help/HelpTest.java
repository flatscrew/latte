package org.flatscrew.latte.spice.help;

import org.flatscrew.latte.cream.color.NoColor;
import org.flatscrew.latte.spice.key.Binding;
import org.flatscrew.latte.term.TerminalInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.flatscrew.latte.spice.key.Binding.withHelp;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HelpTest {

    @BeforeEach
    void setUp() {
        // Set up no-color terminal info for consistent output
        TerminalInfo.provide(() -> new TerminalInfo(false, new NoColor()));
    }

    @ParameterizedTest
    @MethodSource("provideWidthsAndExpectedOutputs")
    void testFullHelp(int width, String expected) {
        // Setup
        Help help = new Help();
        help.setFullSeparator(" | ");
        help.setWidth(width);
        help.setShowAll(true);

        Binding.BindingOption k = Binding.withKeys("x");

        // Create test bindings
        Binding[][] bindings = new Binding[][]{
                new Binding[]{
                        new Binding(k, withHelp("enter", "continue"))
                },
                new Binding[]{
                        new Binding(k, withHelp("esc", "back")),
                        new Binding(k, withHelp("?", "help"))
                },
                new Binding[]{
                        new Binding(k, withHelp("H", "home")),
                        new Binding(k, withHelp("ctrl+c", "quit")),
                        new Binding(k, withHelp("ctrl+l", "log"))
                }
        };

        // Create test Keys
        KeyMap keyMap = new TestKeyMap(bindings);

        // Act
        String result = help.render(keyMap);

        // Assert
        assertEquals(expected, result);
    }

    private static class TestKeyMap implements KeyMap {
        private final Binding[][] bindings;

        TestKeyMap(Binding[][] bindings) {
            this.bindings = bindings;
        }

        @Override
        public Binding[][] fullHelp() {
            return bindings;
        }

        @Override
        public Binding[] shortHelp() {
            return null;
        }
    }


    private static Stream<Arguments> provideWidthsAndExpectedOutputs() {
        return Stream.of(
                Arguments.of(20, "enter continue …"),
                Arguments.of(30, String.join("\n",
                        "enter continue | esc back …",
                        "                 ?   help  "
                )),
                Arguments.of(40, String.join("\n",
                        "enter continue | esc back | H      home",
                        "                 ?   help   ctrl+c quit",
                        "                            ctrl+l log "
                ))
        );
    }
}