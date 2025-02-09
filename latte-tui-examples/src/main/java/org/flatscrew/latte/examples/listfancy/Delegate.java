package org.flatscrew.latte.examples.listfancy;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.message.KeyPressMessage;
import org.flatscrew.latte.spice.help.KeyMap;
import org.flatscrew.latte.spice.key.Binding;
import org.flatscrew.latte.spice.list.DefaultDataSource;
import org.flatscrew.latte.spice.list.DefaultDelegate;

import java.util.LinkedList;

public class Delegate {

    public static class DelegateKeyMap implements KeyMap {
        private final Binding choose;
        private final Binding remove;

        public DelegateKeyMap() {
            this.choose = new Binding(
                    Binding.withKeys("enter"),
                    Binding.withHelp("enter", "choose")
            );
            this.remove = new Binding(
                    Binding.withKeys("x", "backspace"),
                    Binding.withHelp("x", "delete")
            );
        }

        @Override
        public Binding[] shortHelp() {
            return new Binding[]{
                    choose,
                    remove
            };
        }

        @Override
        public Binding[][] fullHelp() {
            return new Binding[][]{
                    new Binding[]{choose, remove},
            };
        }

        public Binding choose() {
            return choose;
        }

        public Binding remove() {
            return remove;
        }
    }

    public static DefaultDelegate newItemDelegate(DelegateKeyMap keyMap) {
        DefaultDelegate defaultDelegate = new DefaultDelegate();
        defaultDelegate.onUpdate((msg, list) -> {
            if (msg instanceof KeyPressMessage keyPressMessage) {
                String title = null;
                int index = -1;
                if (list.selectedItem() instanceof FancyItem fancyItem && list.dataSource() instanceof DefaultDataSource defaultDataSource) {
                    index = defaultDataSource.indexOf(fancyItem);
                    title = fancyItem.title();
                } else {
                    return null;
                }

                if (Binding.matches(keyPressMessage, keyMap.choose())) {
                    return list.newStatusMessage(Styles.statusMessageStyle.apply(new String[]{"You choose", title}));
                } else if (Binding.matches(keyPressMessage, keyMap.remove()) && index != -1) {
                    return Command.batch(
                            defaultDataSource.removeItem(index, () -> {
                                if (defaultDataSource.isEmpty()) {
                                    keyMap.remove().setEnabled(false);
                                }
                            }),
                            list.newStatusMessage(Styles.statusMessageStyle.apply(new String[]{"Deleted", title}))
                    );
                }
            }
            return null;
        });

        Binding[] help = new Binding[]{
                keyMap.choose(), keyMap.remove()
        };
        defaultDelegate.setShortHelpFunc(() -> help);
        defaultDelegate.setFullHelpFunc(() -> new Binding[][]{help});
        return defaultDelegate;
    }

}
