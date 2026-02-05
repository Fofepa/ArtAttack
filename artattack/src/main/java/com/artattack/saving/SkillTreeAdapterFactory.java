package com.artattack.saving;

import com.artattack.mapelements.skilltree.SkillTree;

import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SkillTreeAdapterFactory implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (!SkillTree.class.isAssignableFrom(type.getRawType())) {
            return null;
        }
        return (TypeAdapter<T>) new SkillTreeAdapter(gson);
    }
}

