package org.flatscrew.latte.examples.demo;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.cream.Color;
import org.flatscrew.latte.cream.Style;
import org.flatscrew.latte.message.KeyPressMessage;
import org.flatscrew.latte.Message;
import org.flatscrew.latte.Model;
import org.flatscrew.latte.Program;
import org.flatscrew.latte.message.QuitMessage;
import org.flatscrew.latte.UpdateResult;

enum Choice {
    ESPRESSO("Espresso"),
    AMERICANO("Americano"),
    LATTE("Latte");

    private final String name;

    Choice(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

public class Demo implements Model  {

    private final static Style SELECTION = new Style().foreground(new Color(205));

    private int cursor;
    private String choice;

    @Override
    public Command init() {
        return null;
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            return switch (keyPressMessage.key()) {
                case 'k', 'K', 65 -> new UpdateResult<>(this.moveUp(), null);
                case 'j', 'J', 66 -> new UpdateResult<>(this.moveDown(), null);
                case 13 -> new UpdateResult<>(this.makeChoice(), QuitMessage::new);
                case 'q', 'Q' -> new UpdateResult<>(this, QuitMessage::new);
                default -> new UpdateResult<>(this, null);
            };
        }
        return new UpdateResult<>(this, null);
    }

    private Model makeChoice() {
        Choice[] values = Choice.values();
        for (Choice choice : values) {
            if (choice.ordinal() == cursor) {
                this.choice =  choice.getName();
                return this;
            }
        }
        return this;
    }

    private Model moveUp() {
        if (cursor - 1 <= 0 ) {
            cursor = 0;
            return this;
        }
        cursor--;
        return this;
    }

    private Model moveDown() {
        if (cursor + 1 >= Choice.values().length) {
            cursor = 0;
            return this;
        }
        cursor++;
        return this;
    }

    @Override
    public String view() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("What kind of Coffee would you like to order?\n\n");

        Choice[] values = Choice.values();
        for (int index = 0; index < values.length; index++) {
            if (cursor == index) {
                buffer.append(SELECTION.render("[•]", values[index].getName()));
            } else {
                buffer.append("[ ] ").append(values[index].getName());
            }
            buffer.append("\n");
        }
        buffer.append("\n(press q to quit)");
        return buffer.toString();
    }

    public String getChoice() {
        return choice;
    }

    public static void main(String[] args) {
        Demo resultModel = new Demo();
        Program program = new Program(resultModel);
        program.run();

        if (resultModel.getChoice() == null) {
            return;
        }
        System.out.printf("\n---\nYou chose: %s!\n", resultModel.getChoice());
    }
}
