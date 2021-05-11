package com.mygdx.game.factory;

import com.mygdx.game.constant.CharacterConstants;
import com.mygdx.game.model.CharacterConfig;
import com.mygdx.game.model.CharacterStats;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CharacterConfigFactory {

    private static final CharacterConfig ELF_WARRIOR_CONFIG = CharacterConfig.builder()
            .boundsXOffset(150f)
            .boundsYOffset(0f)
            .boundsWidth(100f)
            .boundsHeight(100f)
            .attackAreaXOffset(200f)
            .attackAreaYOffset(0f)
            .attackAreaWidth(70f)
            .attackAreaHeight(100f)
            .characterStats(CharacterStats.builder()
                    .attack(30)
                    .defense(20)
                    .health(100)
                    .build())
            .build();

    private static final CharacterConfig ELF_ARCHER_CONFIG = CharacterConfig.builder()
            .boundsXOffset(150f)
            .boundsYOffset(0f)
            .boundsWidth(100f)
            .boundsHeight(100f)
            .attackAreaXOffset(200f)
            .attackAreaYOffset(0f)
            .attackAreaWidth(180f)
            .attackAreaHeight(100f)
            .characterStats(CharacterStats.builder()
                    .attack(15)
                    .defense(10)
                    .health(60)
                    .build())
            .build();

    private static final CharacterConfig ELF_MAGE_CONFIG = CharacterConfig.builder()
            .boundsXOffset(150f)
            .boundsYOffset(0f)
            .boundsWidth(100f)
            .boundsHeight(100f)
            .attackAreaXOffset(200f)
            .attackAreaYOffset(0f)
            .attackAreaWidth(150f)
            .attackAreaHeight(100f)
            .characterStats(CharacterStats.builder()
                    .attack(20)
                    .defense(5)
                    .health(80)
                    .build())
            .build();

    @Inject
    public CharacterConfigFactory() {
    }

    public CharacterConfig getCharacterConfigForCharacter(final CharacterConstants.CharacterType characterType) {
        switch (characterType) {
            case ELF_WARRIOR:
                return ELF_WARRIOR_CONFIG;
            case ELF_ARCHER:
                return ELF_ARCHER_CONFIG;
            case ELF_MAGE:
                return ELF_MAGE_CONFIG;
            default:
                throw new IllegalArgumentException("Unexpected value: " + characterType);
        }
    }
}
