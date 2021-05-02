package com.mygdx.game.model;

public class AnimConfig {

    private final String name;

    private final float frameRate;

    public AnimConfig(final String name, final float frameRate) {
        this.name = name;
        this.frameRate = frameRate;
    }

    public String getName() {
        return name;
    }

    public float getFrameRate() {
        return frameRate;
    }
}
