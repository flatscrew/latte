package org.flatscrew.latte.spice.list;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.Message;
import org.flatscrew.latte.Model;

public interface ItemDelegate {

    void render(Appendable output, Model model, int index, Item item);
    int height();
    int spacing();
    Command update(Message msg, Model model);
}
