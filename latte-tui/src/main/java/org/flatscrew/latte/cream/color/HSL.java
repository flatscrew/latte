package org.flatscrew.latte.cream.color;

public record HSL(float h, float s, float l) {

    public boolean isDark() {
        return l < 0.5;
    }
}
