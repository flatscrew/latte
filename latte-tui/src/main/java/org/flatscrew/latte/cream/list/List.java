package org.flatscrew.latte.cream.list;

import org.flatscrew.latte.cream.Style;
import org.flatscrew.latte.cream.tree.StyleFunction;
import org.flatscrew.latte.cream.tree.Tree;
import org.flatscrew.latte.cream.tree.TreeEnumerator;
import org.flatscrew.latte.cream.tree.TreeIndenter;

public class List {

    private Tree tree;

    public List(Object... items) {
        this.tree = new Tree();
        this.items(items).enumerator(ListEnumerator.bullet()).indenter((children, index) -> " ");
    }

    public boolean isHidden() {
        return tree.isHidden();
    }

    public List hide(boolean hide) {
        tree.hide();
        return this;
    }

    public List offset(int start, int end) {
        tree.offset(start, end);
        return this;
    }

    public List enumeratorStyle(Style style) {
        tree.enumeratorStyle(style);
        return this;
    }

    public List enumeratorStyleFunc(StyleFunction function) {
        tree.enumeratorStyleFunc(function);
        return this;
    }

    public List itemStyleFunc(StyleFunction function) {
        tree.itemStyleFunc(function);
        return this;
    }

    public List indenter(TreeIndenter indenter) {
        tree.indenter(indenter);
        return this;
    }

    public List enumerator(TreeEnumerator enumerator) {
        tree.enumerator(enumerator);
        return this;
    }

    public List item(Object item) {
        if (item instanceof List list) {
            tree.child(list.tree);
        } else {
            tree.child(item);
        }
        return this;
    }

    public List items(Object... items) {
        for (Object item : items) {
            item(item);
        }
        return this;
    }

    public String render() {
        return tree.render();
    }

    @Override
    public String toString() {
        return render();
    }
}
