package org.flatscrew.latte.examples.counter;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.Message;
import org.flatscrew.latte.Model;
import org.flatscrew.latte.Program;
import org.flatscrew.latte.UpdateResult;
import org.flatscrew.latte.message.KeyPressMessage;
import org.flatscrew.latte.message.QuitMessage;

public class CounterExample implements Model {

    private final int value;

    public CounterExample() {
        this(0);
    }

    public CounterExample(int value) {
        this.value = value;
    }

    @Override
    public Command init() {
        return null;
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            return switch (keyPressMessage.key()) {
                case "k", "K", "up" -> new UpdateResult<>(this, () -> CounterMsg.INCREMENT);
                case "j", "J", "down" -> new UpdateResult<>(this, () -> CounterMsg.DECREMENT);
                case "d", "D" -> new UpdateResult<>(this, () -> CounterMsg.INCREMENT_LATER);
                case "Q" -> new UpdateResult<>(this, QuitMessage::new);
                default -> new UpdateResult<>(this, null);
            };
        } else if (msg == CounterMsg.INCREMENT) {
            return UpdateResult.from(increment());
        } else if (msg == CounterMsg.DECREMENT) {
            return UpdateResult.from(decrement());
        } else if (msg == CounterMsg.INCREMENT_LATER) {
            return new UpdateResult<>(this, () -> {
                try {
                    Thread.sleep(1000); // Delay for 1 second
                    return CounterMsg.INCREMENT;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return null;
                }
            });
        }
        return new UpdateResult<>(this, null);
    }

    public CounterExample increment() {
        return new CounterExample(value + 1);
    }

    public CounterExample decrement() {
        return new CounterExample(value - 1);
    }

    @Override
    public String view() {
        return """
                Counter Example
                ==============
                              \s
                Value: %d
                              \s
                Commands:
                ↑/k - Increment
                ↓/j - Decrement
                d - Delayed Increment
                q - Quit
               \s""".formatted(value);
    }

    public static void main(String[] args) {
        Program app = new Program(new CounterExample(0));
        app.run();
    }
}
