package com.artattack.saving;

import java.io.IOException;

import com.artattack.mapelements.skilltree.SkillTree;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

public final class SkillTreeAdapter extends TypeAdapter<SkillTree> {

    private final TypeAdapter<SkillTreeData> delegate;

    public SkillTreeAdapter(Gson gson) {
        this.delegate = gson.getAdapter(SkillTreeData.class);
    }

    @Override
    public void write(JsonWriter out, SkillTree value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        SkillTreeData data = SkillTreeMapper.toData(value);
        delegate.write(out, data);
    }

    @Override
    public SkillTree read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        SkillTreeData data = delegate.read(in);
        return SkillTreeMapper.fromData(data);
    }
}


