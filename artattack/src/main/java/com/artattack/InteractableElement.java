import java.util.*;
package com.artattack;

public class InteractableElement extends MapElement {
    //Attributes
    private int ID;
    private char mapSymbol;
    private String name;
    private Coordinates coordinates;
    private List<Interaction> interactions;

    //Contructor
    public InteractableElement(){
        
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
    public List<Interaction> getInteractions(){
        return this.interactions;
    }

    public void interact(){

    }
}
