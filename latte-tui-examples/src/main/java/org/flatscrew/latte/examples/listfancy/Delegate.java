package org.flatscrew.latte.examples.listfancy;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.message.KeyPressMessage;
import org.flatscrew.latte.spice.help.KeyMap;
import org.flatscrew.latte.spice.key.Binding;
import org.flatscrew.latte.spice.list.DefaultDelegate;
import org.flatscrew.latte.spice.list.DefaultDataSource;

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
                if (list.selectedItem() instanceof FancyItem fancyItem) {
                    title = fancyItem.title();
                } else {
                    return null;
                }

                if (Binding.matches(keyPressMessage, keyMap.choose())) {
                    return list.newStatusMessage(Styles.statusMessageStyle.apply(new String[]{"You choose", title}));
                } else if (Binding.matches(keyPressMessage, keyMap.remove())) {
                    int index = list.index();

                    java.util.List<Command> commands = new LinkedList<>();

                    if (list.dataSource() instanceof DefaultDataSource defaultDataSource) {
                        commands.add(defaultDataSource.removeItem(index, () -> {
                            if (defaultDataSource.isEmpty()) {
                                keyMap.remove().setEnabled(false);
                            }
                        }));
                    }
                    commands.add(list.newStatusMessage(Styles.statusMessageStyle.apply(new String[]{"Deleted", title})));
                    return Command.batch(
                            commands
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
