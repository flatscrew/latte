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

    public static void setDefaultRenderer(Renderer renderer) {
        Renderer.defaultRenderer = renderer;
    }

    private final Lock renderLock = new ReentrantLock();
    private Output output;
    private ColorProfile colorProfile;
    private boolean explicitColorProfile;

    public Renderer(Output output) {
        this.output = output;
    }

    public Style newStyle() {
        return new Style(colorProfile());
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

    public void setColorProfile(ColorProfile colorProfile) {
        renderLock.lock();
        try {
            this.colorProfile = colorProfile;
            this.explicitColorProfile = true;
        } finally {
            renderLock.unlock();
        }
    }

    public Output output() {
        renderLock.lock();
        try {
            return output;
        } finally {
            renderLock.unlock();
        }
    }

    public void setOutput(Output output) {
        renderLock.lock();
        try {
            this.output = output;
        } finally {
            renderLock.unlock();
        }
        this.output = output;
    }
}
