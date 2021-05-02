package com.mygdx.game.constant;

import com.mygdx.game.model.AnimConfig;

import java.util.HashMap;
import java.util.Map;

public final class CharacterAnimStateConstants {

    private static final Map<State, AnimConfig> ELF_WARRIOR_ANIM_MAP = new HashMap<>();

    private static final Map<State, AnimConfig> ELF_ARCHER_ANIM_MAP = new HashMap<>();

    private static final Map<State, AnimConfig> ELF_MAGE_ANIM_MAP = new HashMap<>();

    static {
        ELF_ARCHER_ANIM_MAP.put(State.IDLE, new AnimConfig("Elf_01__IDLE", 1/25f));
        ELF_ARCHER_ANIM_MAP.put(State.MOVING, new AnimConfig("Elf_01__RUN", 1/25f));
        ELF_ARCHER_ANIM_MAP.put(State.ATTACKING, new AnimConfig("Elf_01__ATTACK", 1/25f));

        ELF_WARRIOR_ANIM_MAP.put(State.IDLE, new AnimConfig("Elf_02__IDLE", 1/25f));
        ELF_WARRIOR_ANIM_MAP.put(State.MOVING, new AnimConfig("Elf_02__RUN", 1/25f));
        ELF_WARRIOR_ANIM_MAP.put(State.ATTACKING, new AnimConfig("Elf_02__ATTACK", 1/25f));

        ELF_MAGE_ANIM_MAP.put(State.IDLE, new AnimConfig("Elf_03__IDLE", 1/25f));
        ELF_MAGE_ANIM_MAP.put(State.MOVING, new AnimConfig("Elf_03__RUN", 1/25f));
        ELF_MAGE_ANIM_MAP.put(State.ATTACKING, new AnimConfig("Elf_03__ATTACK", 1/25f));
    }

    public static Map<State, AnimConfig> getElfWarriorAnimMap() {
        return ELF_WARRIOR_ANIM_MAP;
    }

    public static Map<State, AnimConfig> getElfArcherAnimMap() {
        return ELF_ARCHER_ANIM_MAP;
    }

    public static Map<State, AnimConfig> getElfMageAnimMap() {
        return ELF_MAGE_ANIM_MAP;
    }

    private CharacterAnimStateConstants() {
    }
}
