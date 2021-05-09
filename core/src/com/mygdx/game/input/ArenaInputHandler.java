package com.mygdx.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.character.Character;
import com.mygdx.game.constant.AppConstants;
import com.mygdx.game.constant.State;
import com.mygdx.game.repository.ConnectedPlayersRepository;

import static com.mygdx.game.constant.CharacterConstants.PLAYER_MOVEMENT_SPEED;

public class ArenaInputHandler implements InputHandler {

    private final Character player;

    private final Texture background;

    private final ConnectedPlayersRepository connectedPlayersRepository;

    public ArenaInputHandler(final Character player,
                             final Texture background,
                             final ConnectedPlayersRepository connectedPlayersRepository) {
        this.player = player;
        this.background = background;
        this.connectedPlayersRepository = connectedPlayersRepository;
    }

    @Override
    public void handleInput(float deltaTime) {
        if (Gdx.input.isTouched()) {
            player.setState(State.ATTACKING);
            player.setCanMove(false);
            connectedPlayersRepository.getConnectedPlayers().forEach((id, character) -> {
                if (character.getBounds().overlaps(player.getAttackArea())) {
                    Gdx.app.log(AppConstants.APP_LOG_TAG, String.format("%s attacked %s", player.getName(), character.getName()));
                    // TODO: Add actual attack logic
                }
            });
        }
        if (player.isCanMove()) {
            boolean isMoving = false;
            Vector2 playerPosition = player.getPosition();
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                if (player.getBounds().x + (-PLAYER_MOVEMENT_SPEED * deltaTime) > 0) {
                    playerPosition.x = playerPosition.x + (-PLAYER_MOVEMENT_SPEED * deltaTime);
                }
                player.setFlipX(true);
                isMoving = true;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                if (player.getBounds().x + player.getBounds().width + (PLAYER_MOVEMENT_SPEED * deltaTime) < background.getWidth()) {
                    playerPosition.x = playerPosition.x + (PLAYER_MOVEMENT_SPEED * deltaTime);
                }
                player.setFlipX(false);
                isMoving = true;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                if (player.getBounds().y + player.getBounds().height + (PLAYER_MOVEMENT_SPEED * deltaTime) < background.getHeight()) {
                    playerPosition.y = playerPosition.y + (PLAYER_MOVEMENT_SPEED * deltaTime);
                }
                isMoving = true;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                if (player.getBounds().y + (-PLAYER_MOVEMENT_SPEED * deltaTime) > 0) {
                    playerPosition.y = playerPosition.y + (-PLAYER_MOVEMENT_SPEED * deltaTime);
                }
                isMoving = true;
            }

            if (isMoving) {
                player.setState(State.MOVING);
            } else {
                player.setState(State.IDLE);
            }
        }
    }
}
