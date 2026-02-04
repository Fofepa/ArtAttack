package com.artattack.view;

import com.artattack.level.MapManager;
import com.artattack.saving.SaveManager;

public class GameContext {
    private final UIManager uiManager;
    private MapManager mapManager;
    private SaveManager saveManager;

    public GameContext(UIManager uiManager, MapManager mapManager){
        this.uiManager = uiManager;
        this.mapManager = mapManager;
        this.saveManager = new SaveManager();
    }

    public GameContext(UIManager uiManager, MapManager mapManager, SaveManager saveManager){
        this.uiManager = uiManager;
        this.mapManager = mapManager;
        this.saveManager = saveManager;
    }

    public UIManager getUiManager() {
        return uiManager;
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public SaveManager getSaveManager() {
        return saveManager;
    }

    
}