package com.mygdx.game.factory;

import com.mygdx.game.constant.CharacterType;
import com.mygdx.game.constant.CharacterAnimStateConstants;
import com.mygdx.game.model.TextureConfig;

public class TextureConfigFactory {

    private final TextureConfig elfWarriorConfig = new TextureConfig("ElfWarrior/ElfWarrior.atlas",
            400, 100, CharacterAnimStateConstants.getElfWarriorAnimMap());

    private final TextureConfig elfArcherConfig = new TextureConfig("ElfArcher/ElfArcher.atlas",
            400, 100, CharacterAnimStateConstants.getElfArcherAnimMap());

    private final TextureConfig elfMageConfig = new TextureConfig("ElfMage/ElfMage.atlas",
            400, 100, CharacterAnimStateConstants.getElfMageAnimMap());

    public TextureConfig getTextureConfigForCharacter(final CharacterType characterType) {
        switch (characterType) {
            case ELF_WARRIOR:
                return elfWarriorConfig;
            case ELF_ARCHER:
                return elfArcherConfig;
            case ELF_MAGE:
                return elfMageConfig;
            default:
                throw new IllegalStateException("Unexpected value: " + characterType);
        }
    }
}
