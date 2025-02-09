package org.flatscrew.latte.springexample.view;

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
import org.flatscrew.latte.spice.key.Binding;
import org.flatscrew.latte.spice.list.FilterState;
import org.flatscrew.latte.spice.list.List;
import org.flatscrew.latte.spice.spinner.Spinner;
import org.flatscrew.latte.spice.spinner.SpinnerType;
import org.springframework.stereotype.Component;

@Component
public class MainViewModel implements Model {

    private static final int LIST_WIDTH = 50;

    private Style listStyle;
    private Style detailsHeaderStyle;
    private Style sectionHeaderStyle;
    private Style descriptionBlockStyle;
    private Style noSelectionStyle;

    private final RemoveBookViewModel removeBookViewModel;
    private final BookItemDelegateFactory.DelegateKeyMap delegateKeyMap;
    private final BooksGenerator booksGenerator;
    private final List list;

    private Spinner spinner;
    private boolean booksGenerated;
    private BookItem choosenItem;

    private int width;
    private int height;

    public MainViewModel(RemoveBookViewModel removeBookViewModel,
                         BookItemDelegateFactory bookItemDelegateFactory,
                         BookDataSource bookDataSource,
                         BooksGenerator booksGenerator) {
        this.removeBookViewModel = removeBookViewModel;
        this.delegateKeyMap = new BookItemDelegateFactory.DelegateKeyMap();
        this.list = new List(bookDataSource, bookItemDelegateFactory.newBokItemDelegate(delegateKeyMap), 0, 0);
        list.setTitle("Books");
//        list.setFilterOnAcceptOnly(true);

        this.listStyle = Style.newStyle()
                .padding(0, 1, 0, 0)
                .margin(0, 1, 0, 0)
                .border(StandardBorder.DoubleBorder, false, true, false, false);
        this.detailsHeaderStyle = Style.newStyle()
                .background(Color.color("62"))
                .foreground(Color.color("230"))
                .padding(0, 1)
                .marginBottom(1);
        this.sectionHeaderStyle = Style.newStyle().bold(true);
        this.descriptionBlockStyle = Style.newStyle().inline(false);
        this.noSelectionStyle = Style.newStyle().align(Position.Center, Position.Center);

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
            this.width = windowSizeMessage.width();
            this.height = windowSizeMessage.height();

            listStyle = listStyle
                    .height(windowSizeMessage.height());
            descriptionBlockStyle = descriptionBlockStyle.width(width - LIST_WIDTH - 1);
            noSelectionStyle = noSelectionStyle.width(width - LIST_WIDTH - 1).height(height);
            return UpdateResult.from(this, list.setSize(LIST_WIDTH, height));
        }

        if (msg instanceof BooksGenerated) {
            this.booksGenerated = true;
            return UpdateResult.from(this, list.init());
        }

        if (!booksGenerated) {
            UpdateResult<Spinner> spinnerUpdateResult = spinner.update(msg);
            return UpdateResult.from(this, spinnerUpdateResult.command());
        }

        if (msg instanceof BookRemovalRequested request) {
            return UpdateResult.from(
                    removeBookViewModel.prepare(
                            this,
                            request.book(),
                            width,
                            height)
            );
        } else if (msg instanceof BookRemovedMessage) {
            return UpdateResult.from(this, list.refresh());
        }

        if (list.filterState() != FilterState.Filtering) {
            if (msg instanceof KeyPressMessage keyPressMessage && list.selectedItem() instanceof BookItem bookItem) {
                if (Binding.matches(keyPressMessage, delegateKeyMap.choose())) {
                    this.choosenItem = bookItem;
                } else if (Binding.matches(keyPressMessage, delegateKeyMap.remove())) {
                    return UpdateResult.from(this, () -> new BookRemovalRequested(bookItem.book()));
                }
            }
        }

        return UpdateResult.from(this, list.update(msg).command());
    }

    @Override
    public String view() {
        if (!booksGenerated) {
            return spinner.view() + " Generating books";
        }
        return HorizontalJoinDecorator.joinHorizontal(
                Position.Top,
                listStyle.render(listView()),
                detailsView()
        );
    }

    private String listView() {
        return list.view();
    }

    private String detailsView() {
        if (choosenItem == null) {
            return noSelectionStyle.render("- choose a book -");
        }

        return VerticalJoinDecorator.joinVertical(
                Position.Left,
                detailsHeaderStyle.render("Details view here"),
                "%s: %s".formatted(
                        sectionHeaderStyle.render("Title"),
                        choosenItem.title()
                ),
                "",
                sectionHeaderStyle.render("Authors:"),
                choosenItem.authors(),
                "",
                descriptionBlockStyle.render(choosenItem.getDescription())
        );
    }

    record BooksGenerated() implements Message {

    }
}
