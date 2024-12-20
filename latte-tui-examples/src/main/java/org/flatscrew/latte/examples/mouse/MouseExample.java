package org.flatscrew.latte.examples.mouse;

import org.flatscrew.latte.*;
import org.flatscrew.latte.input.MouseMessage;
import org.flatscrew.latte.message.KeyPressMessage;
import org.flatscrew.latte.message.QuitMessage;

import static org.flatscrew.latte.Command.printf;

public class MouseExample implements Model {

    @Override
    public Command init() {
        return null;
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof MouseMessage mouseMessage) {
            return UpdateResult.from(
                    this,
                    printf(
                            "(X: %d, Y: %d) %s",
                            mouseMessage.column(),
                            mouseMessage.row(),
                            mouseMessage.describe())
            );
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
