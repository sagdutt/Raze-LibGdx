package com.mygdx.game.event.events;

import com.mygdx.game.constant.CharacterConstants;
import com.mygdx.game.event.Event;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Value
@Builder
public class CharacterSelectedEvent implements Event<CharacterConstants.CharacterType> {

    @Getter(AccessLevel.PRIVATE)
    CharacterConstants.CharacterType character;

    @Override
    public CharacterConstants.CharacterType getPayload() {
        return character;
    }
}
