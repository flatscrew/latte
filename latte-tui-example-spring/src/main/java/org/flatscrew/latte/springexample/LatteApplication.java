package org.flatscrew.latte.springexample;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.Message;
import org.flatscrew.latte.Model;
import org.flatscrew.latte.UpdateResult;
import org.flatscrew.latte.cream.Position;
import org.flatscrew.latte.cream.Style;
import org.flatscrew.latte.cream.border.StandardBorder;
import org.flatscrew.latte.cream.color.Color;
import org.flatscrew.latte.cream.join.HorizontalJoinDecorator;
import org.flatscrew.latte.cream.join.VerticalJoinDecorator;
import org.flatscrew.latte.message.KeyPressMessage;
import org.flatscrew.latte.message.WindowSizeMessage;
import org.flatscrew.latte.spice.list.List;
import org.flatscrew.latte.spice.spinner.Spinner;
import org.flatscrew.latte.spice.spinner.SpinnerType;
import org.springframework.stereotype.Component;

@Component
public class LatteApplication implements Model {

    private static final int LIST_WIDTH = 50;

    private Style detailsViewStyle;
    private Style detailsHeaderStyle;
    private Style sectionHeaderStyle;

    private final BooksGenerator booksGenerator;
    private final List list;
    private Spinner spinner;
    private boolean booksGenerated;
    private BookItem choosenItem;

    public LatteApplication(BookDataSource bookDataSource, BooksGenerator booksGenerator) {
        this.list = new List(bookDataSource, 0, 0);
        this.detailsViewStyle = Style.newStyle()
                .paddingLeft(1)
                .border(StandardBorder.DoubleBorder, false, false, false, true);
        this.detailsHeaderStyle = Style.newStyle()
                .background(Color.color("62"))
                .foreground(Color.color("230"))
                .padding(0, 1)
                .marginBottom(1);
        this.sectionHeaderStyle = Style.newStyle().bold(true);

        list.setTitle("Books");
        this.spinner = new Spinner(SpinnerType.DOT);
        this.booksGenerator = booksGenerator;
    }

    @Override
    public Command init() {
        return Command.batch(
                spinner.init(),
                this::generateBooks
        );
    }

    private Message generateBooks() {
        booksGenerator.generateBooks(1000);
        return new BooksGenerated();
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof WindowSizeMessage windowSizeMessage) {
            detailsViewStyle = detailsViewStyle
                    .width(windowSizeMessage.width() - LIST_WIDTH - 1)
                    .height(windowSizeMessage.height() - 2);
            return UpdateResult.from(this, list.setSize(LIST_WIDTH, windowSizeMessage.height()));
        }

        if (msg instanceof BooksGenerated) {
            this.booksGenerated = true;
            return UpdateResult.from(this, list.init());
        }

        if (!booksGenerated) {
            UpdateResult<Spinner> spinnerUpdateResult = spinner.update(msg);
            return UpdateResult.from(this, spinnerUpdateResult.command());
        }

        UpdateResult<List> listUpdateResult = list.update(msg);

        if (msg instanceof KeyPressMessage keyPressMessage) {
            if ("enter".equals(keyPressMessage.key()) && list.selectedItem() instanceof BookItem bookItem){
                this.choosenItem = bookItem;
            }
        }

        return UpdateResult.from(this, listUpdateResult.command());
    }

    @Override
    public String view() {
        if (!booksGenerated) {
            return spinner.view() + " Generating books";
        }
        return HorizontalJoinDecorator.joinHorizontal(
                Position.Top,
                listView(),
                detailsViewStyle.render(
                        detailsView()
                )
        );
    }

    private String listView() {
        return list.view();
    }

    private String detailsView() {
        if (choosenItem == null) {
            return "- select book -";
        }

        return VerticalJoinDecorator.joinVertical(
                Position.Left,
                detailsHeaderStyle.render("Details view here"),
                "%s: %s".formatted(
                        sectionHeaderStyle.render("Title"),
                        choosenItem.title()
                ),
                "",
                sectionHeaderStyle.render("Authors"),
                choosenItem.authors(),
                "",
                choosenItem.getDescription()
        );
    }

    record BooksGenerated() implements Message {

    }
}
