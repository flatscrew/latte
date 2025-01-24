package org.flatscrew.latte.examples.tetris;

public enum Tetromino {
    I(new Position[]{
            new Position(0, 0), new Position(1, 0), new Position(2, 0), new Position(3, 0),
            new Position(4, 0), new Position(5, 0), new Position(6, 0), new Position(7, 0)
    }),
    J(new Position[]{
            new Position(0, 0), new Position(1, 0), new Position(2, 0), new Position(3, 0),
            new Position(4, 0), new Position(5, 0),
            new Position(0, 1), new Position(1, 1)
    }),
    L(new Position[]{
            new Position(0, 0), new Position(1, 0), new Position(2, 0), new Position(3, 0),
            new Position(4, 0), new Position(5, 0),
            new Position(4, 1), new Position(5, 1)
    }),
    O(new Position[]{
            new Position(0, 0), new Position(1, 0), new Position(2, 0), new Position(3, 0),
            new Position(0, 1), new Position(1, 1), new Position(2, 1), new Position(3, 1)
    }),
    T(new Position[]{
            new Position(0, 0), new Position(1, 0), new Position(2, 0), new Position(3, 0), // Top row
            new Position(4, 0), new Position(5, 0),
            new Position(2, 1), new Position(3, 1) // Middle blocks (stem)
    }),
    S(new Position[]{
            new Position(2, 0), new Position(3, 0), new Position(4, 0), new Position(5, 0),
            new Position(0, 1), new Position(1, 1), new Position(2, 1), new Position(3, 1)
    }),
    Z(new Position[]{
            new Position(0, 0), new Position(1, 0), new Position(2, 0), new Position(3, 0),
            new Position(2, 1), new Position(3, 1), new Position(4, 1), new Position(5, 1)
    });

    private final Position[] positions;

    Tetromino(Position[] positions) {
        this.positions = positions;
    }

    public TetrominoInstance newInstance() {
        return new TetrominoInstance(this);
    }

    public Block[] buildBlocks() {
        Block[] blocks = new Block[positions.length];
        for (int i = 0; i < positions.length; i++) {
            Position pos = positions[i];
            blocks[i] = new Block(pos);
        }
        return blocks;
    }

    public static Tetromino random() {
        return values()[(int) (Math.random() * values().length)];
    }
}