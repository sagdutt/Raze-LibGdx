package com.mygdx.game.repository;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.character.Character;
import com.mygdx.game.constant.AppConstants;
import com.mygdx.game.constant.CharacterConstants;
import com.mygdx.game.event.Event;
import com.mygdx.game.event.EventBus;
import com.mygdx.game.event.EventHandler;
import com.mygdx.game.event.events.TakeDamageEvent;
import com.mygdx.game.factory.CharacterConfigFactory;
import com.mygdx.game.factory.TextureConfigFactory;
import lombok.Getter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.List;

@Singleton
public class LocalPlayerRepository implements EventHandler {

    private static final float NEW_PLAYER_X = 1100;

    private static final float NEW_PLAYER_Y = 780;

    private final TextureConfigFactory textureConfigFactory;

    private final CharacterConfigFactory characterConfigFactory;

    @Getter
    private Character player;

    @Inject
    public LocalPlayerRepository(final TextureConfigFactory textureConfigFactory,
                                      final CharacterConfigFactory characterConfigFactory,
                                      final EventBus eventBus) {
        this.textureConfigFactory = textureConfigFactory;
        this.characterConfigFactory = characterConfigFactory;
        eventBus.subscribe(this);
    }

    public Character initializePlayer(final CharacterConstants.CharacterType characterType, final String characterName) {
        player = new Character(textureConfigFactory.getTextureConfigForCharacter(characterType),
                new Vector2(NEW_PLAYER_X, NEW_PLAYER_Y), false, characterName,
                characterConfigFactory.getCharacterConfigForCharacter(characterType),
                characterType);
        return player;
    }

    @Override
    public List<Class<?>> getSubscribedEventClasses() {
        return Collections.singletonList(TakeDamageEvent.class);
    }

    @Override
    public void handleEvent(Event<?> event) {
        if (TakeDamageEvent.class == event.getClass()) {
            TakeDamageEvent.TakeDamageEventPayload payload = ((TakeDamageEvent)event).getPayload();
            if (payload.isLocalPlayer()) {
                Gdx.app.log(AppConstants.APP_LOG_TAG, "Taking damage: " + payload.getDamage());
                player.applyDamage(payload.getDamage());
            }
        }
    }
}
