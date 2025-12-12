import java.util.*;
package com.artattack;

public class InteractableElement extends MapElement {
    //Attributes
    private int ID;
    private char mapSymbol;
    private String name;
    private Coordinates coordinates;
    private List<Interaction> interactions;
    private int nMaxInteractions;
    private int currInteraction;

    //Contructor
    public InteractableElement(int ID, char mapSymbol, String name, Coordinates coordinates, List<Interaction> interactions){
        super(ID,mapSymbol,name,coordinates);
        this.interactions = interactions;
        this.nMaxInteractions = interactions.size();
        this.currInteraction = 0;
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

    public void interact(Player player){
        if(this.currInteraction < this.nMaxInteractions){
            this.interactions.get(this.currInteraction).doAction(player);
            this.currInteraction++;
        }
        else this.interactions.get(this.nMaxInteractions - 1).doAction(player);
    }
    
}
