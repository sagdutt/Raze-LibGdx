package com.mygdx.game.event.events;

import com.mygdx.game.event.Event;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

/**
 * Event containing the id of the disconnected player.
 */
@Value
@Builder
public class PlayerDisconnectedEvent implements Event<String> {

    @Getter(AccessLevel.PRIVATE)
    String id;

    @Override
    public String getPayload() {
        return id;
    }
}
