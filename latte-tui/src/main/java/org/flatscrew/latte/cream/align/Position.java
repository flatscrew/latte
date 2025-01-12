package org.flatscrew.latte.cream.align;

public enum Position {
    Top(0.0),
    Bottom(1.0),
    Center(0.5),
    Left(0.0),
    Right(1.0);

    private final double value;

    Position(double value) {
        this.value = value;
    }

    public double value() {
        return Math.min(1, Math.max(0, this.value));
    }
}
