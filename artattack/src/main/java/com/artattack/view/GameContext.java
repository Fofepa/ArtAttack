package com.artattack.view;

import com.artattack.level.MapManager;

public class GameContext {
    private final UIManager uiManager;
    private MapManager mapManager;

    public GameContext(UIManager uiManager, MapManager mapManager){
        this.uiManager = uiManager;
        this.mapManager = mapManager;
    }

    public UIManager getUiManager() {
        return uiManager;
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    

    
}
