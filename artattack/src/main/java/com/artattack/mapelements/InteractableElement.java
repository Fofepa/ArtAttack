package com.artattack.mapelements;

import java.util.List;

import com.artattack.interactions.Interaction;
import com.artattack.interactions.InteractionFactory;
import com.artattack.level.Coordinates;
import com.artattack.view.GameContext;
import com.artattack.view.MainFrame;
import com.artattack.view.SpritePanel;

public class InteractableElement extends MapElement {

    private List<Interaction> interactions;
    private int maxInteractions;
    private int lastInteraction;
    private int currInteraction;
    private String spritePath;
    //private SpritePanel sp;
    //private MainFrame mainFrame;

    public InteractableElement(int ID, char mapSymbol, String name, Coordinates coordinates, 
                               List<Interaction> interactions, String spritePath/*/, 
                               SpritePanel spritePanel, MainFrame mainFrame*/){
        super(ID, mapSymbol, name, coordinates);
        this.interactions = interactions;
        this.maxInteractions = interactions.size();
        this.lastInteraction = this.maxInteractions - 1;
        this.currInteraction = 0;
        this.spritePath = spritePath;
        //this.sp = spritePanel;
        //this.mainFrame = mainFrame;
    }

    public void interact(GameContext gameContext, Player player){
        // Load sprite
        if (gameContext.getUiManager() != null) {
            gameContext.getUiManager().loadSprite(spritePath);
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
            interaction.doInteraction(gameContext, player);
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

    public String getSpritePath() {
        return spritePath;
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
