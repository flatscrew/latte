package org.flatscrew.latte.examples.width;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.Message;
import org.flatscrew.latte.Model;
import org.flatscrew.latte.Program;
import org.flatscrew.latte.UpdateResult;
import org.flatscrew.latte.cream.Style;
import org.flatscrew.latte.message.QuitMessage;

public class WidthExample implements Model {

    private final static Style limitedWidth = Style.newStyle().width(15);
    private final static Style red = Style.newStyle();
    private final static Style green = Style.newStyle();

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
        return limitedWidth.render("This is a long text and it %s be wrapped after 15 %s, we will see how it presents ...".formatted(
                red.render("should"),
                green.render("characters")
        ));
    }

    public static void main(String[] args) {
        new Program(new WidthExample()).run();
    }
}
