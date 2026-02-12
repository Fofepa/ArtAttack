package com.artattack.mapelements;

import java.util.Objects;

import com.artattack.level.Coordinates;

public abstract class MapElement {
    private final int ID;
    private char mapSymbol;
    private final String name;
    private Coordinates coordinates;
    private String spritePath;  
    
    
    public MapElement(int ID, char mapSymbol, String name, Coordinates coordinates){
        this(ID, mapSymbol, name, coordinates, null);
    }
    
    
    public MapElement(int ID, char mapSymbol, String name, Coordinates coordinates, String spritePath){
        this.ID = ID;
        this.mapSymbol = mapSymbol;
        this.name = name;
        this.coordinates = coordinates;
        this.spritePath = spritePath;
    }
    
    
    public int getID() {
        return this.ID;
    }
    
    public char getMapSymbol() {
        return this.mapSymbol;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Coordinates getCoordinates() {
        return this.coordinates;
    }
    
    public void setCoordinates(Coordinates coordinates){
        this.coordinates = coordinates;
    }
    
    public String getSpritePath() {
        return this.spritePath;
    }
    
    public void setSpritePath(String spritePath) {
        this.spritePath = spritePath;
    }

    public void setMapSymbol(char c){
        this.mapSymbol = c;
    }
    
    public boolean hasSprite() {
        return this.spritePath != null && !this.spritePath.isEmpty();
    }
    
    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if (obj == null) return false;
        if(this.getClass() != obj.getClass()) return false;
        MapElement other = (MapElement) obj;
        return this.getCoordinates().equals(other.getCoordinates()) && this.getID() == other.getID();
    }
    
    @Override
    public int hashCode(){
        return Objects.hash(ID);
    }    
}