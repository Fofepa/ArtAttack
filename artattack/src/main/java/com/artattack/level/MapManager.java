package com.artattack.level;

import java.util.Map;

public class MapManager {
    private Map<Integer, Maps> levels;
    private int currMap;
    
    public MapManager(Map<Integer, Maps> levels, int currMap){
        this.levels = levels;
        this.currMap = currMap;
    }

    public Map<Integer, Maps> getLevels() {
        return levels;
    }

    public int getCurrMap() {
        return currMap;
    }

    public void setCurrMap(int mapID){
        this.currMap = mapID;
    }

    
}
