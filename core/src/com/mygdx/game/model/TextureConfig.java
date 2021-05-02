package com.mygdx.game.model;

import com.mygdx.game.constant.State;

import java.util.Map;

public class TextureConfig {

    private final String texturePath;

    private final int textureWidth;

    private final int textureHeight;

    private final Map<State, AnimConfig> animConfigMap;

    public TextureConfig(final String texturePath, final int textureWidth, final int textureHeight,
                         final Map<State, AnimConfig> animConfigMap) {
        this.texturePath = texturePath;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.animConfigMap = animConfigMap;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public int getTextureWidth() {
        return textureWidth;
    }

    public int getTextureHeight() {
        return textureHeight;
    }

    public Map<State, AnimConfig> getAnimConfigMap() {
        return animConfigMap;
    }
}
