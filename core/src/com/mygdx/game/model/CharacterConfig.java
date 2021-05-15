package com.mygdx.game.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CharacterConfig {

    float boundsXOffset;

    float boundsYOffset;

    float boundsWidth;

    float boundsHeight;

    float attackAreaXOffset;

    float attackAreaYOffset;

    float attackAreaWidth;

    float attackAreaHeight;

    CharacterStats characterStats;
}
