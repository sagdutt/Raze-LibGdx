package com.mygdx.game.event.events;

import com.mygdx.game.event.Event;
import lombok.Value;

/**
 * Event which signifies that a connection has been established with the server.
 */
@Value
public class SocketConnectedEvent implements Event<Void> {

    @Override
    public Void getPayload() {
        return null;
    }
}
