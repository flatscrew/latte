package org.flatscrew.latte.examples.tetris;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.Message;
import org.flatscrew.latte.Model;
import org.flatscrew.latte.UpdateResult;
import org.flatscrew.latte.cream.Style;
import org.flatscrew.latte.message.KeyPressMessage;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.flatscrew.latte.Command.tick;

public class Grid implements Model {

    record TickMessage(LocalDateTime localDateTime) implements Message {

    }

    private final int width;
    private final int height;

    private Block[][] blocks;
    private Style blockStyle = Style.newStyle();
    private TetrominoInstance nextPiece = Tetromino.random().newInstance();
    private TetrominoInstance currentPiece;
    private Duration tickRate;

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        this.blocks = new Block[height][width];
        this.tickRate = Duration.ofMillis(500);
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
            lockCurrentPiece();
//            spawnNewPiece();
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
            case "up" -> {
                currentPiece.rotate();
                if (!canMove(0, 0)) { // Check if rotation is valid
                    currentPiece.rotate(); // Rotate back if invalid
//                    currentPiece.rotate();
//                    currentPiece.rotate();
                }
            }
        }
        return UpdateResult.from(this);
    }

    private boolean canMove(int dx, int dy) {
        for (Block block : currentPiece.blocks()) {

            Position position = block.position();
            Position newPos = new Position(position.x() + dx, position.y() + dy);
            if (newPos.x() < 0 || newPos.x() >= width ||
                    newPos.y() < 0 || newPos.y() >= height ||
                    blocks[newPos.y()][newPos.x()] != null) {
                return false;
            }
        }
        return true;
    }

    private void lockCurrentPiece() {
        for (Block block : currentPiece.blocks()) {
            Position pos = block.position();
            blocks[pos.y()][pos.x()] = block;
        }
        spawnNewPiece();
    }

    private void moveBlocks(int dx, int dy) {
        currentPiece.moveTo(dx, dy);
    }

    @Override
    public String view() {
        String[][] grid = new String[height][];
        for (int i = 0; i < grid.length; i++) {
            grid[i] = new String[width];
            Arrays.fill(grid[i], "·");
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (blocks[y][x] != null) {
                    grid[y][x] = blockStyle.foreground(blocks[y][x].color()).render("█");
                }
            }
        }

        for (Block block : currentPiece.blocks()) {
            Position pos = block.position();
            if (pos.y() >= 0 && pos.y() < height && pos.x() >= 0 && pos.x() < width) { // Ensure within bounds
                grid[pos.y()][pos.x()] = blockStyle.foreground(block.color()).render("█");
            }
        }

        return Arrays.stream(grid)
                .map(row -> String.join("", row))
                .collect(Collectors.joining("\n")) + "\n";
    }


    private void spawnNewPiece() {
        this.currentPiece = nextPiece;
        currentPiece.moveTo(width / 2 - 4, 0);

        this.nextPiece = Tetromino.random().newInstance();
    }

    public String nextBlockPreview() {
        return nextPiece.preview(8, 4);
    }
}
