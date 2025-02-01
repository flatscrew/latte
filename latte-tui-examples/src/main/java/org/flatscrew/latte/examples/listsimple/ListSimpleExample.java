package org.flatscrew.latte.examples.listsimple;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.Message;
import org.flatscrew.latte.Model;
import org.flatscrew.latte.Program;
import org.flatscrew.latte.UpdateResult;
import org.flatscrew.latte.cream.Style;
import org.flatscrew.latte.cream.color.Color;
import org.flatscrew.latte.message.KeyPressMessage;
import org.flatscrew.latte.message.WindowSizeMessage;
import org.flatscrew.latte.spice.list.Item;
import org.flatscrew.latte.spice.list.ItemDelegate;
import org.flatscrew.latte.spice.list.List;
import org.flatscrew.latte.spice.list.Styles;

public class ListSimpleExample implements Model {

    private final Style titleStyle = Style.newStyle().marginLeft(2);
    private final Style itemStyle = Style.newStyle().paddingLeft(4);
    private final Style selectedItemStyle = Style.newStyle().paddingLeft(2).foreground(Color.color("170"));
    private final Style paginationStyle = Styles.defaultStyles().paginationStyle().paddingLeft(4);
    private final Style helpStyle = Styles.defaultStyles().helpStyle().paddingLeft(4).paddingBottom(1);
    private final Style quitTextStyle = Style.newStyle().margin(1, 0, 2, 4);

    record SimpleItem(String value) implements Item {
        @Override
        public String filterValue() {
            return "";
        }
    }

    class SimpleItemDelegate implements ItemDelegate {

        @Override
        public void render(StringBuilder output, List list, int index, Item item) {
            if (item instanceof SimpleItem simpleItem) {
                String str = "%d. %s".formatted(index + 1, simpleItem.value());
                if (index == list.index()) {
                    output.append(selectedItemStyle.render(">", str));
                } else {
                    output.append(itemStyle.render(str));
                }
            }
        }

        @Override
        public int height() {
            return 1;
        }

        @Override
        public int spacing() {
            return 0;
        }

        @Override
        public Command update(Message msg, List model) {
            return null;
        }
    }

    private List list;
    private String choice;
    private boolean quitting;

    public ListSimpleExample() {
        this.list = new List(
                new Item[]{
                        new SimpleItem("Ramen"),
                        new SimpleItem("Tomato Soup"),
                        new SimpleItem("Hamburgers"),
                        new SimpleItem("Cheeseburgers"),
                        new SimpleItem("Currywurst"),
                        new SimpleItem("Okonomiyaki"),
                        new SimpleItem("Pasta"),
                        new SimpleItem("Fillet Mignon"),
                        new SimpleItem("Caviar"),
                        new SimpleItem("Just Wine")
                },
                new SimpleItemDelegate(),
                20,
                14
        );

    }

    @Override
    public Command init() {
        list.setTitle("What do you want for dinner?");
        list.setShowStatusBar(false);
        list.setFilteringEnabled(false);
        list.styles().setTitle(titleStyle);
        list.styles().setPaginationStyle(paginationStyle);
        list.styles().setHelpStyle(helpStyle);

        return list.init();
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof WindowSizeMessage windowSizeMessage) {
            list.setWidth(windowSizeMessage.width());
            return UpdateResult.from(this);
        }

        if (msg instanceof KeyPressMessage keyPressMessage) {
            if ("q".equals(keyPressMessage.key()) || "ctrl+c".equals(keyPressMessage.key())) {
                this.quitting = true;
                return UpdateResult.from(this, Command.quit());
            } else if ("enter".equals(keyPressMessage.key())) {
                Item item = list.selectedItem();
                if (item instanceof SimpleItem simpleItem) {
                    this.choice = simpleItem.value();
                }
                return UpdateResult.from(this, Command.quit());
            }
        }

        UpdateResult<List> listUpdateResult = list.update(msg);
        return UpdateResult.from(this, listUpdateResult.command());
    }

    @Override
    public String view() {
        if (this.choice != null && !this.choice.isEmpty()) {
            return quitTextStyle.render("%s? Sounds good to me.".formatted(choice));
        }
        if (this.quitting) {
            return quitTextStyle.render("Not hungry? That's cool.");
        }
        return "\n" + list.view();
    }

    public static void main(String[] args) {
        new Program(new ListSimpleExample()).run();
    }
}
