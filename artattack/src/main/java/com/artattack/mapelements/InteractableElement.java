package com.artattack.mapelements;

import java.util.List;

import com.artattack.interactions.Interaction;
import com.artattack.interactions.InteractionFactory;
import com.artattack.level.Coordinates;
import com.artattack.view.MainFrame;
import com.artattack.view.SpritePanel;

public class InteractableElement extends MapElement {

    private List<InteractionFactory> interactionFactories;
    private int maxInteractions;
    private int lastInteraction;
    private int currInteraction;
    private String spritePath;
    private SpritePanel sp;
    private MainFrame mainFrame;

    public InteractableElement(int ID, char mapSymbol, String name, Coordinates coordinates, 
                               List<InteractionFactory> interactionFactories, String spritePath, 
                               SpritePanel spritePanel, MainFrame mainFrame){
        super(ID, mapSymbol, name, coordinates);
        this.interactionFactories = interactionFactories;
        this.maxInteractions = interactionFactories.size();
        this.lastInteraction = this.maxInteractions - 1;
        this.currInteraction = 0;
        this.spritePath = spritePath;
        this.sp = spritePanel;
        this.mainFrame = mainFrame;
    }

    public void interact(Player player){
        // Load sprite
        if (sp != null) {
            sp.loadImage(this.spritePath);
        }

        // CREATE the interaction from the factory
        Interaction interaction;
        if(this.currInteraction < this.maxInteractions){
            interaction = this.interactionFactories.get(this.currInteraction).createInteraction();
            this.currInteraction++;
        } else {
            interaction = this.interactionFactories.get(this.lastInteraction).createInteraction();
        }
        
        // INJECT the MainFrame before executing
        if (interaction != null && this.mainFrame != null) {
            interaction.setMainFrame(this.mainFrame);
            interaction.doInteraction(player);
        }
    }

    public String getSpritePath() {
        return spritePath;
    }

    public void setSpritePanel(SpritePanel spritePanel) {
        this.sp = spritePanel;
    }
    
    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }
}