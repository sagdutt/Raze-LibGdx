package com.mygdx.game.event.events;

import com.mygdx.game.constant.CharacterConstants;
import com.mygdx.game.event.Event;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Value
@Builder
public class CharacterSelectedEvent implements Event<CharacterSelectedEvent.CharacterSelectedPayload> {

    @Getter(AccessLevel.PRIVATE)
    CharacterSelectedPayload characterSelectedPayload;

    @Value
    @Builder
    public static class CharacterSelectedPayload {

        CharacterConstants.CharacterType character;

        String name;
    }

    @Override
    public CharacterSelectedPayload getPayload() {
        return characterSelectedPayload;
    }
}
