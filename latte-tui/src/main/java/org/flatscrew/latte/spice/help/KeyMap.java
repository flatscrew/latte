package org.flatscrew.latte.spice.help;

import org.flatscrew.latte.spice.key.Binding;

public interface KeyMap {

    Binding[] shortHelp();
    Binding[][] fullHelp();
}
