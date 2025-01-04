package org.flatscrew.latte.ansi;

import com.ibm.icu.text.BreakIterator;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class GraphemeCluster {

    public static GraphemeResult getFirstGrapheme(byte[] bytes, int startIndex, int state) {
        // Convert bytes to string
        String text = new String(bytes, startIndex, bytes.length - startIndex, StandardCharsets.UTF_8);

        // Use BreakIterator to find grapheme boundaries
        BreakIterator iterator = BreakIterator.getCharacterInstance();
        iterator.setText(text);

        int clusterStart = iterator.first();
        int clusterEnd = iterator.next();

        if (clusterEnd == BreakIterator.DONE) {
            return null; // No grapheme cluster found
        }

        // Extract grapheme cluster
        String cluster = text.substring(clusterStart, clusterEnd);

        // Calculate width (e.g., handle wide characters)
        int width = calculateWidth(cluster);

        // Determine the rest of the bytes
        byte[] clusterBytes = cluster.getBytes(StandardCharsets.UTF_8);
        byte[] restBytes = Arrays.copyOfRange(bytes, startIndex + clusterBytes.length, bytes.length);

        // New state logic (customize as needed)
        int newState = (state == State.UTF8.ordinal()) ? State.GROUND.ordinal() : State.UTF8.ordinal();

        return new GraphemeResult(clusterBytes, restBytes, width, newState);
    }

    private static int calculateWidth(String cluster) {
        // Determine width for wrapping (handle wide characters, emojis, etc.)
        int cp = cluster.codePointAt(0);
        if (Character.isSupplementaryCodePoint(cp)) {
            return 2; // Wide characters
        }
        return 1; // Default width
    }

    public record GraphemeResult(byte[] cluster, byte[] rest, int width, int newState) {
    }
}
