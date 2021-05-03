package com.mygdx.game.event;

/**
 * Interface for an event bus which sends published events to subscribed event handler.
 */
public interface EventBus {

    /**
     * Publishes the given event to the event bus.
     * @param event the event to be published
     */
    void publish(final Event<?> event);

    /**
     * Subscribes the provided event handler to receive events from the event bus.
     * @param eventHandler the event handler to subscribe
     */
    void subscribe(final EventHandler eventHandler);
}
