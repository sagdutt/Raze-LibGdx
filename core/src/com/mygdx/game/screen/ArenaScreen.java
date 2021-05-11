package com.mygdx.game.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.character.Character;
import com.mygdx.game.client.SocketIOClient;
import com.mygdx.game.constant.AppConstants;
import com.mygdx.game.constant.CharacterConstants;
import com.mygdx.game.constant.SocketEventConstants;
import com.mygdx.game.dto.CharacterReadyDto;
import com.mygdx.game.dto.PlayerMovedDto;
import com.mygdx.game.input.ArenaInputHandler;
import com.mygdx.game.input.InputHandler;
import com.mygdx.game.repository.ConnectedPlayersRepository;
import com.mygdx.game.repository.LocalPlayerRepository;
import lombok.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.Objects;

@Singleton
public class ArenaScreen implements Screen {

    private final Batch batch;

    private final Camera camera;

    private final SocketIOClient socketIOClient;

    private final ConnectedPlayersRepository connectedPlayersRepository;

    private final LocalPlayerRepository localPlayerRepository;

    private final Texture background;

    private Character player;

    private float timeSinceLastServerUpdate = 0.0f;

    private InputHandler inputHandler;

    @Inject
    public ArenaScreen(@NonNull final Batch batch,
                       @NonNull final Camera camera,
                       @NonNull final SocketIOClient socketIOClient,
                       @NonNull final ConnectedPlayersRepository connectedPlayersRepository,
                       @NonNull final LocalPlayerRepository localPlayerRepository) {
        this.batch = batch;
        this.camera = camera;
        this.socketIOClient = socketIOClient;
        this.connectedPlayersRepository = connectedPlayersRepository;
        this.localPlayerRepository = localPlayerRepository;
        this.background = new Texture("Background/game_background_4.png"); //TODO : Refactor this
    }

    /**
     * Initializes the local player. Notifies the server once the player is successfully created.
     * @param characterType
     */
    public void initializePlayer(final CharacterConstants.CharacterType characterType, final String characterName) {
        player = localPlayerRepository.initializePlayer(characterType, characterName);
        inputHandler = new ArenaInputHandler(player, background, connectedPlayersRepository, socketIOClient);
        CharacterReadyDto dto = CharacterReadyDto.builder()
                .name(characterName)
                .character(characterType)
                .build();
        socketIOClient.emit(SocketEventConstants.PLAYER_READY, dto.toJsonObject());
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 1);

        //Update
        updateServer(delta);
        updateCamera(delta);
        updateCharacters(delta);

        //Draw
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        if (Objects.nonNull(player)) {
            batch.draw(background, 0, 0);
            for (Map.Entry<String, Character> characterEntry : connectedPlayersRepository.getConnectedPlayers().entrySet()) {
                characterEntry.getValue().draw(batch);
            }
            player.draw(batch);
        }
        batch.end();
    }

    private void updateCharacters(float delta) {
        if (Objects.nonNull(player)) {
            inputHandler.handleInput(delta);
            player.update(delta);

            for (Map.Entry<String, Character> characterEntry : connectedPlayersRepository.getConnectedPlayers().entrySet()) {
                characterEntry.getValue().update(delta);
            }
        }
    }

    private void updateCamera(float delta) {
        if (Objects.nonNull(player)) {
            float nextX = player.getBounds().x + (player.getBounds().width / 2);
            if (nextX - (camera.viewportWidth / 2) > 0 && nextX + (camera.viewportWidth / 2) < background.getWidth()) {
                camera.position.x = nextX;
            }
            float nextY = player.getBounds().y + (player.getBounds().height / 2);
            if (nextY - (camera.viewportHeight/2) > 0 && nextY + (camera.viewportHeight/2) < background.getHeight()) {
                camera.position.y = nextY;
            }
        }
        camera.update();
    }

    private void updateServer(float delta) {
        timeSinceLastServerUpdate += delta;
        if (timeSinceLastServerUpdate >= AppConstants.SERVER_UPDATE_RATE && Objects.nonNull(player)) {
            PlayerMovedDto dto = PlayerMovedDto.builder()
                        .x(player.getPosition().x)
                        .y(player.getPosition().y)
                        .flipX(player.isFlipX())
                        .state(player.getState())
                        .build();
            socketIOClient.emit(SocketEventConstants.PLAYER_MOVED, dto.toJsonObject());
            timeSinceLastServerUpdate = 0f;
        }
    }

    @Override
    public void dispose() {
        player.dispose();
        for (Map.Entry<String, Character> characterEntry : connectedPlayersRepository.getConnectedPlayers().entrySet()) {
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
