package com.artattack.saving;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.artattack.level.MapManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.artattack.interactions.Interaction;

public class SaveManager {
    
    private final Gson gson;
    private final Path savePath;

    public SaveManager(){
        this.savePath = getSaveFilePath("ArtAttack");
        this.gson = new GsonBuilder()
            .registerTypeAdapter(Interaction.class, new InteractionDeserializer())
            .registerTypeAdapterFactory(new SkillTreeAdapterFactory())
            .setPrettyPrinting()
            .create();
    }

    public void save(MapManager mapManager) throws IOException{
        Files.createDirectories(this.savePath.getParent());

        System.out.println("SAVE PATH: " + this.savePath.toAbsolutePath());

        mapManager.setTurnIndex(
            mapManager.getLevels().get(mapManager.getCurrMap()).getConcreteTurnHandler().getIndex()
        );


        try(FileWriter writer = new FileWriter(savePath.toFile())){
            gson.toJson(mapManager,  writer);
        }
    }

    public MapManager load() throws IOException{
        try(FileReader reader = new FileReader(savePath.toFile())){
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
