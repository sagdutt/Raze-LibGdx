package com.mygdx.game.event.events;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.constant.State;
import com.mygdx.game.event.Event;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

/**
 * Event containing info about a remote player. Also used to send the local player's info to the server.
 */
@Value
@Builder
public class PlayerUpdatedEvent implements Event<PlayerUpdatedEvent.PlayerUpdatedPayload> {

    @Getter(AccessLevel.PRIVATE)
    PlayerUpdatedPayload playerUpdatedPayload;

    @Value
    @Builder
    public static class PlayerUpdatedPayload {
        String id;

        Vector2 position;

        boolean flipX;

        State state;
    }

    @Override
    public PlayerUpdatedPayload getPayload() {
        return playerMUpdatedPayload;
    }
}
