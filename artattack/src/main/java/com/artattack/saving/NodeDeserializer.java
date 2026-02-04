package com.artattack.saving;

import java.lang.reflect.Type;

import com.artattack.mapelements.skilltree.APNODE;
import com.artattack.mapelements.skilltree.HPNODE;
import com.artattack.mapelements.skilltree.MANODE;
import com.artattack.mapelements.skilltree.MAXMVNODE;
import com.artattack.mapelements.skilltree.MAXWPNODE;
import com.artattack.mapelements.skilltree.Node;
import com.artattack.mapelements.skilltree.NodeType;
import com.artattack.mapelements.skilltree.RootNode;
import com.artattack.mapelements.skilltree.SPNODE;
import com.artattack.mapelements.skilltree.SpecialMoveNODE;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class NodeDeserializer implements JsonDeserializer<Node> {
    @Override
    public Node deserialize(
            JsonElement json,
            Type typeOfT,
            JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject obj = json.getAsJsonObject();
        NodeType type = NodeType.valueOf(obj.get("type").getAsString());

        switch (type) {
            case AP:
                return context.deserialize(obj, APNODE.class);
            case HP:
                return context.deserialize(obj, HPNODE.class);
            case MA:
                return context.deserialize(obj, MANODE.class);
            case MAXMV:
                return context.deserialize(obj, MAXMVNODE.class);
            case MAXWP:
                return context.deserialize(obj, MAXWPNODE.class);
            case ROOT:
                return context.deserialize(obj, RootNode.class);
            case SP:
                return context.deserialize(obj, SPNODE.class);
            case SPECIALMOVE:
                return context.deserialize(obj, SpecialMoveNODE.class);
            default:
                throw new JsonParseException("Unknown Node type: " + type);
        }
    }
}
