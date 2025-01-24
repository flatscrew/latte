package org.flatscrew.latte.examples.tetris;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.Message;
import org.flatscrew.latte.Model;
import org.flatscrew.latte.UpdateResult;
import org.flatscrew.latte.message.KeyPressMessage;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.flatscrew.latte.Command.tick;

public class Grid implements Model {

    record TickMessage(LocalDateTime localDateTime) implements Message {

    }

    private final int width;
    private final int height;

    private TetrominoInstance nextPiece = Tetromino.random().newInstance();
    private TetrominoInstance currentPiece;
    private Duration tickRate;

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        this.tickRate = Duration.ofSeconds(1);
        spawnNewPiece();
    }

    @Override
    public Command init() {
        return tick(tickRate, TickMessage::new);
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof TickMessage) {
            return handleTick();
        } else if (msg instanceof KeyPressMessage keyPressMessage) {
            return handleKey(keyPressMessage);
        }

        return UpdateResult.from(this);
    }

    private UpdateResult<? extends Model> handleTick() {
        if (canMove(0, 1)) {
            moveBlocks(0, 1);
        } else {
            spawnNewPiece();
        }
        return UpdateResult.from(this, tick(tickRate, TickMessage::new));
    }

    private UpdateResult<? extends Model> handleKey(KeyPressMessage key) {
        switch (key.key()) {
            case "left" -> {
                if (canMove(-1, 0)) moveBlocks(-1, 0);
            }
            case "right" -> {
                if (canMove(1, 0)) moveBlocks(1, 0);
            }
            case "down" -> {
                if (canMove(0, 1)) moveBlocks(0, 1);
            }
        }
        return UpdateResult.from(this);
    }

    private boolean canMove(int dx, int dy) {
        for (Block block : currentPiece.blocks()) {
            Position newPos = new Position(block.position().x() + dx, block.position().y() + dy);
            if (newPos.x() < 0 || newPos.x() >= width ||
                    newPos.y() < 0 || newPos.y() >= height) {
                return false;
            }
        }
        return true;
    }

    private void moveBlocks(int dx, int dy) {
        currentPiece.moveTo(dx, dy);
    }

    @Override
    public String view() {
        char[][] grid = new char[height][];
        for (int i = 0; i < grid.length; i++) {
            grid[i] = new char[width];
            Arrays.fill(grid[i], '·');
        }

        for (Block block : currentPiece.blocks()) {
            grid[block.position().y()][block.position().x()] = '█';
        }

        StringBuilder gridView = new StringBuilder();
        for (char[] row : grid) {
            for (char cell : row) {
                gridView.append(cell);
            }
            gridView.append('\n');
        }
        return gridView.toString();
    }

    private void spawnNewPiece() {
        this.currentPiece = nextPiece;
        currentPiece.moveTo(width / 2 - 4, 0);

        this.nextPiece = Tetromino.random().newInstance();
    }

    public String nextBlockPreview() {
        return nextPiece.preview(8, 2);
    }
}
