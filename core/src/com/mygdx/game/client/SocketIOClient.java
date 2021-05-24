package com.mygdx.game.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.constant.AppConstants;
import com.mygdx.game.constant.SocketEventConstants;
import com.mygdx.game.constant.State;
import com.mygdx.game.event.EventBus;
import com.mygdx.game.event.eventbus.InMemoryEventBus;
import com.mygdx.game.event.events.*;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import lombok.NonNull;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * Client for establishing a connection to the socket io server. Can be used to send messages to the server.
 * Publishes all messages received from the server to the event bus.
 */
@Singleton
public class SocketIOClient {

    private final EventBus eventBus;

    private Socket socket;

    @Inject
    public SocketIOClient(@NonNull final EventBus eventBus) {
        this.eventBus = eventBus;
        connectSocket();
        configureSocketEvents();
    }

    /**
     * Sends the given message to the socket io server.
     * @param eventName name of the event
     * @param payload JSONObject payload
     */
    public void emit(final String eventName, final JSONObject payload) {
        try {
            socket.emit(eventName, payload);
        } catch (Exception e) {
            Gdx.app.error(AppConstants.SOCKET_IO_LOG_TAG, "Error while updating server" , e);
        }
    }

    /**
     * Sends the given message to the socket io server.
     * @param eventName name of the event
     * @param payload JSONArray payload
     */
    public void emit(final String eventName, final JSONArray payload) {
        try {
            socket.emit(eventName, payload);
        } catch (Exception e) {
            Gdx.app.error(AppConstants.SOCKET_IO_LOG_TAG, "Error while updating server" , e);
        }
    }

    private void connectSocket() {
        try {
            socket = IO.socket(AppConstants.SERVER_URL);
            socket.connect();
        } catch (Exception e) {
            Gdx.app.error(AppConstants.SOCKET_IO_LOG_TAG, "Connection Error" , e);
        }
    }

    private void configureSocketEvents() {
        socket.on(SocketEventConstants.CONNECT, getConnectEventListener())
                .on(SocketEventConstants.SOCKET_ID, getSocketIdEventListener())
                .on(SocketEventConstants.NEW_PLAYER_CONNECTED, getNewPlayerConnectedEventListener())
                .on(SocketEventConstants.PLAYER_DISCONNECTED, getPlayerDisconnectedEventListener())
                .on(SocketEventConstants.GET_PLAYERS, getGetExistingPlayersEventListener())
                .on(SocketEventConstants.PLAYER_MOVED, getPlayerUpdatedEventListener());
    }

    private Emitter.Listener getPlayerUpdatedEventListener() {
        return args -> {
            JSONObject data = (JSONObject) args[0];
            try {
                String id = data.getString("id");
                float x = ((Double) data.getDouble("x")).floatValue();
                float y = ((Double) data.getDouble("y")).floatValue();
                boolean flipX = data.getBoolean("flipX");
                State state = State.valueOf(data.getString("state"));

                eventBus.publish(PlayerUpdatedEvent.builder()
                        .playerUpdatedPayload(PlayerUpdatedEvent.PlayerUpdatedPayload.builder()
                                .id(id)
                                .position(new Vector2(x, y))
                                .flipX(flipX)
                                .state(state)
                                .build())
                        .build());
            } catch (Exception e) {
                Gdx.app.error(AppConstants.SOCKET_IO_LOG_TAG, "Error while processing player updated", e);
            }
        };
    }

    private Emitter.Listener getGetExistingPlayersEventListener() {
        return args -> {
            JSONArray playerInfoList = (JSONArray) args[0];
            try {
                List<GetExistingPlayersEvent.GetPlayerPayload> getPlayerPayloadList = new ArrayList<>();
                for (int i = 0; i < playerInfoList.length(); i++) {
                    JSONObject playerInfo = (JSONObject) playerInfoList.get(i);
                    String id = playerInfo.getString("id");
                    Vector2 position = new Vector2(((Double) playerInfo.getDouble("x")).floatValue(),
                            ((Double) playerInfo.getDouble("y")).floatValue());
                    boolean flipX = playerInfo.getBoolean("flipX");
                    getPlayerPayloadList.add(GetExistingPlayersEvent.GetPlayerPayload.builder()
                            .id(id)
                            .position(position)
                            .flipX(flipX)
                            .build());
                }
                Gdx.app.log(AppConstants.SOCKET_IO_LOG_TAG, "GetExistingPlayers succeeded");
                eventBus.publish(GetExistingPlayersEvent.builder()
                        .getPlayerPayloadList(getPlayerPayloadList)
                        .build());
            } catch (Exception e) {
                Gdx.app.error(AppConstants.SOCKET_IO_LOG_TAG, "Error while getting players", e);
            }
        };
    }

    private Emitter.Listener getPlayerDisconnectedEventListener() {
        return args -> {
            JSONObject data = (JSONObject) args[0];
            try {
                String id = data.getString("id");
                Gdx.app.log(AppConstants.SOCKET_IO_LOG_TAG, "Player disconnected. Player ID : " + id);
                eventBus.publish(PlayerDisconnectedEvent.builder()
                        .id(id)
                        .build());
            } catch (Exception e) {
                Gdx.app.error(AppConstants.SOCKET_IO_LOG_TAG, "Error while getting disconnected player id", e);
            }
        };
    }

    private Emitter.Listener getNewPlayerConnectedEventListener() {
        return args -> {
            JSONObject data = (JSONObject) args[0];
            try {
                String id = data.getString("id");
                Gdx.app.log(AppConstants.SOCKET_IO_LOG_TAG, "New player connected. Player ID : " + id);
                eventBus.publish(NewPlayerConnectedEvent.builder()
                        .id(id)
                        .build());
            } catch (Exception e) {
                Gdx.app.error(AppConstants.SOCKET_IO_LOG_TAG, "Error while getting new player id", e);
            }
        };
    }

    private Emitter.Listener getSocketIdEventListener() {
        return args -> {
            JSONObject data = (JSONObject) args[0];
            try {
                String id = data.getString("id");
                Gdx.app.log(AppConstants.SOCKET_IO_LOG_TAG, "Socket ID : " + id);
            } catch (Exception e) {
                Gdx.app.error(AppConstants.SOCKET_IO_LOG_TAG, "Error while getting socket id", e);
            }
        };
    }

    private Emitter.Listener getConnectEventListener() {
        return args -> {
            Gdx.app.log(AppConstants.SOCKET_IO_LOG_TAG, "Connected");
            eventBus.publish(new SocketConnectedEvent());
        };
    }
}
