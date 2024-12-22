package org.flatscrew.latte.examples.cursor;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.Message;
import org.flatscrew.latte.Model;
import org.flatscrew.latte.Program;
import org.flatscrew.latte.UpdateResult;
import org.flatscrew.latte.message.KeyPressMessage;
import org.flatscrew.latte.message.QuitMessage;
import org.flatscrew.latte.spice.cursor.Cursor;

public class CursorExample implements Model {

    private final Cursor cursor;

    public CursorExample() {
        this.cursor = new Cursor();
        cursor.setChar(" ");
    }

    @Override
    public Command init() {
        return cursor.init();
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage) {
            return UpdateResult.from(this, QuitMessage::new);
        }

        UpdateResult<? extends Model> updateResult = cursor.update(msg);
        return UpdateResult.from(this, updateResult.command());
    }

    @Override
    public String view() {
        return "This cursor is not real... " + cursor.view();
    }

    public static void main(String[] args) {
        new Program(new CursorExample())
                .withAltScreen()
                .withReportFocus()
                .run();
    }
}
