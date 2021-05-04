package com.mygdx.game.event.events;

import com.mygdx.game.constant.CharacterConstants;
import com.mygdx.game.event.Event;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

/**
 * Event containing the id of the newly connected player.
 */
@Value
@Builder
public class NewPlayerConnectedEvent implements Event<NewPlayerConnectedEvent.NewPlayerConnectedPayload> {

    @Getter(AccessLevel.PRIVATE)
    NewPlayerConnectedPayload newPlayerConnectedPayload;

    @Value
    @Builder
    public static class NewPlayerConnectedPayload {

        String id;

        CharacterConstants.CharacterType characterType;
    }

    @Override
    public NewPlayerConnectedPayload getPayload() {
        return newPlayerConnectedPayload;
    }
}
