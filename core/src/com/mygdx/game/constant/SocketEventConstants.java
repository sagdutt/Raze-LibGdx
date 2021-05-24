package com.mygdx.game.constant;

import io.socket.client.Socket;

public final class SocketEventConstants {

    public static final String CONNECT = Socket.EVENT_CONNECT;

    public static final String SOCKET_ID = "socketId";

    public static final String NEW_PLAYER_CONNECTED = "newPlayerConnected";

    public static final String PLAYER_DISCONNECTED = "playerDisconnected";

    public static final String GET_PLAYERS = "getExistingPlayers";

    public static final String PLAYER_MOVED = "playerUpdated";

    private SocketEventConstants() {
    }
}
