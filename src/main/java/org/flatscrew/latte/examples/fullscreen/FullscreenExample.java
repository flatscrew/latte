package org.flatscrew.latte.examples.fullscreen;

import org.flatscrew.latte.*;
import org.flatscrew.latte.command.Tick;
import org.flatscrew.latte.message.QuitMessage;

import java.time.Duration;
import java.time.LocalDateTime;

class TickMessage implements Message {
    private final LocalDateTime time;

    public TickMessage(LocalDateTime time) {
        this.time = time;
    }
}

public class FullscreenExample implements Model {

    private int seconds;

    public FullscreenExample(int seconds) {
        this.seconds = seconds;
    }

    @Override
    public Command init() {
        return Tick.tick(Duration.ofSeconds(1), TickMessage::new);
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof TickMessage) {
            seconds--;
            if (seconds <= 0) {
                return UpdateResult.from(this, QuitMessage::new);
            }
            return UpdateResult.from(this, Tick.tick(Duration.ofSeconds(1), TickMessage::new));
        }
        return UpdateResult.from(this, null);
    }

    @Override
    public String view() {
        return "\n\n     Hi. This program will exit in %d seconds...".formatted(seconds);
    }

    public static void main(String[] args) {
        Model model = new FullscreenExample(5);
        new Program(model)
                .withAltScreen()
                .run();
    }
}
