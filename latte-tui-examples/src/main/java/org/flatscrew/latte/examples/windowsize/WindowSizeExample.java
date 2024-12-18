package org.flatscrew.latte.examples.windowsize;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.Message;
import org.flatscrew.latte.Model;
import org.flatscrew.latte.Program;
import org.flatscrew.latte.UpdateResult;
import org.flatscrew.latte.message.CheckWindowSizeMessage;
import org.flatscrew.latte.message.KeyPressMessage;
import org.flatscrew.latte.message.QuitMessage;
import org.flatscrew.latte.message.WindowSizeMessage;

import static org.flatscrew.latte.command.Printf.printf;

public class WindowSizeExample implements Model {
    @Override
    public Command init() {
        return null;
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            if (keyPressMessage.key() == 'q') {
                return UpdateResult.from(this, QuitMessage::new);
            }
            return UpdateResult.from(this, CheckWindowSizeMessage::new);
        }
        if (msg instanceof WindowSizeMessage windowSizeMessage) {
            return UpdateResult.from(this, printf("%dx%d", windowSizeMessage.width(), windowSizeMessage.height()));
        }
        return UpdateResult.from(this);
    }

    @Override
    public String view() {
        return "When you're done press q to quit. Press any other key to query the window-size.\n";
    }

    public static void main(String[] args) {
        new Program(new WindowSizeExample()).run();
    }
}
