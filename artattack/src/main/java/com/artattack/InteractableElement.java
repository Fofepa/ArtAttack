package com.artattack;

import java.util.List;

public class InteractableElement extends MapElement {

    private int ID;
    private char mapSymbol;
    private String name;
    private Coordinates coordinates;
    private List<Interaction> interactions;
    private int maxInteractions;
    private int currInteraction;

    public InteractableElement(int ID, char mapSymbol, String name, Coordinates coordinates, List<Interaction> interactions){
        super(ID,mapSymbol,name,coordinates);
        this.interactions = interactions;
        this.maxInteractions = interactions.size();
        this.currInteraction = 0;
    }
    
    @Override
    public int getID() {
        return this.ID;
    }

    @Override
    public char getMapSymbol() {
        return this.mapSymbol;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Coordinates getCoordinates() {
        return this.coordinates;
    }

    public List<Interaction> getInteractions(){
        return this.interactions;
    }

    public int getMaxInteractions(){
        return this.maxInteractions;
    }

    public int getCurrInteraction(){
        return this.currInteraction;
    }

    public void interact(Player player){
        if(this.currInteraction < this.maxInteractions){
            this.interactions.get(this.currInteraction).doInteraction(player);
            this.currInteraction++;
        }
        else this.interactions.get(this.maxInteractions - 1).doInteraction(player);
    }
    
}
