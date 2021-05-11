package com.mygdx.game.event.events;

import com.mygdx.game.event.Event;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Value
@Builder
public class TakeDamageEvent implements Event<TakeDamageEvent.TakeDamageEventPayload> {

    @Value
    @Builder
    public static class TakeDamageEventPayload {
        String id;

        int damage;

        boolean localPlayer;
    }

    @Getter(AccessLevel.PRIVATE)
    TakeDamageEventPayload takeDamageEventPayload;

    @Override
    public TakeDamageEventPayload getPayload() {
        return takeDamageEventPayload;
    }
}
