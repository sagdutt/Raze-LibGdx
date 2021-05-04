package com.mygdx.game.event.events;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.constant.CharacterConstants;
import com.mygdx.game.event.Event;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.util.List;

/**
 * Event containing info of all players existing in the world.
 */
@Value
@Builder
public class GetPlayersEvent implements Event<List<GetPlayersEvent.GetPlayerPayload>> {

    @Getter(AccessLevel.PRIVATE)
    List<GetPlayerPayload> getPlayerPayloadList;

    @Value
    @Builder
    public static class GetPlayerPayload {

        String id;

        Vector2 position;

        boolean flipX;

        CharacterConstants.CharacterType characterType;
    }

    @Override
    public List<GetPlayerPayload> getPayload() {
        return getPlayerPayloadList;
    }
}
