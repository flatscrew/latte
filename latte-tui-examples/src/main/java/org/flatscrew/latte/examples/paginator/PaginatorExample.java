package org.flatscrew.latte.examples.paginator;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.Message;
import org.flatscrew.latte.Model;
import org.flatscrew.latte.Program;
import org.flatscrew.latte.UpdateResult;
import org.flatscrew.latte.cream.Style;
import org.flatscrew.latte.cream.color.AdaptiveColor;
import org.flatscrew.latte.message.KeyPressMessage;
import org.flatscrew.latte.message.QuitMessage;
import org.flatscrew.latte.spice.paginator.Bounds;
import org.flatscrew.latte.spice.paginator.Paginator;
import org.flatscrew.latte.spice.paginator.Type;

import java.util.Arrays;

public class PaginatorExample implements Model {

    private Paginator paginator;
    private String[] items;

    public PaginatorExample() {
        this.items = new String[100];
        for (int i = 1; i < 101; i++) {
            items[i - 1] = "Item %d".formatted(i);
        }

        this.paginator = new Paginator();
        paginator.type(Type.Dots);
        paginator.perPage(10);
        paginator.totalPages(items.length);
    }

    @Override
    public Command init() {
        // cannot render until terminal info is provided
        paginator.activeDot(Style.newStyle().foreground(new AdaptiveColor("235", "252")).render("•"));
        paginator.inactiveDot(Style.newStyle().foreground(new AdaptiveColor("250", "238")).render("•"));

        return null;
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            String key = keyPressMessage.key();
            if (key.equals("q") || key.equals("Q")) {
                return new UpdateResult<>(this, QuitMessage::new);
            }
        }
        this.paginator = paginator.update(msg);
        return UpdateResult.from(this);
    }

    @Override
    public String view() {
        StringBuilder view = new StringBuilder();
        view.append("\n  Paginator Example\n\n");
        Bounds sliceBounds = paginator.getSliceBounds(items.length);

        String[] range = Arrays.copyOfRange(items, sliceBounds.start(), sliceBounds.end());
        for (String item : range) {
            view.append("  • ").append(item).append("\n\n");
        }
        view.append("  ").append(paginator.view());
        view.append("\n\n  h/l ←/→ page • q: quit\n");

        return view.toString();
    }

    public static void main(String[] args) {
        new Program(new PaginatorExample()).run();
    }
}
