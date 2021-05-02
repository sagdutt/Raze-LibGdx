package com.mygdx.game.constant;

import com.mygdx.game.model.AnimConfig;

import java.util.HashMap;
import java.util.Map;

public final class CharacterAnimStateConstants {

    private static final Map<State, AnimConfig> ELF_WARRIOR_ANIM_MAP = new HashMap<>();

    static {
        ELF_WARRIOR_ANIM_MAP.put(State.IDLE, new AnimConfig("Elf_02__IDLE", 1/25f));
        ELF_WARRIOR_ANIM_MAP.put(State.MOVING, new AnimConfig("Elf_02__RUN", 1/25f));
        ELF_WARRIOR_ANIM_MAP.put(State.ATTACKING, new AnimConfig("Elf_02__ATTACK", 1/25f));
    }

    public static Map<State, AnimConfig> getElfWarriorAnimMap() {
        return ELF_WARRIOR_ANIM_MAP;
    }

    private CharacterAnimStateConstants() {
    }
}
