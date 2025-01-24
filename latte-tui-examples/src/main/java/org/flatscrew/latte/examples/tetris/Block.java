package org.flatscrew.latte.examples.tetris;

public class Block {

    private Position position;

    public Block(Position position) {
        this.position = position;
    }

    public Position position() {
        return position;
    }
}
