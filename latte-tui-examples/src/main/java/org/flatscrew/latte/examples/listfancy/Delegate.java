package org.flatscrew.latte.examples.listfancy;

import org.flatscrew.latte.message.KeyPressMessage;
import org.flatscrew.latte.spice.help.KeyMap;
import org.flatscrew.latte.spice.key.Binding;
import org.flatscrew.latte.spice.list.DefaultDelegate;

public class Delegate {

    public static class DelegateKeyMap implements KeyMap {
        private Binding choose;
        private Binding remove;

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
        defaultDelegate.setUpdateFunction((msg, model) -> {
            String title = null;
            if (model.selectedItem() instanceof FancyItem fancyItem) {
                title = fancyItem.title();
            } else {
                return null;
            }

            if (msg instanceof KeyPressMessage keyPressMessage) {
                if (Binding.matches(keyPressMessage, keyMap.choose())) {
                    return model.newStatusMessage(Styles.statusMessageStyle.apply(new String[]{"You choose", title}));
                } else if (Binding.matches(keyPressMessage, keyMap.remove())) {
                    int index = model.index();
                    model.removeItem(index);
                    if (model.items().size() == 0) {
                        keyMap.remove().setEnabled(false);
                    }
                    return model.newStatusMessage(Styles.statusMessageStyle.apply(new String[]{"Deleted", title}));
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
