package org.flatscrew.latte.springexample.view;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.Message;
import org.flatscrew.latte.Model;
import org.flatscrew.latte.UpdateResult;
import org.flatscrew.latte.cream.Position;
import org.flatscrew.latte.cream.Style;
import org.flatscrew.latte.cream.border.StandardBorder;
import org.flatscrew.latte.cream.color.Color;
import org.flatscrew.latte.cream.join.VerticalJoinDecorator;
import org.flatscrew.latte.cream.placement.PlacementDecorator;
import org.flatscrew.latte.message.KeyPressMessage;
import org.flatscrew.latte.spice.help.Help;
import org.flatscrew.latte.spice.help.KeyMap;
import org.flatscrew.latte.spice.key.Binding;
import org.flatscrew.latte.springexample.model.Book;

public class RemoveBookViewModel implements Model, KeyMap {

    private final Book book;
    private final Model previousViewModel;
    private final int width;
    private final int height;

    private final Style warningBoxStyle;
    private final Style warningMessageStyle;
    private final Binding confirm;
    private final Binding back;
    private Help help;

    public RemoveBookViewModel(Book book, Model previousViewModel, int width, int height) {
        this.book = book;
        this.previousViewModel = previousViewModel;
        this.width = width;
        this.height = height;

        this.help = new Help();

        this.warningBoxStyle = Style.newStyle()
                .width(10)
                .padding(5)
                .margin(3)
                .border(StandardBorder.RoundedBorder)
                .borderBackground(Color.color("#ff0000"))
                .background(Color.color("#ff0000"));
        this.warningMessageStyle = Style.newStyle()
                .foreground(Color.color("#fff"))
                .blink(true);

        this.confirm = new Binding(Binding.withKeys("y"), Binding.withHelp("y", "confirm"));
        this.back = new Binding(Binding.withKeys("esc"), Binding.withHelp("esc", "go back"));
    }

    @Override
    public Command init() {
        return null;
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            if (Binding.matches(keyPressMessage, back)) {
                return UpdateResult.from(previousViewModel);
            } else if (Binding.matches(keyPressMessage, confirm)) {
                //
            }
        }
        return UpdateResult.from(this);
    }

    @Override
    public String view() {
        return PlacementDecorator.place(
                width,
                height,
                Position.Center,
                Position.Center,
                VerticalJoinDecorator.joinVertical(
                        Position.Center,
                        warningBoxStyle.render(
                                VerticalJoinDecorator.joinVertical(
                                        Position.Center,
                                        warningMessageStyle.render("Are you sure you want to remove this book?"),
                                        "",
                                        "“%s”".formatted(book.getTitle())
                                )
                        ),
                        help.render(this)
                )
        );
    }

    @Override
    public Binding[] shortHelp() {
        return new Binding[]{
                confirm,
                back
        };
    }

    @Override
    public Binding[][] fullHelp() {
        return new Binding[][]{
                new Binding[]{
                        confirm, back
                }
        };
    }
}
