package com.mygdx.game.dto;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.constant.AppConstants;
import com.mygdx.game.constant.CharacterConstants;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import org.json.JSONArray;
import org.json.JSONObject;

@Value
@Builder
@Getter(AccessLevel.PRIVATE)
public class CharacterReadyDto implements Dto {

    CharacterConstants.CharacterType character;

    String name;

    @Override
    public JSONObject toJsonObject() {
        try {
            JSONObject payload = new JSONObject();
            payload.put("character", character.name());
            payload.put("name", name);
            return payload;
        } catch (Exception e) {
            Gdx.app.error(AppConstants.APP_LOG_TAG, "Error while converting to json", e);
            return null;
        }
    }

    @Override
    public JSONArray toJsonArray() {
        throw new UnsupportedOperationException("Cannot convert this object to JsonArray");
    }
}
