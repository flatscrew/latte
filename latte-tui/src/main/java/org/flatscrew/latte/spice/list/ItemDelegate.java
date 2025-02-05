package org.flatscrew.latte.spice.list;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.Message;
import org.flatscrew.latte.spice.key.Binding;

public interface ItemDelegate {

    interface UpdateFunction {
        Command update(Message msg, List listModel);
    }

    interface ShortHelpFunc {
        Binding[] get();
    }

    interface FullHelpFunc {
        Binding[][] get();
    }

    void render(StringBuilder output, List list, int index, FilteredItem filteredItem);
    int height();
    int spacing();
    Command update(Message msg, List listModel);
}
