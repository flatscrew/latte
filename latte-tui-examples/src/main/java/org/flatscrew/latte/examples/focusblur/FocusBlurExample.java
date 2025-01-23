package org.flatscrew.latte.examples.focusblur;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.Message;
import org.flatscrew.latte.Model;
import org.flatscrew.latte.Program;
import org.flatscrew.latte.UpdateResult;
import org.flatscrew.latte.message.BlurMessage;
import org.flatscrew.latte.message.FocusMessage;
import org.flatscrew.latte.message.KeyPressMessage;
import org.flatscrew.latte.message.QuitMessage;

public class FocusBlurExample implements Model {

    private boolean focused = true;
    private boolean reporting = true;

    @Override
    public Command init() {
        return null;
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof FocusMessage) {
            this.focused = true;
        } else if (msg instanceof BlurMessage) {
            this.focused = false;
        } else if (msg instanceof KeyPressMessage keyPress) {

            switch (keyPress.key()) {
                case "t":
                    this.reporting = !this.reporting;
                    break;
                case "q":
                    return UpdateResult.from(this, QuitMessage::new);
            }
        }
        return UpdateResult.from(this);
    }

    @Override
    public String view() {
        StringBuilder buffer = new StringBuilder("Hi. Focus report is currently ");
        if (this.reporting) {
            buffer.append("enabled");
        } else {
            buffer.append("disabled");
        }
        buffer.append(".\n\n");

        if (reporting) {
            if (focused) {
                buffer.append("This program is currently focused!");
            } else {
                buffer.append("This program is currently blurred!");
            }
        }

        buffer.append("\n\nTo quit sooner press ctrl-c or q, or t to toggle focus reporting...\n");

        return buffer.toString();
    }

    public static void main(String[] args) {
        new Program(new FocusBlurExample())
                .withReportFocus()
                .run();
    }
}
