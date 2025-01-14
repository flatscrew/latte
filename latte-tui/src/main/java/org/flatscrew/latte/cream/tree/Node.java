package org.flatscrew.latte.cream.tree;

public interface Node {

    String value();
    Children children();
    boolean isHidden();
}
