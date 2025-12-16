package com.artattack;

import java.util.List;

public class InteractableElement extends MapElement {

    private List<Interaction> interactions;
    private int maxInteractions;
    private int lastInteraction;
    private int currInteraction;

    public InteractableElement(int ID, char mapSymbol, String name, Coordinates coordinates, List<Interaction> interactions){
        super(ID,mapSymbol,name,coordinates);
        this.maxInteractions = interactions.size();
        this.lastInteraction = this.maxInteractions - 1;
        if(interactions.get(this.lastInteraction).getClass() != Talk.class)
            throw new IllegalArgumentException();
        this.interactions = interactions;
        this.currInteraction = 0;
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

    public int getLastInteraction(){
        return this.lastInteraction;
    }

    public void interact(Player player){
        if(this.currInteraction < this.maxInteractions){
            this.interactions.get(this.currInteraction).doInteraction(player);
            this.currInteraction++;
        }
        else this.interactions.get(this.lastInteraction).doInteraction(player);
    }
    
}
