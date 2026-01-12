package com.artattack.mapelements;

import java.util.List;

import com.artattack.interactions.Interaction;
import com.artattack.interactions.Talk;
import com.artattack.level.Coordinates;
import com.artattack.view.InteractionPanel;
import com.artattack.view.SpritePanel;

public class InteractableElement extends MapElement {

    private List<Interaction> interactions;
    private int maxInteractions;
    private int lastInteraction;
    private int currInteraction;
    private String spritePath;
    private SpritePanel sp;
    private InteractionPanel interactionPanel;


    public InteractableElement(int ID, char mapSymbol, String name, Coordinates coordinates, List<Interaction> interactions, String spritePath, SpritePanel spritePanel, InteractionPanel interactionPanel){
        super(ID,mapSymbol,name,coordinates);
        this.maxInteractions = interactions.size();
        this.lastInteraction = this.maxInteractions - 1;
        if(interactions.get(this.lastInteraction).getClass() != Talk.class)
            throw new IllegalArgumentException();
        this.interactions = interactions;
        this.currInteraction = 0;
        this.spritePath = spritePath;
        this.sp = spritePanel;
        this.interactionPanel = interactionPanel;
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
        sp.loadImage(this.spritePath);
        interactionPanel.activateAndFocus();

        if(this.currInteraction < this.maxInteractions){
            this.interactions.get(this.currInteraction).doInteraction(player);
            this.currInteraction++;
        }
        else this.interactions.get(this.lastInteraction).doInteraction(player);
    }

    public String getSpritePath() {
        return spritePath;
    }


    
}
