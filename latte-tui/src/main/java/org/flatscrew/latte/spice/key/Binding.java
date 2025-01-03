package org.flatscrew.latte.spice.key;

import org.flatscrew.latte.message.KeyPressMessage;

import java.util.function.Consumer;

public class Binding {

    private int[] keys;
    private Help help;
    private boolean enabled;

    public Binding(Consumer<Binding>... opts) {
        for (Consumer<Binding> option : opts) {
            option.accept(this);
        }
        this.enabled = true;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void unbind() {
        this.keys = new int[0];
        this.help = null;
    }

    public static Consumer<Binding> withKeys(int... keys) {
        return binding -> binding.keys = keys;
    }

    public static Consumer<Binding> withHelp(String key, String desc) {
        return binding -> binding.help = new Help(key, desc);
    }

    public static boolean matches(KeyPressMessage keyPressMessage, Binding... bindings) {
        for (Binding binding : bindings) {
            if (!binding.isEnabled()) {
                continue;
            }
            for (int bindingKey : binding.keys) {
                if (keyPressMessage.key() == bindingKey) {
                    return true;
                }
            }
        }
        return false;
    }
}
