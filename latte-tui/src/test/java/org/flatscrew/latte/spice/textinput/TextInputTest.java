package org.flatscrew.latte.spice.textinput;


import org.flatscrew.latte.cream.color.NoColor;
import org.flatscrew.latte.term.TerminalInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextInputTest {

    @BeforeEach
    void setUp() {
        // Set up no-color terminal info for consistent output
        TerminalInfo.provide(() -> new TerminalInfo(false, new NoColor()));
    }

    @Test
    public void testCurrentSuggestion() {
        TextInput textInput = new TextInput();
        textInput.setShowSuggestions(true);

        // Initial state: no current suggestion
        String suggestion = textInput.currentSuggestion();
        String expected = "";
        assertEquals(expected, suggestion, "Expected no current suggestion initially");

        // Set suggestions but no value, so no suggestions should match
        textInput.setSuggestions(new String[]{"test1", "test2", "test3"});
        suggestion = textInput.currentSuggestion();
        expected = "";
        assertEquals(expected, suggestion, "Expected no current suggestion after setting suggestions without value");

        // Set a value, update suggestions, and navigate to the next suggestion
        textInput.setValue("test");
        textInput.updateSuggestions();
        textInput.nextSuggestion();
        suggestion = textInput.currentSuggestion();
        expected = "test2";
        assertEquals(expected, suggestion, "Expected first suggestion to be 'test2'");
    }

    @Test
    public void testSlicingOutsideCap() {
        TextInput textInput = new TextInput();
        textInput.setPlaceholder("作業ディレクトリを指定してください");
        textInput.setWidth(32);

        // Rendering the view should not throw an exception
        textInput.view();
    }
}