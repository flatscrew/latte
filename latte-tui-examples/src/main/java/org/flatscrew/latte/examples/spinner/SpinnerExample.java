package org.flatscrew.latte.examples.spinner;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.Message;
import org.flatscrew.latte.Model;
import org.flatscrew.latte.Program;
import org.flatscrew.latte.UpdateResult;
import org.flatscrew.latte.cream.Style;
import org.flatscrew.latte.cream.color.Color;
import org.flatscrew.latte.message.KeyPressMessage;
import org.flatscrew.latte.message.QuitMessage;
import org.flatscrew.latte.spice.spinner.Spinner;
import org.flatscrew.latte.spice.spinner.SpinnerType;

public class SpinnerExample implements Model {

    private Model spinner;

    public SpinnerExample() {
        this.spinner = new Spinner(SpinnerType.DOT).setStyle(Style.newStyle().foreground(Color.color("205")));
    }

    @Override
    public Command init() {
        return spinner.init();
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            return switch (keyPressMessage.key()) {
                case "q", "Q" -> new UpdateResult<>(this, QuitMessage::new);
                default -> new UpdateResult<>(this, null);
            };
        }

        UpdateResult<? extends Model> updateResult = spinner.update(msg);
        spinner = updateResult.model();
        return UpdateResult.from(this, updateResult.command());
    }

    @Override
    public String view() {
        return "\n\n   %s Loading forever...press q to quit\n\n".formatted(spinner.view());
    }

    public static void main(String[] args) {
        new Program(new SpinnerExample()).run();
    }
}
