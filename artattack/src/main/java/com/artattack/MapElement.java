package com.artattack;

public abstract class MapElement {

    //Attributes
    private int ID;
    private char mapSymbol;
    private String name;
    private Coordinates coordinates;
    
    
    //getters
    public int getID() {
        return ID;
    }
    public char getMapSymbol() {
        return mapSymbol;
    }
    public String getName() {
        return name;
    }
    public Coordinates getCoordinates() {
        return coordinates;
    }

    
}
