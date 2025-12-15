package com.artattack;

public abstract class MapElement {

    //Attributes
    private final int ID;
    private final char mapSymbol;
    private final String name;
    private Coordinates coordinates;

    public MapElement(int ID, char mapSymbol, String name, Coordinates coordinates){
        this.ID = ID;
        this.mapSymbol = mapSymbol;
        this.name = name;
        this.coordinates = coordinates;
    }
    
    
    //getters
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
}