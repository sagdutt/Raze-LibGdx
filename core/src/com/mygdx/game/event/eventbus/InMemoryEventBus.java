package com.mygdx.game.event.eventbus;

import com.mygdx.game.event.Event;
import com.mygdx.game.event.EventBus;
import com.mygdx.game.event.EventHandler;

import java.util.*;

/**
 * In-memory implementation of event bus.
 */
public class InMemoryEventBus implements EventBus {

    private static InMemoryEventBus instance;

    private final Map<Class<?>, List<EventHandler>> eventHandlerMap;

    private InMemoryEventBus() {
        eventHandlerMap = new HashMap<>();
    }

    public static InMemoryEventBus getInstance() {
        if (Objects.isNull(instance)) {
            instance = new InMemoryEventBus();
        }
        return instance;
    }

    @Override
    public void publish(final Event<?> event) {
        if (Objects.nonNull(event) && eventHandlerMap.containsKey(event.getClass())) {
            eventHandlerMap.get(event.getClass())
                    .forEach(eventHandler -> eventHandler.handleEvent(event));
        }
    }

    @Override
    public void subscribe(final EventHandler eventHandler) {
        if (Objects.nonNull(eventHandler)) {
            eventHandler.getSubscribedEventClasses()
                    .forEach(clazz -> {
                        if (!eventHandlerMap.containsKey(clazz)) {
                            eventHandlerMap.put(clazz, new ArrayList<>());
                        }
                        eventHandlerMap.get(clazz).add(eventHandler);
                    });
        }
    }
}
