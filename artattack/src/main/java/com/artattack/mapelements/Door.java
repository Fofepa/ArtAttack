package com.artattack.mapelements;

import com.artattack.level.Coordinates;
import com.artattack.level.MapBuilder;
import com.artattack.items.Key;
import com.artattack.level.Maps;
import com.artattack.view.GameContext;

public class Door extends MapElement implements Interactable{
    private boolean unloked;
    private Key key;
    private Maps nextMap;
    private MapBuilder mapBuilder;
    private String spritePath;

    public Door(int ID, char mapSymbol, String name, Coordinates coordinates){
        super(ID, mapSymbol, name, coordinates);
    }

    public Door(int ID, char mapSymbol, String name, Coordinates coordinates, Key key, Maps nextMap, String spritePath){
        this(ID, mapSymbol, name, coordinates);
        this.unloked = false;
        this.key = key;
        this.nextMap = nextMap;
        this.mapBuilder = null;
        this.spritePath = spritePath;
    }

    public Door(int ID, char mapSymbol, String name, Coordinates coordinates, Key key, MapBuilder mapBuilder, String spritePath){
        this(ID, mapSymbol, name, coordinates);
        this.unloked = false;
        this.key = key;
        this.nextMap = null;
        this.mapBuilder = mapBuilder;
        this.spritePath = spritePath;
    }

    public Door(int ID, char mapSymbol, String name, Coordinates coordinates, MapBuilder mapBuilder, String spritePath){
        this(ID, mapSymbol, name, coordinates);
        this.unloked = true;
        this.key = null;
        this.nextMap = null;
        this.mapBuilder = mapBuilder;
        this.spritePath = spritePath;
    }

    public Door(int ID, char mapSymbol, String name, Coordinates coordinates, Maps nextMap, String spritePath){
        this(ID, mapSymbol, name, coordinates);
        this.unloked = true;
        this.key = null;
        this.nextMap = nextMap;
        this.mapBuilder = null;
        this.spritePath = spritePath;
    }

    public boolean isUnloked() {
        return unloked;
    }

    public Key getKey() {
        return key;
    }

    public Maps getNextMap() {
        return nextMap;
    }

    public MapBuilder getMapBuilder() {
        return mapBuilder;
    }

    public String getSpritePath(){
        return this.spritePath;
    }

    @Override
    public void interact(GameContext gameContext, Player player) {
        // TODO Auto-generated method stub
        
    }

    
    
}
