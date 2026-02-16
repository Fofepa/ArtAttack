package com.artattack.saving;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.artattack.interactions.Interaction;
import com.artattack.mapelements.TriggerGroup;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class TriggerGroupAdapter implements JsonSerializer<TriggerGroup>, JsonDeserializer<TriggerGroup> {
    
    // Cache per mantenere i riferimenti condivisi durante la deserializzazione
    private static ThreadLocal<Map<String, TriggerGroup>> deserializationCache = 
        ThreadLocal.withInitial(HashMap::new);
    
    @Override
    public JsonElement serialize(TriggerGroup src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        
        // Crea un ID univoco basato sull'interaction
        String groupId = src.getInteraction().getClass().getName() + "@" + System.identityHashCode(src);
        jsonObject.addProperty("groupId", groupId);
        
        jsonObject.add("interaction", context.serialize(src.getInteraction()));
        jsonObject.addProperty("consumed", isConsumed(src));
        return jsonObject;
    }

    @Override
    public TriggerGroup deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) 
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        
        // Recupera l'ID del gruppo
        String groupId = jsonObject.has("groupId") ? jsonObject.get("groupId").getAsString() : null;
        
        // Se abbiamo già deserializzato questo gruppo, riusa lo stesso oggetto
        if (groupId != null) {
            Map<String, TriggerGroup> cache = deserializationCache.get();
            if (cache.containsKey(groupId)) {
                return cache.get(groupId);
            }
        }
        
        Interaction interaction = context.deserialize(jsonObject.get("interaction"), Interaction.class);
        boolean consumed = jsonObject.has("consumed") ? jsonObject.get("consumed").getAsBoolean() : false;
        
        TriggerGroup triggerGroup = new TriggerGroup(interaction);
        triggerGroup.toggle(consumed);
        
        // Salva nella cache per riuso
        if (groupId != null) {
            deserializationCache.get().put(groupId, triggerGroup);
        }
        
        return triggerGroup;
    }
    
    // Helper method per ottenere lo stato consumed usando reflection
    private boolean isConsumed(TriggerGroup triggerGroup) {
        try {
            java.lang.reflect.Field field = TriggerGroup.class.getDeclaredField("consumed");
            field.setAccessible(true);
            return (boolean) field.get(triggerGroup);
        } catch (Exception e) {
            return false;
        }
    }
    
    // Metodo da chiamare dopo il caricamento per pulire la cache
    public static void clearCache() {
        deserializationCache.get().clear();
    }
}