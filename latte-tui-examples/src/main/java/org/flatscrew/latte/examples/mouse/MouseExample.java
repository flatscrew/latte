package org.flatscrew.latte.examples.mouse;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.Message;
import org.flatscrew.latte.Model;
import org.flatscrew.latte.Program;
import org.flatscrew.latte.UpdateResult;
import org.flatscrew.latte.input.MouseMessage;
import org.flatscrew.latte.input.key.KeyType;
import org.flatscrew.latte.message.KeyPressMessage;
import org.flatscrew.latte.message.QuitMessage;
import org.flatscrew.latte.message.UnknownSequenceMessage;

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
            if (keyPressMessage.key().equals("q") || keyPressMessage.type() == KeyType.keyESC) {
                return UpdateResult.from(this, QuitMessage::new);
            }
            return UpdateResult.from(
                    this,
                    printf("key: %s, alt: %s", keyPressMessage.key(), keyPressMessage.alt() ? "true" : "false")
            );
        } else if (msg instanceof UnknownSequenceMessage unknownSequence) {
            return UpdateResult.from(
                    this,
                    printf("unknown sequence: %s", unknownSequence.sequence())
            );
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
