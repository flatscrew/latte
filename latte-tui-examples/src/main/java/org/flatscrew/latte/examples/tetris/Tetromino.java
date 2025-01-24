package org.flatscrew.latte.examples.tetris;

import java.util.ArrayList;
import java.util.List;

public enum Tetromino {
    I(new Position[][]{{
            new Position(0, 0), new Position(1, 0), new Position(2, 0), new Position(3, 0)
    }}),
    J(new Position[][]{{
            new Position(0, 0), new Position(1, 0), new Position(2, 0),
            new Position(0, 1)
    }}),
    O(new Position[][]{{
            new Position(0, 0), new Position(1, 0),
            new Position(0, 1), new Position(1, 1)
    }}),
    T(new Position[][]{{
            new Position(1, 0),
            new Position(0, 1), new Position(1, 1), new Position(2, 1)
    }}),
    S(new Position[][]{{
            new Position(1, 0), new Position(2, 0),
            new Position(0, 1), new Position(1, 1)
    }});
    private final Position[][] positions;

    Tetromino(Position[][] positions) {
        this.positions = positions;
    }

    public Block[] createBlocks(Position offset) {
        List<Block> blocks = new ArrayList<>();
        for (Position[] row : positions) {
            for (Position pos : row) {
                blocks.add(new Block(new Position(pos.x() * 2 + offset.x(), pos.y() + offset.y())));
                blocks.add(new Block(new Position(pos.x() * 2 + 1 + offset.x(), pos.y() + offset.y())));
            }
        }
        return blocks.toArray(new Block[0]);
    }

    public static Tetromino random() {
        return values()[(int) (Math.random() * values().length)];
    }
}