package com.mygdx.game.model;

import com.mygdx.game.constant.State;
import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class TextureConfig {

    String texturePath;

    int textureWidth;

    int textureHeight;

    Map<State, AnimConfig> animConfigMap;
}
