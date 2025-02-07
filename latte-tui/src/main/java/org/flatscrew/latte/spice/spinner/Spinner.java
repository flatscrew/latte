package org.flatscrew.latte.spice.spinner;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.Message;
import org.flatscrew.latte.Model;
import org.flatscrew.latte.UpdateResult;
import org.flatscrew.latte.cream.Style;

import java.time.LocalDateTime;

public class Spinner implements Model {

    private SpinnerType type;
    private int frame;
    private int id;
    private int tag;
    private Style style = Style.newStyle();

    public Spinner(SpinnerType type) {
        this.type = type;
    }

    public void setType(SpinnerType type) {
        this.type = type;
    }

    public Spinner setStyle(Style style) {
        this.style = style;
        return this;
    }

    public Command init() {
        return this::tick;
    }

    public UpdateResult<Spinner> update(Message msg) {
        if (msg instanceof TickMessage tickMessage) {
            if (tickMessage.id() > 0 && tickMessage.id() != id) {
                return UpdateResult.from(this);
            }
            if (tickMessage.tag() > 0 && tickMessage.tag() != tag) {
                return UpdateResult.from(this);
            }

            frame++;
            if (frame >= type.frames().length) {
                frame = 0;
            }

            tag++;
            return UpdateResult.from(
                    this,
                    Command.tick(type.duration(), localDateTime -> new TickMessage(localDateTime, tag, id))
            );
        }

        return UpdateResult.from(this);
    }

    public String view() {
        if (frame >= type.frames().length) {
            return "(error)";
        }
        return style.render(type.frames()[frame]);
    }

    public Message tick() {
        return new TickMessage(LocalDateTime.now(), tag, id);
    }
}
