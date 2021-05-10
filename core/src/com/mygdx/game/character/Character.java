package com.mygdx.game.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.constant.State;
import com.mygdx.game.model.CharacterConfig;
import com.mygdx.game.model.TextureConfig;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class Character implements Disposable {

    private final TextureAtlas textureAtlas;

    private final Map<State, Animation<TextureRegion>> stateRegionMap;

    private final CharacterConfig characterConfig;

    @Getter
    private final String name;

    private final BitmapFont bitmapFont;

    private final Vector2 textureSize;

    @Getter
    private final Rectangle bounds;

    @Getter
    private final Rectangle attackArea;

    @Getter
    @Setter
    private Vector2 position;

    @Getter
    @Setter
    private State state;

    private State previousState;

    @Getter
    @Setter
    private boolean flipX;

    private float elapsedTime = 0.0f;

    @Getter
    @Setter
    private boolean canMove;

    public Character(final TextureConfig textureConfig,
                     final Vector2 position,
                     final boolean flipX,
                     final String name,
                     final CharacterConfig characterConfig) {
        this.textureAtlas = new TextureAtlas(textureConfig.getTexturePath());
        this.stateRegionMap = new HashMap<>();
        textureConfig.getAnimConfigMap().forEach((state, animConfig) -> stateRegionMap.put(state,
                new Animation<>(animConfig.getFrameRate(), textureAtlas.findRegions(animConfig.getName()))));
        this.bounds = new Rectangle(position.x + characterConfig.getBoundsXOffset(),
                position.y + characterConfig.getBoundsYOffset(),
                characterConfig.getBoundsWidth(),
                characterConfig.getBoundsHeight());
        this.attackArea = new Rectangle(position.x + characterConfig.getAttackAreaXOffset(),
                position.y + characterConfig.getAttackAreaYOffset(),
                characterConfig.getAttackAreaWidth(),
                characterConfig.getAttackAreaHeight());
        this.textureSize = new Vector2(textureConfig.getTextureWidth(), textureConfig.getTextureHeight());
        this.position = position;
        this.state = State.IDLE;
        this.previousState = State.IDLE;
        this.flipX = flipX;
        this.canMove = true;
        this.name = name;
        this.bitmapFont = new BitmapFont(Gdx.files.internal("Skins/glassy/font-export.fnt"));
        this.characterConfig = characterConfig;
    }

    public void update(final float deltaTime) {
        bounds.setX(position.x + characterConfig.getBoundsXOffset());
        bounds.setY(position.y + characterConfig.getBoundsYOffset());

        elapsedTime += deltaTime;
        if (state != previousState) {
            elapsedTime = 0.0f;
            previousState = state;
        }
        if (State.ATTACKING.equals(state)) {
            attackArea.setX(flipX ?
                    position.x + characterConfig.getAttackAreaXOffset() - attackArea.width :
                    position.x + characterConfig.getAttackAreaXOffset());
            attackArea.setY(position.y + characterConfig.getAttackAreaYOffset());
            canMove = stateRegionMap.get(state).isAnimationFinished(elapsedTime);
        }
    }

    public void draw(final Batch batch) {
        batch.draw(stateRegionMap.get(state).getKeyFrame(elapsedTime, !state.equals(State.ATTACKING)),
                flipX ? position.x + textureSize.x : position.x,
                position.y,
                flipX ? -textureSize.x : textureSize.x,
                textureSize.y);
        bitmapFont.draw(batch, name, position.x + (textureSize.x / 2), position.y);
    }

    @Override
    public void dispose() {
        textureAtlas.dispose();
        bitmapFont.dispose();
    }
}
