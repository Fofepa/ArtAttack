package com.artattack.interactions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

public class InteractionDeserializer implements JsonDeserializer<Interaction>{

    @Override
    public Interaction deserialize(
            JsonElement json,
            Type typeOfT,
            JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject obj = json.getAsJsonObject();
        InteractionType type = InteractionType.valueOf(obj.get("type").getAsString());

        switch (type) {
            case ASK:
                return context.deserialize(obj, Ask.class);
            case GIVE:
                return context.deserialize(obj, Give.class);
            case SWITCH_MAP:
                return context.deserialize(obj, SwitchMap.class);
            case TALK:
                return context.deserialize(obj, Talk.class);
            default:
                throw new JsonParseException("Unknown Interaction type: " + type);
        }
    }
}

