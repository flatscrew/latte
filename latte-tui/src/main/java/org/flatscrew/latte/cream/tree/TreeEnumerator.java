package org.flatscrew.latte.cream.tree;

@FunctionalInterface
public interface TreeEnumerator {

    class DefaultEnumerator implements TreeEnumerator {

        @Override
        public String enumerate(Children children, int index) {
            if (children.length() -1 == index) {
                return "└──";
            }
            return "├──";
        }
    }

    String enumerate(Children children, int index);
}
