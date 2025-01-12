package org.flatscrew.latte.cream.placement;

import org.flatscrew.latte.cream.Position;
import org.flatscrew.latte.cream.Whitespace.WhitespaceOption;

import static org.flatscrew.latte.cream.Renderer.defaultRenderer;

public class PlacementDecorator {

    public static String place(int width, int height, Position hPos, Position vPos, String input, WhitespaceOption... options) {
        return defaultRenderer().place(width, height, hPos, vPos, input, options);
    }

    public static String placeHorizontal(int width, Position position, String input, WhitespaceOption... options) {
        return defaultRenderer().placeHorizontal(width, position, input, options);
    }

    public static String placeVertical(int height, Position position, String input, WhitespaceOption... options) {
        return defaultRenderer().placeVertical(height, position, input, options);
    }
}
