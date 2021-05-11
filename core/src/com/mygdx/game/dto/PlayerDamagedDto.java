package com.mygdx.game.dto;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.constant.AppConstants;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import org.json.JSONArray;
import org.json.JSONObject;

@Value
@Builder
@Getter(AccessLevel.PRIVATE)
public class PlayerDamagedDto implements Dto {

    String id;

    int damage;

    @Override
    public JSONObject toJsonObject() {
        try {
            JSONObject data = new JSONObject();
            data.put("id", id);
            data.put("damage", damage);
            return data;
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
