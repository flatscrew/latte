package org.flatscrew.latte.examples.tetris;

public record GameState(Block[] blocks, Tetromino currentPiece, Position currentOffset) {
}
