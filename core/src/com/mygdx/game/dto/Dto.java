package com.mygdx.game.dto;

import org.json.JSONArray;
import org.json.JSONObject;

public interface Dto {

    JSONObject toJsonObject();

    JSONArray toJsonArray();
}
