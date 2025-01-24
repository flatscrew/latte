package org.flatscrew.latte.examples.tetris;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TetrominoInstance {

    private Block[] blocks;

    public TetrominoInstance(Tetromino tetromino) {
        this.blocks = tetromino.buildBlocks();
    }

    public void moveTo(int dx, int dy) {
        for (Block block : blocks) {
            block.move(dx, dy);
        }
    }

    public Block[] blocks() {
        return blocks;
    }

    public String preview(int width, int height) {
        char[][] grid = new char[height][width];
        for (char[] row : grid) Arrays.fill(row, ' ');

        for (Block block : blocks) {
            Position pos = block.position();
            if (pos.y() < height && pos.x() < width) {
                grid[pos.y()][pos.x()] = '█';
            }
        }

        return Arrays.stream(grid)
                .map(String::new)
                .collect(Collectors.joining("\n")) + "\n";
    }
}
