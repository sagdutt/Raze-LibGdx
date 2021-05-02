package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.RazeGame;
import com.mygdx.game.character.Character;
import com.mygdx.game.constant.AppConstants;
import com.mygdx.game.constant.CharacterType;
import com.mygdx.game.constant.SocketEventConstants;
import com.mygdx.game.constant.State;
import com.mygdx.game.factory.TextureConfigFactory;
import com.mygdx.game.input.ArenaInputHandler;
import com.mygdx.game.input.InputHandler;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ArenaScreen implements Screen {

    private final RazeGame game;

    private final OrthographicCamera camera;

    private final TextureConfigFactory textureConfigFactory;

    private final Map<String, Character> connectedPlayers;

    private final Texture background;

    private Character player;

    private Socket socket;

    private float timeSinceLastServerUpdate = 0.0f;

    private InputHandler inputHandler;

    public ArenaScreen(final RazeGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, AppConstants.APP_WIDTH, AppConstants.APP_HEIGHT);
        textureConfigFactory = new TextureConfigFactory();
        connectedPlayers = new HashMap<>();
        background = new Texture("Background/game_background_4.png");
        connectSocket();
        configureSocketEvents();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 1);

        //Update
        updateCamera(delta);
        updateServer(delta);
        updateCharacters(delta);

        //Draw
        game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();
        if (Objects.nonNull(player)) {
            game.getBatch().draw(background, 0, 0);
            for (Map.Entry<String, Character> characterEntry : connectedPlayers.entrySet()) {
                characterEntry.getValue().draw(game.getBatch());
            }
            player.draw(game.getBatch());
        }
        game.getBatch().end();
    }

    private void updateCharacters(float delta) {
        if (Objects.nonNull(player)) {
            inputHandler.handleInput(delta);
            player.update(delta);
        }
        for (Map.Entry<String, Character> characterEntry : connectedPlayers.entrySet()) {
            characterEntry.getValue().update(delta);
        }
    }

    private void updateCamera(float delta) {
        if (Objects.nonNull(player)) {
            float nextX = player.getX() + (player.getTextureSize().x / 2);
            if (nextX - (camera.viewportWidth / 2) > 0 && nextX + (camera.viewportWidth / 2) < background.getWidth()) {
                camera.position.x = nextX;
            }
            float nextY = player.getY() + (player.getTextureSize().y / 2);
            if (nextY - (camera.viewportHeight/2) > 0 && nextY + (camera.viewportHeight/2) < background.getHeight()) {
                camera.position.y = nextY;
            }
        }
        camera.update();
    }

    private void updateServer(float delta) {
        timeSinceLastServerUpdate += delta;
        if (timeSinceLastServerUpdate >= AppConstants.SERVER_UPDATE_RATE && Objects.nonNull(player) && player.isDirty()) {
            try {
                JSONObject playerData = new JSONObject();
                playerData.put("x", player.getX());
                playerData.put("y", player.getY());
                playerData.put("flipX", player.isFlipX());
                playerData.put("state", player.getState().name());
                socket.emit(SocketEventConstants.PLAYER_MOVED, playerData);
            } catch (Exception e) {
                Gdx.app.error(AppConstants.SOCKET_IO_LOG_TAG, "Error while updating server" , e);
            }

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
        socket.on(SocketEventConstants.CONNECT, args -> {
            Gdx.app.log(AppConstants.SOCKET_IO_LOG_TAG, "Connected");
            Gdx.app.postRunnable(() -> {
                    player = new Character(textureConfigFactory.getTextureConfigForCharacter(CharacterType.ELF_WARRIOR),
                            new Vector2(camera.viewportWidth, camera.viewportHeight), false);
                    inputHandler = new ArenaInputHandler(player, background);
            });
        }).on(SocketEventConstants.SOCKET_ID, args -> {
            JSONObject data = (JSONObject) args[0];
            try {
                String id = data.getString("id");
                Gdx.app.log(AppConstants.SOCKET_IO_LOG_TAG, "Socket ID : " + id);
            } catch (Exception e) {
                Gdx.app.error(AppConstants.SOCKET_IO_LOG_TAG, "Error while getting socket id", e);
            }
        }).on(SocketEventConstants.NEW_PLAYER_CONNECTED, args -> {
            JSONObject data = (JSONObject) args[0];
            try {
                String id = data.getString("id");
                Gdx.app.log(AppConstants.SOCKET_IO_LOG_TAG, "New player connected. Player ID : " + id);
                Gdx.app.postRunnable(() ->
                        connectedPlayers.put(id,
                                new Character(textureConfigFactory.getTextureConfigForCharacter(CharacterType.ELF_WARRIOR))));
            } catch (Exception e) {
                Gdx.app.error(AppConstants.SOCKET_IO_LOG_TAG, "Error while getting new player id", e);
            }
        }).on(SocketEventConstants.PLAYER_DISCONNECTED, args -> {
            JSONObject data = (JSONObject) args[0];
            try {
                String id = data.getString("id");
                Gdx.app.log(AppConstants.SOCKET_IO_LOG_TAG, "Player disconnected. Player ID : " + id);
                connectedPlayers.remove(id);
            } catch (Exception e) {
                Gdx.app.error(AppConstants.SOCKET_IO_LOG_TAG, "Error while getting disconnected player id", e);
            }
        }).on(SocketEventConstants.GET_PLAYERS, args -> {
            JSONArray playerInfoList = (JSONArray) args[0];
            try {
                for (int i = 0; i < playerInfoList.length(); i++) {
                    JSONObject playerInfo = (JSONObject) playerInfoList.get(i);
                    String id = playerInfo.getString("id");
                    Vector2 position = new Vector2(((Double) playerInfo.getDouble("x")).floatValue(),
                            ((Double) playerInfo.getDouble("y")).floatValue());
                    boolean flipX = playerInfo.getBoolean("flipX");
                    Gdx.app.postRunnable(() ->
                            connectedPlayers.put(id,
                                    new Character(textureConfigFactory.getTextureConfigForCharacter(CharacterType.ELF_WARRIOR),
                                            position, flipX)));
                }
                Gdx.app.log(AppConstants.SOCKET_IO_LOG_TAG, "GetPlayers succeeded");
            } catch (Exception e) {
                Gdx.app.error(AppConstants.SOCKET_IO_LOG_TAG, "Error while getting players", e);
            }
        }).on(SocketEventConstants.PLAYER_MOVED, args -> {
            JSONObject data = (JSONObject) args[0];
            try {
                String id = data.getString("id");
                if (connectedPlayers.containsKey(id)) {
                    connectedPlayers.get(id).setX(((Double) data.getDouble("x")).floatValue());
                    connectedPlayers.get(id).setY(((Double) data.getDouble("y")).floatValue());
                    connectedPlayers.get(id).setFlipX((data.getBoolean("flipX")));
                    if (connectedPlayers.get(id).isCanMove()) {
                        connectedPlayers.get(id).setState(State.valueOf(data.getString("state")));
                    }
                }
            } catch (Exception e) {
                Gdx.app.error(AppConstants.SOCKET_IO_LOG_TAG, "Error while processing player moved", e);
            }
        });
    }

    @Override
    public void dispose() {
        player.dispose();
        for (Map.Entry<String, Character> characterEntry : connectedPlayers.entrySet()) {
            characterEntry.getValue().dispose();
        }
        background.dispose();
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void resize(int width, int height) {
    }
}
