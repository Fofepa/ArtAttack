package com.artattack.level;

import java.util.Map;

public class MapManager {
    private Map<Integer, Maps> levels;
    private int currMap;
    private int turnIndex = 0;
    
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

    public int getTurnIndex() {
        return turnIndex;
    }

    public void setTurnIndex(int turnIndex){
        this.turnIndex = turnIndex;
    }

    
}
