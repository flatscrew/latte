package org.flatscrew.latte.examples;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.Message;
import org.flatscrew.latte.Model;
import org.flatscrew.latte.Program;
import org.flatscrew.latte.UpdateResult;
import org.flatscrew.latte.examples.altscreentoggle.AltScreenToggleExample;
import org.flatscrew.latte.examples.counter.CounterExample;
import org.flatscrew.latte.examples.demo.Demo;
import org.flatscrew.latte.examples.fullscreen.FullscreenExample;
import org.flatscrew.latte.message.KeyPressMessage;
import org.flatscrew.latte.message.QuitMessage;

import java.util.List;

public class ExamplesRunner implements Model {

    private final List<Model> exampleModels;
    private int cursor;
    private Model selectedModel;

    public ExamplesRunner(List<Model> exampleModels) {
        this.exampleModels = exampleModels;
    }

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
                case 13 -> new UpdateResult<>(this.makeChoice(), null);
                case 'q', 'Q' -> new UpdateResult<>(this, QuitMessage::new);
                default -> new UpdateResult<>(this, null);
            };
        }
        return new UpdateResult<>(this, null);    }

    @Override
    public String view() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("Pick an example:\n");
        buffer.append("~~~~~~~~~~~~~~~~\n\n");

        for (int index = 0; index < exampleModels.size(); index++) {
            if (cursor == index) {
                buffer.append("(•) ");
            } else {
                buffer.append("( ) ");

            }
            buffer.append(exampleModels.get(index).getClass().getSimpleName()).append("\n");
        }
        buffer.append("\n(press q to quit)");
        return buffer.toString();
    }

    private Model makeChoice() {
        for (int index = 0; index < exampleModels.size(); index++) {
            if (index == cursor) {
                return exampleModels.get(index);
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
        if (cursor + 1 >= exampleModels.size()) {
            cursor = 0;
            return this;
        }
        cursor++;
        return this;
    }

    public static void main(String[] args) {
        new Program(new ExamplesRunner(List.of(
                new CounterExample(),
                new Demo(),
                new AltScreenToggleExample(),
                new FullscreenExample(5)
        ))).run();
    }
}