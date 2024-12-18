package org.flatscrew.latte.input;

import org.flatscrew.latte.Message;

public class MouseMessage implements Message {
    public enum Type { X10, SGR }

    private final Type type;
    private final int button;
    private final int column;
    private final int row;
    private final boolean release;  // Only relevant for SGR

    public MouseMessage(Type type, int button, int column, int row, boolean release) {
        this.type = type;
        this.button = button;
        this.column = column;
        this.row = row;
        this.release = release;
    }

    public Type type() { return type; }
    public int button() { return button; }
    public int column() { return column; }
    public int row() { return row; }
    public boolean release() { return release; }
}