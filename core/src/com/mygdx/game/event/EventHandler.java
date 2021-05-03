package com.mygdx.game.event;

import java.util.List;

/**
 * Interface for classes which want to receive events from an event bus.
 */
public interface EventHandler {

    /**
     * Used by an event bus to filter events by class before sending it to this event handler.
     * @return List of events this event handler wants to receive from the event bus.
     */
    List<Class<?>> getSubscribedEventClasses();

    /**
     * Called by the event bus with events which have been subscribed to by this event handler.
     * @param event a subscribed event
     */
    void handleEvent(final Event<?> event);
}
