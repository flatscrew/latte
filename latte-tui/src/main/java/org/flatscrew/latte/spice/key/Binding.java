package org.flatscrew.latte.spice.key;

import org.flatscrew.latte.message.KeyPressMessage;

public class Binding {

    public interface BindingOption {

        void apply(Binding binding);
    }
    private String[] keys;

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
        this.keys = new String[0];
        this.help = null;
    }

    public Help help() {
        return help;
    }

    public boolean matches(KeyPressMessage keyPressMessage) {
        for (String bindingKey : keys) {
            if (bindingKey.equals(keyPressMessage.key())) {
                return true;
            }
        }
        return false;
    }

    public static BindingOption withKeys(String... keys) {
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
