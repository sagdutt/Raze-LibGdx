package com.mygdx.game.character;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.constant.State;
import com.mygdx.game.model.TextureConfig;

import java.util.HashMap;
import java.util.Map;

public class Character implements Disposable {

    private final TextureAtlas textureAtlas;

    private final Map<State, Animation<TextureRegion>> stateRegionMap;

    private final Vector2 textureSize;

    private final Vector2 position;

    private final Vector2 previousPosition;

    private State state;

    private State previousState;

    private boolean flipX;

    private float elapsedTime = 0.0f;

    private boolean canMove;

    public Character(final TextureConfig textureConfig) {
        this.textureAtlas = new TextureAtlas(textureConfig.getTexturePath());
        this.stateRegionMap = new HashMap<>();
        textureConfig.getAnimConfigMap().forEach((state, animConfig) -> stateRegionMap.put(state,
                new Animation<>(animConfig.getFrameRate(), textureAtlas.findRegions(animConfig.getName()))));
        this.textureSize = new Vector2(textureConfig.getTextureWidth(), textureConfig.getTextureHeight());
        this.position = new Vector2(0,0);
        this.previousPosition = new Vector2(getX(), getY());
        this.state = State.IDLE;
        this.previousState = State.IDLE;
        this.canMove = true;
    }

    public Character(final TextureConfig textureConfig, final Vector2 position, final boolean flipX) {
        this.textureAtlas = new TextureAtlas(textureConfig.getTexturePath());
        this.stateRegionMap = new HashMap<>();
        textureConfig.getAnimConfigMap().forEach((state, animConfig) -> stateRegionMap.put(state,
                new Animation<>(animConfig.getFrameRate(), textureAtlas.findRegions(animConfig.getName()))));
        this.textureSize = new Vector2(textureConfig.getTextureWidth(), textureConfig.getTextureHeight());
        this.position = position;
        this.previousPosition = position;
        this.state = State.IDLE;
        this.previousState = State.IDLE;
        this.flipX = flipX;
        this.canMove = true;
    }

    public boolean isDirty() {
        boolean isDirty = false;
        if (previousPosition.x != getX() || previousPosition.y != getY()) {
            previousPosition.x = getX();
            previousPosition.y = getY();
            isDirty = true;
        }
        if (state != previousState) {
            elapsedTime = 0.0f;
            previousState = state;
            isDirty = true;
        }
        return isDirty;
    }

    public void update(final float deltaTime) {
        elapsedTime += deltaTime;
        if (State.ATTACKING.equals(state)) {
            canMove = stateRegionMap.get(state).isAnimationFinished(elapsedTime);
        }
    }

    public void draw(final Batch batch) {
        batch.draw(stateRegionMap.get(state).getKeyFrame(elapsedTime, !state.equals(State.ATTACKING)),
                flipX ? position.x + textureSize.x : position.x,
                position.y,
                flipX ? -textureSize.x : textureSize.x,
                textureSize.y);
    }

    @Override
    public void dispose() {
        textureAtlas.dispose();
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public State getState() {
        return state;
    }

    public boolean isFlipX() {
        return flipX;
    }

    public boolean isCanMove() {
        return canMove;
    }

    public Vector2 getTextureSize() {
        return textureSize;
    }

    public void setX(final float x) {
        position.x = x;
    }

    public void setY(final float y) {
        position.y = y;
    }

    public void setState(final State state) {
        this.state = state;
    }

    public void setFlipX(final boolean flipX) {
        this.flipX = flipX;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }
}
