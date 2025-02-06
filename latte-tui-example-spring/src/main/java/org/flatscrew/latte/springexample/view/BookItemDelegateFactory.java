package org.flatscrew.latte.springexample.view;

import org.flatscrew.latte.message.KeyPressMessage;
import org.flatscrew.latte.spice.help.KeyMap;
import org.flatscrew.latte.spice.key.Binding;
import org.flatscrew.latte.spice.list.DefaultDelegate;
import org.springframework.stereotype.Component;

@Component
public class BookItemDelegateFactory {

    public static class DelegateKeyMap implements KeyMap {

        private final Binding remove;

        public DelegateKeyMap() {
            this.remove = new Binding(
                    Binding.withKeys("x", "backspace"),
                    Binding.withHelp("x", "delete")
            );
        }

        @Override
        public Binding[] shortHelp() {
            return new Binding[]{
                    remove
            };
        }

        @Override
        public Binding[][] fullHelp() {
            return new Binding[][] {
                    new Binding[]{remove}
            };
        }

        public Binding remove() {
            return remove;
        }
    }

    public DefaultDelegate newBokItemDelegate(DelegateKeyMap keyMap) {
        DefaultDelegate defaultDelegate = new DefaultDelegate();

        defaultDelegate.setUpdateFunction((msg, listModel) -> {

            if (msg instanceof KeyPressMessage keyPressMessage) {
                if (Binding.matches(keyPressMessage, keyMap.remove())) {

                    if (listModel.selectedItem() instanceof BookItem bookItem) {
                        return () -> new BookRemovalRequested(bookItem.book());
                    }
                }
            }

            return null;
        });

        Binding[] help = new Binding[]{
                keyMap.remove()
        };
        defaultDelegate.setShortHelpFunc(() -> help);
        defaultDelegate.setFullHelpFunc(() -> new Binding[][]{help});

        return defaultDelegate;
    }
}
