package org.flatscrew.latte.cream.tree;

@FunctionalInterface
public interface TreeIndenter {

    class DefaultIndenter implements TreeIndenter {

        @Override
        public String indent(Children children, int index) {
            if (children.length() - 1 == index) {
                return "   ";
            }
            return "│  ";
        }
    }

    String indent(Children children, int index);
}
