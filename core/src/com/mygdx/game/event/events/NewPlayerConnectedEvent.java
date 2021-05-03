package com.mygdx.game.event.events;

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
public class NewPlayerConnectedEvent implements Event<String> {

    @Getter(AccessLevel.PRIVATE)
    String id;

    @Override
    public String getPayload() {
        return id;
    }
}
