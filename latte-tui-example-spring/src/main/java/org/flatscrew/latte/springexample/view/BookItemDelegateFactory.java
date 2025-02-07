package org.flatscrew.latte.springexample.view;

import org.flatscrew.latte.spice.help.KeyMap;
import org.flatscrew.latte.spice.key.Binding;
import org.flatscrew.latte.spice.list.DefaultDelegate;
import org.springframework.stereotype.Component;

@Component
public class BookItemDelegateFactory {

    public static class DelegateKeyMap implements KeyMap {

        private final Binding remove;
        private final Binding choose;

        public DelegateKeyMap() {
            this.remove = new Binding(
                    Binding.withKeys("x", "backspace"),
                    Binding.withHelp("x", "delete")
            );
            this.choose = new Binding(
                    Binding.withKeys("enter"),
                    Binding.withHelp("enter", "choose")
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
                    new Binding[]{remove, choose}
            };
        }

        public Binding remove() {
            return remove;
        }

        public Binding choose() {
            return choose;
        }
    }

    public DefaultDelegate newBokItemDelegate(DelegateKeyMap keyMap) {
        DefaultDelegate defaultDelegate = new DefaultDelegate();
        defaultDelegate.setShortHelpFunc(keyMap::shortHelp);
        defaultDelegate.setFullHelpFunc(keyMap::fullHelp);
        return defaultDelegate;
    }
}
