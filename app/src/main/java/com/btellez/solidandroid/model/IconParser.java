package com.btellez.solidandroid.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public interface IconParser {
    public List<Icon> fromJson(String jsonString, String dataKey);

    /**
     * Google Gson Implementation of our Icon Parser
     */
    public static class GsonIconParser implements IconParser {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();

        @Override
        public List<Icon> fromJson(String jsonString, String dataKey) {
            JsonElement json = parser.parse(jsonString);
            JsonArray iconList = json.getAsJsonObject().getAsJsonArray(dataKey);
            List<Icon> icons = new ArrayList<Icon>(iconList.size());
            for (JsonElement item : iconList) {
                icons.add(gson.fromJson(item, Icon.class));
            }
            return icons;
        }
    }
}
