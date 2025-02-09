package org.flatscrew.latte.examples.listfancy;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.Message;
import org.flatscrew.latte.Model;
import org.flatscrew.latte.Program;
import org.flatscrew.latte.UpdateResult;
import org.flatscrew.latte.cream.Dimensions;
import org.flatscrew.latte.message.KeyPressMessage;
import org.flatscrew.latte.message.WindowSizeMessage;
import org.flatscrew.latte.spice.key.Binding;
import org.flatscrew.latte.spice.list.FilterState;
import org.flatscrew.latte.spice.list.Item;
import org.flatscrew.latte.spice.list.List;
import org.flatscrew.latte.spice.list.DefaultDataSource;

public class ListFancyExample implements Model {

    private List list;

    private RandomItemGenerator itemGenerator;
    private Keys keys;
    private Delegate.DelegateKeyMap delegateKeys;

    public ListFancyExample() {
        this.itemGenerator = new RandomItemGenerator();
        this.keys = new Keys();
        this.delegateKeys = new Delegate.DelegateKeyMap();

        int numItems = 24;
        Item[] items = new Item[numItems];
        for (int i = 0; i < numItems; i++) {
            items[i] = itemGenerator.next();
        }

        this.list = new List(items, Delegate.newItemDelegate(delegateKeys), 0, 0);
        list.setTitle("Groceries");
        list.styles().setTitle(Styles.titleStyle);
        list.setAdditionalFullHelpKeys(() -> new Binding[] {
                keys.toggleSpinner(),
                keys.insertItem(),
                keys.toggleTitleBar(),
                keys.toggleStatusBar(),
                keys.togglePagination(),
                keys.toggleHelpMenu()
        });
    }

    @Override
    public Command init() {
        return Command.sequence(Command.checkWindowSize(), list.init());
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof WindowSizeMessage windowSizeMessage) {
            Dimensions frameSize = Styles.appStyle.frameSize();
            return UpdateResult.from(
                    this,
                    list.setSize(
                            windowSizeMessage.width() - frameSize.width(),
                            windowSizeMessage.height() - frameSize.height()
                    )
            );
        } else if (msg instanceof KeyPressMessage keyPressMessage) {
            if (list.filterState() != FilterState.Filtering) {

                if (Binding.matches(keyPressMessage, keys.toggleTitleBar())) {
                    boolean v = !list.showTitle();
                    list.setShowTitle(v);
                    list.setShowFilter(v);
                    return UpdateResult.from(this, list.setFilteringEnabled(v));
                } else if (Binding.matches(keyPressMessage, keys.toggleStatusBar())) {
                    list.setShowStatusBar(!list.showStatusBar());
                    return UpdateResult.from(this);
                } else if (Binding.matches(keyPressMessage, keys.togglePagination())) {
                    list.setShowPagination(!list.showPagination());
                    return UpdateResult.from(this);
                } else if (Binding.matches(keyPressMessage, keys.toggleHelpMenu())) {
                    list.setShowHelp(!list.showHelp());
                    return UpdateResult.from(this);
                } else if (Binding.matches(keyPressMessage, keys.insertItem())) {
                    if (list.dataSource() instanceof DefaultDataSource defaultDataSource) {
                        delegateKeys.remove().setEnabled(true);
                        FancyItem newItem = itemGenerator.next();
                        Command insertCmd = defaultDataSource.insertItem(0, newItem);
                        Command statusCmd = list.newStatusMessage(Styles.statusMessageStyle.apply(new String[]{"Added", newItem.title()}));
                        return UpdateResult.from(this, Command.batch(insertCmd, statusCmd));
                    }
                    return UpdateResult.from(this);
                }
            }
        }

        UpdateResult<List> listUpdateResult = list.update(msg);
        return UpdateResult.from(this, listUpdateResult.command());
    }

    @Override
    public String view() {
        return Styles.appStyle.render(list.view());
    }

    public static void main(String[] args) {
        new Program(new ListFancyExample())
                .withAltScreen()
                .run();
    }
}
