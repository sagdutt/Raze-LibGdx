package com.mygdx.game.event;

/**
 * Interface for events to be published to an event bus.
 * @param <T> the datatype of the event payload
 */
public interface Event<T> {

    /**
     * Returns the event payload.
     * @return the event payload.
     */
    T getPayload();
}
