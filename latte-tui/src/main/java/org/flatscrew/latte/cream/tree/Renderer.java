package org.flatscrew.latte.cream.tree;

import org.flatscrew.latte.ansi.TextWidth;
import org.flatscrew.latte.cream.Position;
import org.flatscrew.latte.cream.Size;
import org.flatscrew.latte.cream.Style;
import org.flatscrew.latte.cream.join.HorizontalJoinDecorator;
import org.flatscrew.latte.cream.join.VerticalJoinDecorator;

import java.util.LinkedList;
import java.util.List;

public class Renderer {

    private TreeStyle style;
    private TreeEnumerator enumerator;
    private TreeIndenter indenter;

    public Renderer() {
        this.style = new TreeStyle();
        this.enumerator = new TreeEnumerator.DefaultEnumerator();
        this.indenter = new TreeIndenter.DefaultIndenter();
    }

    public String render(Node node, boolean root, String prefix) {
        if (node.isHidden()) {
            return "";
        }

        String currentPrefix = prefix;

        List<String> strings = new LinkedList<>();
        int maxLength = 0;

        Children children = node.children();
        String name = node.value();
        if (!"".equals(name) && root) {
            strings.add(style.rootStyle().render(name));
        }

        for (int i = 0; i < children.length(); i++) {
            currentPrefix = enumerator.enumerate(children, i);
            currentPrefix = style.enumeratorFunction().apply(children, i).render(currentPrefix);
            maxLength = Math.max(TextWidth.measureCellWidth(prefix), maxLength);
        }

        for (int i = 0; i < children.length(); i++) {
            Node child = children.at(i);
            if (child.isHidden()) {
                continue;
            }

            String indent = indenter.indent(children, i);
            String nodePrefix = enumerator.enumerate(children, i);
            Style enumStyle = style.enumeratorFunction().apply(children, i);
            Style itemStyle = style.itemFunction().apply(children, i);

            nodePrefix = enumStyle.render(nodePrefix);

            int l = maxLength - Size.width(nodePrefix);
            if (l > 0) {
                nodePrefix = " ".repeat(l) + nodePrefix;
            }

            String item = itemStyle.render(child.value());
            String multiLinePrefix = currentPrefix;

            while (Size.height(item) > Size.height(nodePrefix)) {
                nodePrefix = VerticalJoinDecorator.joinVertical(
                        Position.Left,
                        nodePrefix,
                        enumStyle.render(indent)
                );
            }

            while (Size.height(nodePrefix) > Size.height(multiLinePrefix)) {
                multiLinePrefix = VerticalJoinDecorator.joinVertical(
                        Position.Left,
                        multiLinePrefix,
                        prefix
                );
            }

            strings.add(HorizontalJoinDecorator.joinHorizontal(
                    Position.Top,
                    multiLinePrefix,
                    nodePrefix,
                    item
            ));

            if (children.length() > 0) {
                Renderer newRenderer = this;

                if (child instanceof Tree tree) {
                    if (tree.renderer() != null) {
                        newRenderer = tree.renderer();
                    }
                }

                String rendered = newRenderer.render(child, false, prefix + enumStyle.render(indent));
                if (!"".equals(rendered)) {
                    strings.add(rendered);
                }
            }

        }
        return String.join("\n", strings);
    }

    public TreeStyle style() {
        return style;
    }

    public void setEnumerator(TreeEnumerator enumerator) {
        this.enumerator = enumerator;
    }

    public void setIndenter(TreeIndenter indenter) {
        this.indenter = indenter;
    }
}
