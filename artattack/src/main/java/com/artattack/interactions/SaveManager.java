package com.artattack.interactions;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.artattack.level.MapManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SaveManager {
    
    private final Gson gson;
    private final String savePath;

    public SaveManager(){
        this.savePath = getSaveFilePath("ArtAttack").toString();
        this.gson = new GsonBuilder()
            .registerTypeAdapter(Interaction.class, new InteractionDeserializer())
            .setPrettyPrinting()
            .create();
    }

    public void save(MapManager mapManager) throws IOException{
        try(FileWriter writer = new FileWriter(savePath)){
            gson.toJson(mapManager,  writer);
        }
    }

    public MapManager load() throws IOException{
        try(FileReader reader = new FileReader(savePath)){
            return gson.fromJson(reader, MapManager.class);
        }
    }

    private Path getSaveFilePath(String gameName) {
        String os = System.getProperty("os.name").toLowerCase();
        String home = System.getProperty("user.home");

        Path folder;

        if (os.contains("win")) {
            String appData = System.getenv("LOCALAPPDATA");
            folder = Paths.get(appData, gameName);
        } else if (os.contains("mac")) {
            folder = Paths.get(home, "Library", "Application Support", gameName);
        } else {
            folder = Paths.get(home, ".local", "share", gameName);
        }

        return folder.resolve("save.json");
    }

}
