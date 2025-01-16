package org.flatscrew.latte.cream.tree;

import org.flatscrew.latte.cream.Style;

public class TreeStyle {

    private StyleFunction enumeratorFunction;
    private StyleFunction itemFunction;
    private Style rootStyle;

    public TreeStyle() {
        this.enumeratorFunction = (children, index) -> Style.newStyle().paddingRight(1);
        this.itemFunction = (children, index) -> Style.newStyle();
        this.rootStyle = Style.newStyle();
    }

    public Style rootStyle() {
        return rootStyle;
    }

    public StyleFunction enumeratorFunction() {
        return enumeratorFunction;
    }

    public StyleFunction itemFunction() {
        return itemFunction;
    }

    public void setEnumeratorFunction(StyleFunction enumeratorFunction) {
        this.enumeratorFunction = enumeratorFunction;
    }

    public void setRootStyle(Style rootStyle) {
        this.rootStyle = rootStyle;
    }

    public void setItemFunction(StyleFunction itemFunction) {
        this.itemFunction = itemFunction;
    }
}
