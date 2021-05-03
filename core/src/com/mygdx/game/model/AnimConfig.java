package com.mygdx.game.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AnimConfig {

    String name;

    float frameRate;
}
