package org.flatscrew.latte.springexample;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.Message;
import org.flatscrew.latte.Model;
import org.flatscrew.latte.UpdateResult;
import org.flatscrew.latte.message.WindowSizeMessage;
import org.flatscrew.latte.spice.list.List;
import org.flatscrew.latte.spice.spinner.Spinner;
import org.flatscrew.latte.spice.spinner.SpinnerType;
import org.springframework.stereotype.Component;

@Component
public class LatteApplication implements Model {

    private final BookService bookService;
    private final List list;
    private Spinner spinner;
    private boolean booksGenerated;

    public LatteApplication(BookDataSource bookDataSource, BookService bookService) {
        this.list = new List(bookDataSource, 0, 0);
        list.setTitle("Books");
        this.spinner = new Spinner(SpinnerType.DOT);
        this.bookService = bookService;
    }

    @Override
    public Command init() {
        return Command.batch(
                spinner.init(),
                this::generateBooks
        );
    }

    private Message generateBooks() {
        bookService.generateBooks(100);
        return new BooksGenerated();
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof WindowSizeMessage windowSizeMessage) {
            return UpdateResult.from(this, list.setSize(windowSizeMessage.width(), windowSizeMessage.height()));
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
        return UpdateResult.from(this, listUpdateResult.command());
    }

    @Override
    public String view() {
        if (!booksGenerated) {
            return spinner.view() + " Generating books";
        }
        return list.view();
    }

    record BooksGenerated() implements Message {

    }
}
