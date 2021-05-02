package com.mygdx.game.factory;

import com.mygdx.game.constant.CharacterType;
import com.mygdx.game.constant.CharacterAnimStateConstants;
import com.mygdx.game.model.TextureConfig;

public class TextureConfigFactory {

    private final TextureConfig elfWarriorConfig = new TextureConfig("ElfWarrior/ElfWarrior.atlas",
            400, 100, CharacterAnimStateConstants.getElfWarriorAnimMap());

    public TextureConfig getTextureConfigForCharacter(final CharacterType characterType) {
        switch (characterType) {
            case ELF_WARRIOR:
                return elfWarriorConfig;
            default:
                throw new IllegalStateException("Unexpected value: " + characterType);
        }
    }
}
