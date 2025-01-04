//package org.flatscrew.latte.ansi;
//
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//class GraphemeClusterTest {
//
//    @Test
//    void testSimpleCharacters() {
//        String text = "abc";
//        List<String> expected = List.of("a", "b", "c");
//        assertClustersEqual(text, expected);
//    }
//
//    @Test
//    void testCombiningCharacters() {
//        String text = "áb́"; // 'a' + acute accent, 'b' + acute accent
//        List<String> expected = List.of("á", "b́");
//        assertClustersEqual(text, expected);
//    }
//
//    @Test
//    void testEmojis() {
//        String text = "👩‍👩‍👧‍👦👨‍👩‍👦"; // Family emojis
//        List<String> expected = List.of("👩‍👩‍👧‍👦", "👨‍👩‍👦");
//        assertClustersEqual(text, expected);
//    }
//
//    @Test
//    void testEdgeCases() {
//        assertClustersEqual("", new ArrayList<>()); // Empty string
//        assertClustersEqual("a", List.of("a")); // Single character
//    }
//
//    private void assertClustersEqual(String text, List<String> expected) {
//        List<String> actual = new ArrayList<>();
//        int index = 0;
//
//        GraphemeCluster.GraphemeResult result;
//        while ((result = GraphemeCluster.getFirstGrapheme(text, index)) != null) {
//            actual.add(result.cluster());
//            index = result.nextIndex;
//        }
//
//        assertEquals(expected, actual, "Clusters did not match for input: " + text);
//    }
//}