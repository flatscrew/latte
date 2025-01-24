package org.flatscrew.latte.examples.tetris;

public class Block {

    private Position position;

    public Block(Position position) {
        this.position = position;
    }

    public Position position() {
        return position;
    }

    public void move(int dx, int dy) {
        this.position = new Position(
                position().x() + dx,
                position().y() + dy
        );
    }
}
