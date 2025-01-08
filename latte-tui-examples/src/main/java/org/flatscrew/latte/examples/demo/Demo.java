package org.flatscrew.latte.examples.demo;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.Message;
import org.flatscrew.latte.Model;
import org.flatscrew.latte.Program;
import org.flatscrew.latte.UpdateResult;
import org.flatscrew.latte.cream.Style;
import org.flatscrew.latte.cream.color.Color;
import org.flatscrew.latte.message.KeyPressMessage;
import org.flatscrew.latte.message.QuitMessage;

public class Demo implements Model {

    private final static Style SELECTION = Style.newStyle().foreground(Color.color("205"));
    private final static String[] CHOICES = {"Espresso", "Americano", "Latte"};

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
                case 'k', 'K', 65 -> UpdateResult.from(this.moveUp());
                case 'j', 'J', 66 -> UpdateResult.from(this.moveDown());
                case 13 -> UpdateResult.from(this.makeChoice(), QuitMessage::new);
                case 'q', 'Q' -> UpdateResult.from(this, QuitMessage::new);
                default -> UpdateResult.from(this);
            };
        }
        return UpdateResult.from(this);
    }

    private Model makeChoice() {
        for (int index = 0; index < CHOICES.length; index++) {
            String choice = CHOICES[index];
            if (index == cursor) {
                this.choice = choice;
                return this;
            }
        }
        return this;
    }

    private Model moveUp() {
        if (cursor - 1 < 0) {
            cursor = CHOICES.length - 1;
            return this;
        }
        cursor--;
        return this;
    }

    private Model moveDown() {
        if (cursor + 1 >= CHOICES.length) {
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

        for (int index = 0; index < CHOICES.length; index++) {
            if (cursor == index) {
                buffer.append(SELECTION.render("[•]", CHOICES[index]));
            } else {
                buffer.append("[ ] ").append(CHOICES[index]);
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
        Demo demoModel = new Demo();
        Program program = new Program(demoModel);
        program.run();

        if (demoModel.getChoice() == null) {
            return;
        }
        System.out.printf("\n---\nYou chose: %s!\n", demoModel.getChoice());
    }
}
