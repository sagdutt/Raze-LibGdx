package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.RazeGame;
import com.mygdx.game.character.Character;
import com.mygdx.game.client.SocketIOClient;
import com.mygdx.game.constant.AppConstants;
import com.mygdx.game.constant.CharacterConstants.CharacterType;
import com.mygdx.game.constant.SocketEventConstants;
import com.mygdx.game.event.Event;
import com.mygdx.game.event.EventHandler;
import com.mygdx.game.event.eventbus.InMemoryEventBus;
import com.mygdx.game.event.events.*;
import com.mygdx.game.factory.TextureConfigFactory;
import com.mygdx.game.input.ArenaInputHandler;
import com.mygdx.game.input.InputHandler;
import org.json.JSONObject;

import java.util.*;

public class ArenaScreen implements Screen, EventHandler {

    private final RazeGame game;

    private final OrthographicCamera camera;

    private final TextureConfigFactory textureConfigFactory;

    private final Map<String, Character> connectedPlayers;

    private final SocketIOClient socketIOClient;

    private final Texture background;

    private Character player;

    private float timeSinceLastServerUpdate = 0.0f;

    private InputHandler inputHandler;

    public ArenaScreen(final RazeGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, AppConstants.APP_WIDTH, AppConstants.APP_HEIGHT);
        textureConfigFactory = new TextureConfigFactory();
        connectedPlayers = new HashMap<>();
        background = new Texture("Background/game_background_4.png"); //TODO : Refactor this
        InMemoryEventBus.getInstance().subscribe(this);
        socketIOClient = new SocketIOClient();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 1);

        //Update
        updateServer(delta);
        updateCamera(delta);
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

            for (Map.Entry<String, Character> characterEntry : connectedPlayers.entrySet()) {
                characterEntry.getValue().update(delta);
            }
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
        if (timeSinceLastServerUpdate >= AppConstants.SERVER_UPDATE_RATE && Objects.nonNull(player)) {
            try {
                JSONObject playerData = new JSONObject();
                playerData.put("x", player.getX());
                playerData.put("y", player.getY());
                playerData.put("flipX", player.isFlipX());
                playerData.put("state", player.getState().name());
                socketIOClient.emit(SocketEventConstants.PLAYER_MOVED, playerData);
                timeSinceLastServerUpdate = 0f;
            } catch (Exception e) {
                Gdx.app.error(AppConstants.APP_LOG_TAG, "Error while updating server" , e);
            }
        }
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

    @Override
    public List<Class<?>> getSubscribedEventClasses() {
        return Arrays.asList(SocketConnectedEvent.class,
                NewPlayerConnectedEvent.class,
                PlayerDisconnectedEvent.class,
                GetPlayersEvent.class,
                PlayerMovedEvent.class);
    }

    @Override
    public void handleEvent(final Event<?> event) {
        if (SocketConnectedEvent.class == event.getClass()) {
            handleSocketConnected((SocketConnectedEvent) event);
        } else if (NewPlayerConnectedEvent.class == event.getClass()) {
            handleNewPlayerConnected((NewPlayerConnectedEvent) event);
        } else if (PlayerDisconnectedEvent.class == event.getClass()) {
            handlePlayerDisconnected((PlayerDisconnectedEvent) event);
        } else if (GetPlayersEvent.class == event.getClass()) {
            handleGetPlayers((GetPlayersEvent) event);
        } else if (PlayerMovedEvent.class == event.getClass()) {
            handlePlayerMoved((PlayerMovedEvent) event);
        }
    }

    private void handlePlayerMoved(final PlayerMovedEvent event) {
        PlayerMovedEvent.PlayerMovedPayload playerMovedPayload = event.getPayload();
        if (connectedPlayers.containsKey(playerMovedPayload.getId())) {
            Character otherPlayer = connectedPlayers.get(playerMovedPayload.getId());
            otherPlayer.setX(playerMovedPayload.getPosition().x);
            otherPlayer.setY(playerMovedPayload.getPosition().y);
            otherPlayer.setFlipX(playerMovedPayload.isFlipX());
            if (otherPlayer.isCanMove()) {
                otherPlayer.setState(playerMovedPayload.getState());
            }
        }
    }

    private void handleGetPlayers(final GetPlayersEvent event) {
        List<GetPlayersEvent.GetPlayerPayload> getPlayerPayloadList = event.getPayload();
        getPlayerPayloadList.forEach(getPlayerPayload ->
                Gdx.app.postRunnable(() ->
                        connectedPlayers.put(getPlayerPayload.getId(),
                                new Character(textureConfigFactory.getTextureConfigForCharacter(CharacterType.ELF_WARRIOR),
                                        getPlayerPayload.getPosition(), getPlayerPayload.isFlipX()))));
    }

    private void handlePlayerDisconnected(final PlayerDisconnectedEvent event) {
        connectedPlayers.remove(event.getPayload());
    }

    private void handleNewPlayerConnected(final NewPlayerConnectedEvent event) {
        Gdx.app.postRunnable(() ->
                connectedPlayers.put(event.getPayload(),
                        new Character(textureConfigFactory.getTextureConfigForCharacter(CharacterType.ELF_WARRIOR),
                                new Vector2(camera.viewportWidth, camera.viewportHeight), false)));
    }

    private void handleSocketConnected(final SocketConnectedEvent socketConnectedEvent) {
        Gdx.app.postRunnable(() -> {
            player = new Character(textureConfigFactory.getTextureConfigForCharacter(CharacterType.ELF_WARRIOR),
                    new Vector2(camera.viewportWidth, camera.viewportHeight), false);
            inputHandler = new ArenaInputHandler(player, background);
        });
    }
}
