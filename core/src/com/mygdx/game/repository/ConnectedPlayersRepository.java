package com.mygdx.game.repository;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.character.Character;
import com.mygdx.game.constant.State;
import com.mygdx.game.event.Event;
import com.mygdx.game.event.EventBus;
import com.mygdx.game.event.EventHandler;
import com.mygdx.game.event.events.*;
import com.mygdx.game.factory.CharacterConfigFactory;
import com.mygdx.game.factory.TextureConfigFactory;
import lombok.Getter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class ConnectedPlayersRepository implements EventHandler {

    private static final float NEW_PLAYER_X = 1100;

    private static final float NEW_PLAYER_Y = 780;

    private final TextureConfigFactory textureConfigFactory;

    private final CharacterConfigFactory characterConfigFactory;

    @Getter
    private final Map<String, Character> connectedPlayers;

    @Inject
    public ConnectedPlayersRepository(final TextureConfigFactory textureConfigFactory,
                                      final CharacterConfigFactory characterConfigFactory,
                                      final EventBus eventBus) {
        this.textureConfigFactory = textureConfigFactory;
        this.characterConfigFactory = characterConfigFactory;
        this.connectedPlayers = new HashMap<>();
        eventBus.subscribe(this);
    }

    @Override
    public List<Class<?>> getSubscribedEventClasses() {
        return Arrays.asList(NewPlayerConnectedEvent.class,
                PlayerDisconnectedEvent.class,
                GetPlayersEvent.class,
                PlayerMovedEvent.class,
                TakeDamageEvent.class);
    }

    @Override
    public void handleEvent(final Event<?> event) {
        if (NewPlayerConnectedEvent.class == event.getClass()) {
            handleNewPlayerConnected((NewPlayerConnectedEvent) event);
        } else if (PlayerDisconnectedEvent.class == event.getClass()) {
            handlePlayerDisconnected((PlayerDisconnectedEvent) event);
        } else if (GetPlayersEvent.class == event.getClass()) {
            handleGetPlayers((GetPlayersEvent) event);
        } else if (PlayerMovedEvent.class == event.getClass()) {
            handlePlayerMoved((PlayerMovedEvent) event);
        } else if (TakeDamageEvent.class == event.getClass()) {
            handleTakeDamage((TakeDamageEvent) event);
        }
    }

    private void handleTakeDamage(final TakeDamageEvent event) {
        TakeDamageEvent.TakeDamageEventPayload payload = event.getPayload();
        if (!payload.isLocalPlayer()) {
            if (connectedPlayers.containsKey(payload.getId())) {
                connectedPlayers.get(payload.getId()).applyDamage(payload.getDamage());
            }
        }
    }

    private void handlePlayerMoved(final PlayerMovedEvent event) {
        PlayerMovedEvent.PlayerMovedPayload playerMovedPayload = event.getPayload();
        if (connectedPlayers.containsKey(playerMovedPayload.getId())) {
            Character otherPlayer = connectedPlayers.get(playerMovedPayload.getId());
            otherPlayer.getPosition().x = playerMovedPayload.getPosition().x;
            otherPlayer.getPosition().y = playerMovedPayload.getPosition().y;
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
                                new Character(textureConfigFactory.getTextureConfigForCharacter(getPlayerPayload.getCharacterType()),
                                        getPlayerPayload.getPosition(), getPlayerPayload.isFlipX(), getPlayerPayload.getName(),
                                        characterConfigFactory.getCharacterConfigForCharacter(getPlayerPayload.getCharacterType())))));
    }

    private void handlePlayerDisconnected(final PlayerDisconnectedEvent event) {
        if (connectedPlayers.containsKey(event.getPayload())) {
            Gdx.app.postRunnable(() -> {
                connectedPlayers.get(event.getPayload()).dispose();
                connectedPlayers.remove(event.getPayload());
            });
        }
    }

    private void handleNewPlayerConnected(final NewPlayerConnectedEvent event) {
        Gdx.app.postRunnable(() ->
                connectedPlayers.put(event.getPayload().getId(),
                        new Character(textureConfigFactory.getTextureConfigForCharacter(event.getPayload().getCharacterType()),
                                new Vector2(NEW_PLAYER_X, NEW_PLAYER_Y), false, event.getPayload().getName(),
                                characterConfigFactory.getCharacterConfigForCharacter(event.getPayload().getCharacterType()))));
    }
}
