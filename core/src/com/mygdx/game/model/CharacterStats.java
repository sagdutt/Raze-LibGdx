package com.mygdx.game.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CharacterStats {

    int health;

    int attack;

    int defense;
}
