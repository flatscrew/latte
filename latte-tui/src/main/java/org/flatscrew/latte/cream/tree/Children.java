package org.flatscrew.latte.cream.tree;

import java.util.Arrays;

public interface Children {

    static Children newStringData(String... strings) {
        return new NodeChildren(Arrays.stream(strings)
                .map(Leaf::new)
                .map(Node.class::cast)
                .toList());
    }

    Node at(int index);

    Children remove(int index);

    Children append(Node child);

    int length();
}
