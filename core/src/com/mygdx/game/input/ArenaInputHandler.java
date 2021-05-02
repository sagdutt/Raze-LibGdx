package com.mygdx.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.character.Character;
import com.mygdx.game.constant.State;

public class ArenaInputHandler implements InputHandler {

    private static final int MOVEMENT_SPEED = 250;

    private final Character player;

    private final Texture background;

    public ArenaInputHandler(final Character player, final Texture background) {
        this.player = player;
        this.background = background;
    }

    @Override
    public void handleInput(float deltaTime) {
        if (Gdx.input.isTouched()) {
            player.setState(State.ATTACKING);
            player.setCanMove(false);
        }
        if (player.isCanMove()) {
            boolean isMoving = false;
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                if (player.getX() + (-MOVEMENT_SPEED * deltaTime) > 0) {
                    player.setX(player.getX() + (-MOVEMENT_SPEED * deltaTime));
                }
                player.setFlipX(true);
                isMoving = true;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                if (player.getX() + player.getTextureSize().x + (MOVEMENT_SPEED * deltaTime) < background.getWidth()) {
                    player.setX(player.getX() + (MOVEMENT_SPEED * deltaTime));
                }
                player.setFlipX(false);
                isMoving = true;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                if (player.getY() + player.getTextureSize().y + (MOVEMENT_SPEED * deltaTime) < background.getHeight()) {
                    player.setY(player.getY() + (MOVEMENT_SPEED * deltaTime));
                }
                isMoving = true;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                if (player.getY() + (-MOVEMENT_SPEED * deltaTime) > 0) {
                    player.setY(player.getY() + (-MOVEMENT_SPEED * deltaTime));
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
