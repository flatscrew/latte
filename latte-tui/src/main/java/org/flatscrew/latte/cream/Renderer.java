package org.flatscrew.latte.cream;

import org.flatscrew.latte.cream.color.ColorProfile;
import org.flatscrew.latte.cream.term.Output;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Renderer {

    static Renderer defaultRenderer = new Renderer(Output.defaultOutput());

    public static Renderer defaultRenderer() {
        return defaultRenderer;
    }

    private final Lock renderLock = new ReentrantLock();
    private Output output;
    private ColorProfile colorProfile;
    private boolean explicitColorProfile;

    private boolean hasDarkBackground;
    private boolean hasDarkBackgroundSet;

    public Renderer(Output output) {
        this.output = output;
    }

    public Style newStyle() {
        return new Style(this);
    }

    public boolean hasDarkBackground() {
        if (hasDarkBackgroundSet) {
            return hasDarkBackground;
        }

        renderLock.lock();
        try {
            hasDarkBackground = output.hasDarkBackground();
            hasDarkBackgroundSet = true;
            return hasDarkBackground;
        } finally {
            renderLock.unlock();
        }
    }

    public ColorProfile colorProfile() {
        if (!explicitColorProfile && colorProfile == null) {
            renderLock.lock();
            try {
                colorProfile = output.envColorProfile();
            } finally {
                renderLock.unlock();
            }
        }
        return colorProfile;
    }

}
