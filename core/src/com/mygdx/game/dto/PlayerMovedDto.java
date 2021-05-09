package com.mygdx.game.dto;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.constant.AppConstants;
import com.mygdx.game.constant.State;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import org.json.JSONArray;
import org.json.JSONObject;

@Value
@Builder
@Getter(AccessLevel.PRIVATE)
public class PlayerMovedDto implements Dto {

    float x;

    float y;

    boolean flipX;

    State state;

    @Override
    public JSONObject toJsonObject() {
        try {
            JSONObject playerData = new JSONObject();
            playerData.put("x", x);
            playerData.put("y", y);
            playerData.put("flipX", flipX);
            playerData.put("state", state.name());
            return playerData;
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
