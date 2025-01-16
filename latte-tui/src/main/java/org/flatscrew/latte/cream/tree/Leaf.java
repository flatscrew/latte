package org.flatscrew.latte.cream.tree;

public class Leaf implements Node {

    private String value;
    private boolean hidden;

    public Leaf(String value) {
        this.value = value;
    }

    public Leaf() {
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public Children children() {
        return new NodeChildren();
    }

    @Override
    public boolean isHidden() {
        return hidden;
    }

    @Override
    public String toString() {
        return value;
    }
}
