package org.flatscrew.latte.examples.altscreentoggle;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.Message;
import org.flatscrew.latte.Model;
import org.flatscrew.latte.Program;
import org.flatscrew.latte.UpdateResult;
import org.flatscrew.latte.cream.color.ANSIColor;
import org.flatscrew.latte.cream.Style;
import org.flatscrew.latte.cream.color.Color;
import org.flatscrew.latte.message.EnterAltScreen;
import org.flatscrew.latte.message.ExitAltScreen;
import org.flatscrew.latte.message.KeyPressMessage;
import org.flatscrew.latte.message.QuitMessage;

enum Mode {
    ALT_SCREEN(" altscreen mode "),
    INLINE(" inline mode ");

    private final String description;

    Mode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

public class AltScreenToggleExample implements Model {

    private static final Style KEYWORD_STYLE = Style.newStyle()
            .foreground(Color.color("204"))
            .background(Color.color("235"));

    private static final Style HELP_STYLE = Style.newStyle()
            .foreground(Color.color("241"));

    private boolean altScreen;
    private boolean quitting;

    @Override
    public Command init() {
        return null;
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            return switch (keyPressMessage.key()) {
                case 'q', 'Q' -> {
                    quitting = true;
                    yield new UpdateResult<>(this, QuitMessage::new);
                }
                case ' ' -> {
                    Command cmd;
                    if (altScreen) {
                        cmd = ExitAltScreen::new;
                    } else {
                        cmd = EnterAltScreen::new;
                    }
                    altScreen = !altScreen;
                    yield new UpdateResult<>(this, cmd);
                }
                default -> new UpdateResult<>(this, null);
            };
        }

        return UpdateResult.from(this);
    }

    @Override
    public String view() {
        if (quitting) {
            return "Bye!\n";
        }

        Mode mode;
        if (altScreen) {
            mode = Mode.ALT_SCREEN;
        } else {
            mode = Mode.INLINE;
        }

        return "\n\n  You're in %s\n\n\n".formatted(KEYWORD_STYLE.render(mode.getDescription())) +
                HELP_STYLE.render("  space: switch modes • q: exit\n");
    }

    public static void main(String[] args) {
        Program program = new Program(new AltScreenToggleExample());
        program.run();
    }
}
