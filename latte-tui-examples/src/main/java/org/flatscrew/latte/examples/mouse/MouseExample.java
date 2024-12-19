package org.flatscrew.latte.examples.mouse;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.Message;
import org.flatscrew.latte.Model;
import org.flatscrew.latte.Program;
import org.flatscrew.latte.UpdateResult;
import org.flatscrew.latte.command.Printf;
import org.flatscrew.latte.input.MouseMessage;
import org.flatscrew.latte.message.KeyPressMessage;
import org.flatscrew.latte.message.QuitMessage;

public class MouseExample implements Model {

    @Override
    public Command init() {
        return null;
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof MouseMessage mouseMessage) {
            return UpdateResult.from(this, Printf.printf("(X: %d, Y: %d)", mouseMessage.column(), mouseMessage.row()));
        } else if (msg instanceof KeyPressMessage keyPressMessage) {
            if (keyPressMessage.key() == 'q') {
                return UpdateResult.from(this, QuitMessage::new);
            }
        }
        return UpdateResult.from(this);
    }

    @Override
    public String view() {
        return "Do mouse stuff. When you're done press q to quit.\n";
    }

    public static void main(String[] args) {
        new Program(new MouseExample())
                .withMouseAllMotion()
                .run();
    }
}
