package org.flatscrew.latte.spice.key;

import org.flatscrew.latte.message.KeyPressMessage;

public class Binding {

    public interface BindingOption {
        void apply(Binding binding);
    }

    private int[] keys;
    private Help help;
    private boolean enabled;

    public Binding(BindingOption... opts) {
        for (BindingOption option : opts) {
            option.apply(this);
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

    public boolean matches(KeyPressMessage keyPressMessage) {
        for (int bindingKey : keys) {
            if (keyPressMessage.key() == bindingKey) {
                return true;
            }
        }
        return false;
    }

    public static BindingOption withKeys(int... keys) {
        return binding -> binding.keys = keys;
    }

    public static BindingOption withHelp(String key, String desc) {
        return binding -> binding.help = new Help(key, desc);
    }

    public static boolean matches(KeyPressMessage keyPressMessage, Binding... bindings) {
        for (Binding binding : bindings) {
            if (!binding.isEnabled()) {
                continue;
            }
            if (binding.matches(keyPressMessage)) {
                return true;
            }
        }
        return false;
    }
}
