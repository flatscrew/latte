package org.flatscrew.latte.cream.tree;

public interface Children {

    Node at(int index);
    Children remove(int index);
    Children append(Node child);
    int length();
}
