package org.flatscrew.latte.examples.tetris;

import org.flatscrew.latte.cream.color.Color;

public class Block {

    private Position position;
    private final Color color;

    public Block(Position position, Color color) {
        this.position = position;
        this.color = color;
    }

    public Position position() {
        return position;
    }

    public Color color() {
        return color;
    }

    public void move(int dx, int dy) {
        this.position = new Position(
                position().x() + dx,
                position().y() + dy
        );
    }
}
