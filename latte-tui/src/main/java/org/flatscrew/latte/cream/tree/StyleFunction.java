package org.flatscrew.latte.cream.tree;

import org.flatscrew.latte.cream.Style;

@FunctionalInterface
public interface StyleFunction {

    Style apply(Children children, int index);
}
