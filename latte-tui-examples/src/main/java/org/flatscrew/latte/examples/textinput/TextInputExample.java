package org.flatscrew.latte.examples.textinput;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.Message;
import org.flatscrew.latte.Model;
import org.flatscrew.latte.Program;
import org.flatscrew.latte.UpdateResult;
import org.flatscrew.latte.input.key.KeyAliases;
import org.flatscrew.latte.message.KeyPressMessage;
import org.flatscrew.latte.message.QuitMessage;
import org.flatscrew.latte.spice.textinput.TextInput;

public class TextInputExample implements Model {

    private TextInput textInput;

    public TextInputExample() {
        this.textInput = new TextInput();
        textInput.setPlaceholder("Pikachu");
        textInput.focus();
        textInput.setCharLimit(156);
        textInput.setWidth(20);
    }

    @Override
    public Command init() {
        return textInput::blink;
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            if (KeyAliases.getKeyType(KeyAliases.KeyAlias.KeyEnter) == keyPressMessage.type()
                    || KeyAliases.getKeyType(KeyAliases.KeyAlias.KeyCtrlC) == keyPressMessage.type()
                    || KeyAliases.getKeyType(KeyAliases.KeyAlias.KeyEscape) == keyPressMessage.type()) {
                return UpdateResult.from(this, QuitMessage::new);
            }
        }

        UpdateResult<? extends Model> updateResult = textInput.update(msg);
        return UpdateResult.from(this, updateResult.command());
    }

    @Override
    public String view() {
        return "What’s your favorite Pokémon?\n\n%s\n\n%s\n".formatted(
                textInput.view(),
                "(esc to quit)"
        );
    }

    public static void main(String[] args) {
        new Program(new TextInputExample()).run();
    }
}
