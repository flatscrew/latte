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

        List<String> strings = new LinkedList<>();
        int maxLength = 0;

        Children children = node.children();
        String name = node.value();

        // Print root node name if not empty
        if (name != null && !name.isEmpty() && root) {
            strings.add(style.rootStyle().render(name));
        }

        // First pass: calculate max prefix length
        for (int i = 0; i < children.length(); i++) {
            String currentPrefix = enumerator.enumerate(children, i);
            currentPrefix = style.enumeratorFunction().apply(children, i).render(currentPrefix);
            maxLength = Math.max(TextWidth.measureCellWidth(currentPrefix), maxLength);
        }

        // Second pass: render nodes
        for (int i = 0; i < children.length(); i++) {
            Node child = children.at(i);
            if (child.isHidden()) {
                continue;
            }

            String indent = indenter.indent(children, i);
            String nodePrefix = enumerator.enumerate(children, i);
            Style enumStyle = style.enumeratorFunction().apply(children, i);
            Style itemStyle = style.itemFunction().apply(children, i);

            // Apply style to prefix
            nodePrefix = enumStyle.render(nodePrefix);

            // Pad prefix to max length
            if (maxLength - TextWidth.measureCellWidth(nodePrefix) > 0) {
                nodePrefix = " ".repeat(maxLength - TextWidth.measureCellWidth(nodePrefix)) + nodePrefix;
            }

            String item = itemStyle.render(child.value());
            String multiLinePrefix = prefix;

            // Handle multiline items
            while (Size.height(item) > Size.height(nodePrefix)) {
                nodePrefix = VerticalJoinDecorator.joinVertical(
                        Position.Left,
                        nodePrefix,
                        enumStyle.render(indent)
                );
            }

            // Ensure prefix heights match
            while (Size.height(nodePrefix) > Size.height(multiLinePrefix)) {
                multiLinePrefix = VerticalJoinDecorator.joinVertical(
                        Position.Left,
                        multiLinePrefix,
                        prefix
                );
            }

            // Join all parts horizontally
            strings.add(HorizontalJoinDecorator.joinHorizontal(
                    Position.Top,
                    multiLinePrefix,
                    nodePrefix,
                    item
            ));

            // Render children
            if (children.length() > 0) {
                Renderer newRenderer = this;
                if (child instanceof Tree tree && tree.renderer() != null) {
                    newRenderer = tree.renderer();
                }

                String rendered = newRenderer.render(
                        child,
                        false,
                        prefix + enumStyle.render(indent)
                );

                if (!rendered.isEmpty()) {
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