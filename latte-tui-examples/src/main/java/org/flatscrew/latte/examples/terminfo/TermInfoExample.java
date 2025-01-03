package org.flatscrew.latte.examples.terminfo;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.Message;
import org.flatscrew.latte.Model;
import org.flatscrew.latte.Program;
import org.flatscrew.latte.UpdateResult;
import org.flatscrew.latte.cream.Style;
import org.flatscrew.latte.cream.color.AdaptiveColor;
import org.flatscrew.latte.cream.color.ColorProfile;
import org.flatscrew.latte.message.QuitMessage;

public class TermInfoExample implements Model {

    private final Style testStyle = Style.newStyle().foreground(
            new AdaptiveColor("#FF0000", "#00FF00")
    );

    private ColorProfile colorProfile;

    @Override
    public Command init() {
        return QuitMessage::new;
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        return UpdateResult.from(this);
    }

    @Override
    public String view() {
        return testStyle.render("Test test test");
    }

    public static void main(String[] args) {
        new Program(new TermInfoExample()).run();
    }
}
