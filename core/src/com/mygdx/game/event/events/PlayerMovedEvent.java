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
public class PlayerMovedEvent implements Event<PlayerMovedEvent.PlayerMovedPayload> {

    @Getter(AccessLevel.PRIVATE)
    PlayerMovedPayload playerMovedPayload;

    @Value
    @Builder
    public static class PlayerMovedPayload {
        String id;

        Vector2 position;

        boolean flipX;

        State state;
    }

    @Override
    public PlayerMovedPayload getPayload() {
        return playerMovedPayload;
    }
}
