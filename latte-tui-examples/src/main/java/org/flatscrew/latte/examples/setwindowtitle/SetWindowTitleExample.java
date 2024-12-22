package org.flatscrew.latte.examples.setwindowtitle;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.Message;
import org.flatscrew.latte.Model;
import org.flatscrew.latte.Program;
import org.flatscrew.latte.UpdateResult;
import org.flatscrew.latte.message.KeyPressMessage;
import org.flatscrew.latte.message.QuitMessage;

import static org.flatscrew.latte.Command.setWidowTitle;

public class SetWindowTitleExample implements Model {

    @Override
    public Command init() {
        return setWidowTitle("Latte Example");
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage) {
            return UpdateResult.from(this, QuitMessage::new);
        }
        return UpdateResult.from(this);
    }

    @Override
    public String view() {
        return "\nPress any key to quit.";
    }

    public static void main(String[] args) {
        new Program(new SetWindowTitleExample()).run();
    }
}
