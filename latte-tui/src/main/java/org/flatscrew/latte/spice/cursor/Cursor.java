package org.flatscrew.latte.spice.cursor;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.Message;
import org.flatscrew.latte.Model;
import org.flatscrew.latte.UpdateResult;
import org.flatscrew.latte.cream.Style;
import org.flatscrew.latte.message.BlurMessage;
import org.flatscrew.latte.message.FocusMessage;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

record InitialBlinkMessage() implements Message {
}

record BlinkMessage(int id, int tag) implements Message {
}

record BlinkCanceled() implements Message {

}

public class Cursor implements Model {

    private static final Duration DEFAULT_BLINK_SPEEd = Duration.ofMillis(530);

    private final int id;

    private Duration blinkSpeed;
    private Style style;
    private Style textStyle;
    private String charUnderCursor;
    private int blinkTag;
    private boolean focus;
    private boolean blink;
    private Mode mode;

    public Cursor() {
        this.id = 0;
        this.blinkSpeed = DEFAULT_BLINK_SPEEd;
        this.blink = true;
        this.focus = true;
        this.mode = Mode.CursorBlink;
        this.style = Style.newStyle();
        this.textStyle = Style.newStyle();
    }

    @Override
    public Command init() {
        return this::blink;
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof InitialBlinkMessage) {
            if (mode != Mode.CursorBlink || !focus) {
                return UpdateResult.from(this);
            }
            return UpdateResult.from(this, blinkCommand());
        } else if (msg instanceof FocusMessage) {
            return UpdateResult.from(this, focus());
        } else if (msg instanceof BlurMessage) {
            blur();
            return UpdateResult.from(this);
        } else if (msg instanceof BlinkMessage blinkMessage) {
            if (mode != Mode.CursorBlink || !focus) {
                return UpdateResult.from(this);
            }
            if (blinkMessage.id() != id || blinkMessage.tag() != blinkTag) {
                return UpdateResult.from(this);
            }
            if (mode == Mode.CursorBlink) {
                this.blink = !blink;
                return UpdateResult.from(this, blinkCommand());
            }
            return UpdateResult.from(this);
        } else if (msg instanceof BlinkCanceled) {
            return UpdateResult.from(this);
        }
        return UpdateResult.from(this);
    }

    public Command blinkCommand() {
        if (mode != Mode.CursorBlink) {
            return null;
        }

        blinkTag++;
        final int currentTag = blinkTag;

        return () -> {
            try (ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor()) {
                return executorService
                        .schedule(() -> new BlinkMessage(id, currentTag), blinkSpeed.toMillis(), TimeUnit.MILLISECONDS)
                        .get();
            } catch (InterruptedException | ExecutionException e) {
                return new BlinkCanceled();
            }
        };
    }

    private Message blink() {
        return new InitialBlinkMessage();
    }

    public Command focus() {
        this.focus = true;
        this.blink = this.mode == Mode.CursorHide;
        if (mode == Mode.CursorBlink) {
            return blinkCommand();
        }
        return null;
    }

    public void blur() {
        this.focus = false;
        this.blink = true;
    }

    @Override
    public String view() {
        if (blink) {
            return textStyle.render(charUnderCursor);
        }
        return style.reverse().render(charUnderCursor);
    }

    public Command setMode(Mode mode) {
        this.mode = mode;
        this.blink = (mode == Mode.CursorHide || !focus);

        if (mode == Mode.CursorBlink) {
            return this::blink;
        }
        return null;
    }

    public void setBlinkSpeed(Duration blinkSpeed) {
        this.blinkSpeed = blinkSpeed;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public void setTextStyle(Style textStyle) {
        this.textStyle = textStyle;
    }

    public void setChar(String charUnderCursor) {
        this.charUnderCursor = charUnderCursor;
    }
}