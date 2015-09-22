package com.btellez.solidandroid.model;

import com.btellez.solidandroid.utility.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public interface IconParser {
    List<Icon> fromJson(String jsonString, String dataKey);

    /**
     * Google Gson Implementation of our Icon Parser
     */
    class GsonIconParser implements IconParser {
        Gson gson = new GsonBuilder().registerTypeAdapter(Icon.class, new IconDeserializer()).create();
        JsonParser parser = new JsonParser();

        @Override
        public List<Icon> fromJson(String jsonString, String dataKey) {
            JsonElement json = parser.parse(jsonString);
            JsonArray iconList;
            if (Strings.isEmpty(dataKey)) {
                iconList = json.getAsJsonArray();
            } else {
                iconList = json.getAsJsonObject().getAsJsonArray(dataKey);
            }

            List<Icon> icons = new ArrayList<>(iconList.size());
            for (JsonElement item : iconList) {
                icons.add(gson.fromJson(item, Icon.class));
            }
            return icons;
        }

        private class IconDeserializer implements JsonDeserializer<Icon> {
            Gson defaultGson = new Gson();
            @Override
            public Icon deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                JsonElement uploader = json.getAsJsonObject().get("uploader");
                if (!uploader.isJsonObject()) {
                    json.getAsJsonObject().remove("uploader");
                    json.getAsJsonObject().add("uploader", new JsonObject());
                }
                return defaultGson.fromJson(json, Icon.class);
            }
        }
    }
}
