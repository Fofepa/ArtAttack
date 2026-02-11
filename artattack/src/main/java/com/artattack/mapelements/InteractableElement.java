package com.artattack.mapelements;

import java.util.List;

import com.artattack.interactions.Interaction;
import com.artattack.level.Coordinates;
import com.artattack.view.GameContext;

public class InteractableElement extends MapElement {

    private List<Interaction> interactions;
    private int maxInteractions;
    private int lastInteraction;
    private int currInteraction;

    public InteractableElement(int ID, char mapSymbol, String name, Coordinates coordinates, 
                               List<Interaction> interactions, String spritePath){
        super(ID, mapSymbol, name, coordinates, spritePath);
        this.interactions = interactions;
        this.maxInteractions = interactions.size();
        this.lastInteraction = this.maxInteractions - 1;
        this.currInteraction = 0;
    }

    public void interact(GameContext gameContext, Player player, String spritePath){
        // Load sprite
        if (gameContext.getUiManager() != null) {
            gameContext.getUiManager().loadSprite(this.getSpritePath());
        }

        // CREATE the interaction from the factory
        Interaction interaction;
        if(this.currInteraction < this.maxInteractions){
            interaction = this.interactions.get(this.currInteraction);
            this.currInteraction++;
        } else {
            interaction = this.interactions.get(this.lastInteraction);
        }
        
        // INJECT the MainFrame before executing
        if (interaction != null) {
            //interaction.setMainFrame(this.mainFrame);
            interaction.doInteraction(gameContext, player, spritePath);
        }
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


    /*public void setSpritePanel(SpritePanel spritePanel) {
        this.sp = spritePanel;
    }
    
    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        
        // IMPORTANT: Also inject MainFrame into all interactions
        if (this.interactions != null) {
            for (Interaction interaction : this.interactions) {
                if (interaction != null) {
                    interaction.setMainFrame(mainFrame);
                }
            }
        }
    }*/
}
