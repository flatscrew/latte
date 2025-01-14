package org.flatscrew.latte.cream.tree;

import org.flatscrew.latte.cream.Style;
import org.flatscrew.latte.cream.color.Color;
import org.flatscrew.latte.cream.color.NoColor;
import org.flatscrew.latte.term.TerminalInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class TreeTest {

    @BeforeEach
    void setUp() {
        // Set up no-color terminal info for consistent output
        TerminalInfo.provide(() -> new TerminalInfo(false, new NoColor()));
    }


    @Test
    void test_ShouldPrintTree() {
        // given
        Tree tree = new Tree().child(
                "Foo",
                Tree.withRoot("Bar")
                        .child(
                                "Qux",
                                Tree.withRoot("Quux")
                                        .child(
                                                "Foo",
                                                "Bar"
                                        ),
                                "Quuux"
                        ),
                "Baz"
        );

        // when
        String treeString = tree.toString();

        // then

        assertThat(treeString).isEqualTo(
                """
                        ├── Foo
                        ├── Bar
                        │   ├── Qux
                        │   ├── Quux
                        │   │   ├── Foo
                        │   │   └── Bar
                        │   └── Quuux
                        └── Baz""");

        // when
        tree.enumerator(new TreeEnumerator.RounderEnumerator());
        treeString = tree.toString();

        // then
        assertThat(treeString).isEqualTo(
                """
                        ├── Foo
                        ├── Bar
                        │   ├── Qux
                        │   ├── Quux
                        │   │   ├── Foo
                        │   │   ╰── Bar
                        │   ╰── Quuux
                        ╰── Baz""");


    }

    @Test
    void test_TreeHidden() {
        // given
        Tree tree = new Tree().child(
                "Foo",
                Tree.withRoot("Bar").child(
                        "Qux",
                        Tree.withRoot("Quux").child("Foo", "Bar").hide(),
                        "Quuux"
                ),
                "Baz"
        );

        // then
        assertThat(tree.toString()).isEqualTo("""
                ├── Foo
                ├── Bar
                │   ├── Qux
                │   └── Quuux
                └── Baz""");
    }

    @Test
    void test_TreeAllHidden() {
        // given
        Tree tree = new Tree()
                .child(
                        "Foo",
                        Tree.withRoot("Bar").child(
                                "Qux",
                                Tree.withRoot("Quux").child("Foo", "Bar"),
                                "Quuux"
                        ),
                        "Baz"
                ).hide();

        // then
        assertThat(tree.toString()).isEqualTo("");
    }

    @Test
    void test_TreeRoot() {
        // given
        Tree tree = new Tree()
                .root("Root")
                .child(
                        "Foo",
                        Tree.withRoot("Bar").child("Qux", "Quuux"),
                        "Baz"
                );

        // then
        assertThat(tree.toString()).isEqualTo("""
                Root
                ├── Foo
                ├── Bar
                │   ├── Qux
                │   └── Quuux
                └── Baz""");
    }

    @Test
    void test_TreeStartsWithSubtree() {
        // given
        Tree tree = new Tree().child(
                new Tree().root("Bar").child("Qux", "Quuux"),
                "Baz"
        );

        // then
        assertThat(tree.toString()).isEqualTo("""
                ├── Bar
                │   ├── Qux
                │   └── Quuux
                └── Baz""");
    }

    @Test
    void test_TreeAddTwoSubTreesWithoutName() {
        // given
        Tree tree = new Tree().child(
                "Bar",
                "Foo",
                new Tree().child("Qux", "Qux", "Qux", "Qux", "Qux"),
                new Tree().child("Quux", "Quux", "Quux", "Quux", "Quux"),
                "Baz"
        );

        // then
        assertThat(tree.toString()).isEqualTo("""
                ├── Bar
                ├── Foo
                │   ├── Qux
                │   ├── Qux
                │   ├── Qux
                │   ├── Qux
                │   ├── Qux
                │   ├── Quux
                │   ├── Quux
                │   ├── Quux
                │   ├── Quux
                │   └── Quux
                └── Baz""");
    }

    @Test
    void test_TreeLastNodeIsSubTree() {
        // given
        Tree tree = new Tree().child(
                "Foo",
                Tree.withRoot("Bar").child(
                        "Qux",
                        Tree.withRoot("Quux").child("Foo", "Bar"),
                        "Quuux"
                )
        );

        // then
        assertThat(tree.toString()).isEqualTo("""
                ├── Foo
                └── Bar
                    ├── Qux
                    ├── Quux
                    │   ├── Foo
                    │   └── Bar
                    └── Quuux""");
    }

    @Test
    void test_TreeNil() {
        // given
        Tree tree = new Tree().child(
                null,
                Tree.withRoot("Bar").child(
                        "Qux",
                        Tree.withRoot("Quux").child("Bar"),
                        "Quuux"
                ),
                "Baz"
        );

        // then
        assertThat(tree.toString()).isEqualTo("""
                ├── Bar
                │   ├── Qux
                │   ├── Quux
                │   │   └── Bar
                │   └── Quuux
                └── Baz""");
    }

//    @Test
//    void test_TreeCustom() {
//        // given
//        Tree tree = new Tree()
//                .child(
//                        "Foo",
//                        Tree.withRoot("Bar").child(
//                                "Qux",
//                                Tree.withRoot("Quux").child("Foo", "Bar"),
//                                "Quuux"
//                        ),
//                        "Baz"
//                )
//                .itemStyle(Style.newStyle().foreground(Color.color("9")))
//                .enumeratorStyle(Style.newStyle().foreground(Color.color("12")).paddingRight(1))
//                .enumerator((children, i) -> "->")
//                .indenter((children, i) -> "->");
//
//        // then
//        assertThat(tree.toString()).isEqualTo("""
//                -> Foo
//                -> Bar
//                -> -> Qux
//                -> -> Quux
//                -> -> -> Foo
//                -> -> -> Bar
//                -> -> Quuux
//                -> Baz""");
//    }

//    @Test
//    void test_TreeMultilineNode() {
//        // given
//        Tree tree = new Tree()
//                .root("Big\nRoot\nNode")
//                .child(
//                        "Foo",
//                        Tree.withRoot("Bar").child(
//                                "Line 1\nLine 2\nLine 3\nLine 4",
//                                Tree.withRoot("Quux").child("Foo", "Bar"),
//                                "Quuux"
//                        ),
//                        "Baz\nLine 2"
//                );
//
//        // then
//        assertThat(tree.toString()).isEqualTo("""
//                Big
//                Root
//                Node
//                ├── Foo
//                ├── Bar
//                │   ├── Line 1
//                │   │   Line 2
//                │   │   Line 3
//                │   │   Line 4
//                │   ├── Quux
//                │   │   ├── Foo
//                │   │   └── Bar
//                │   └── Quuux
//                └── Baz
//                    Line 2""");
//    }

//    @Test
//    void test_TreeSubTreeWithCustomEnumerator() {
//        // given
//        Tree tree = new Tree()
//                .root("The Root Node™")
//                .child(
//                        Tree.withRoot("Parent")
//                                .child("child 1", "child 2")
//                                .itemStyleFunc((children, i) -> Style.newStyle().setString("*"))
//                                .enumeratorStyleFunc((children, i) -> Style.newStyle().setString("+").paddingRight(1)),
//                        "Baz"
//                );
//
//        // then
//        assertThat(tree.toString()).isEqualTo("""
//                The Root Node™
//                ├── Parent
//                │   + ├── * child 1
//                │   + └── * child 2
//                └── Baz""");
//    }

    @Test
    void test_TreeMixedEnumeratorSize() {
        // given
        Map<Integer, String> romans = Map.of(
                1, "I",
                2, "II",
                3, "III",
                4, "IV",
                5, "V",
                6, "VI"
        );

        Tree tree = new Tree()
                .root("The Root Node™")
                .child("Foo", "Foo", "Foo", "Foo", "Foo")
                .enumerator((children, i) -> {
                    return romans.get(i + 1);
                });

        // then
        assertThat(tree.toString()).isEqualTo("""
                The Root Node™
                  I Foo
                 II Foo
                III Foo
                 IV Foo
                  V Foo""");
    }

    @Test
    void test_TreeStyleNilFuncs() {
        // given
        Tree tree = new Tree()
                .root("Silly")
                .child("Willy ", "Nilly")
                .itemStyleFunc(null)
                .enumeratorStyleFunc(null);

        // then
        assertThat(tree.toString()).isEqualTo("""
                Silly
                ├──Willy\s
                └──Nilly""");
    }

    @Test
    void test_TreeStyleAt() {
        // given
        Tree tree = new Tree()
                .root("Root")
                .child("Foo", "Baz")
                .enumerator((children, i) ->
                        children.at(i).value().equals("Foo") ? ">" : "-");

        // then
        assertThat(tree.toString()).isEqualTo("""
                Root
                > Foo
                - Baz""");
    }
}