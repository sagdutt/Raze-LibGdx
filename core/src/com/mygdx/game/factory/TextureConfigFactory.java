package com.mygdx.game.factory;

import com.mygdx.game.constant.CharacterConstants.CharacterType;
import com.mygdx.game.constant.State;
import com.mygdx.game.model.AnimConfig;
import com.mygdx.game.model.TextureConfig;
import com.mygdx.game.util.FluentHashMap;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

@Singleton
public class TextureConfigFactory {

    private static final TextureConfig ELF_WARRIOR_CONFIG = TextureConfig.builder()
            .texturePath("ElfWarrior/ElfWarrior.atlas")
            .textureWidth(400)
            .textureHeight(100)
            .animConfigMap(new FluentHashMap<State, AnimConfig>()
                    .withEntry(State.IDLE, AnimConfig.builder().name("Elf_02__IDLE").frameRate(1/25f).build())
                    .withEntry(State.MOVING, AnimConfig.builder().name("Elf_02__RUN").frameRate(1/25f).build())
                    .withEntry(State.ATTACKING, AnimConfig.builder().name("Elf_02__ATTACK").frameRate(1/25f).build()))
            .build();

    private static final TextureConfig ELF_ARCHER_CONFIG = TextureConfig.builder()
            .texturePath("ElfArcher/ElfArcher.atlas")
            .textureWidth(400)
            .textureHeight(100)
            .animConfigMap(new FluentHashMap<State, AnimConfig>()
                    .withEntry(State.IDLE, AnimConfig.builder().name("Elf_01__IDLE").frameRate(1/25f).build())
                    .withEntry(State.MOVING, AnimConfig.builder().name("Elf_01__RUN").frameRate(1/25f).build())
                    .withEntry(State.ATTACKING, AnimConfig.builder().name("Elf_01__ATTACK").frameRate(1/25f).build()))
            .build();

    private static final TextureConfig ELF_MAGE_CONFIG = TextureConfig.builder()
            .texturePath("ElfMage/ElfMage.atlas")
            .textureWidth(400)
            .textureHeight(100)
            .animConfigMap(new FluentHashMap<State, AnimConfig>()
                    .withEntry(State.IDLE, AnimConfig.builder().name("Elf_03__IDLE").frameRate(1/25f).build())
                    .withEntry(State.MOVING, AnimConfig.builder().name("Elf_03__RUN").frameRate(1/25f).build())
                    .withEntry(State.ATTACKING, AnimConfig.builder().name("Elf_03__ATTACK").frameRate(1/25f).build()))
            .build();

    @Inject
    public TextureConfigFactory() {
    }

    public TextureConfig getTextureConfigForCharacter(final CharacterType characterType) {
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

    public Map<CharacterType, TextureConfig> getCharacterTypeTextureConfigMap() {
        return new FluentHashMap<CharacterType, TextureConfig>()
                .withEntry(CharacterType.ELF_WARRIOR, ELF_WARRIOR_CONFIG)
                .withEntry(CharacterType.ELF_ARCHER, ELF_ARCHER_CONFIG)
                .withEntry(CharacterType.ELF_MAGE, ELF_MAGE_CONFIG);
    }
}
