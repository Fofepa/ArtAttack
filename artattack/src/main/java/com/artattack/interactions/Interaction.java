package com.artattack.interactions;



import com.artattack.mapelements.Player;
import com.artattack.view.GameContext;

public abstract class Interaction {

    private InteractionType type;

    public Interaction(InteractionType type){
        this.type = type;
    }

    public abstract void doInteraction(GameContext gameContext, Player player, String spritePath);

    public InteractionType getType() {
        return type;
    }

    /*public MainFrame getMainFrame(){  / CHANGED from getInteractionPanel
        return this.mainFrame;
    }
    
    public void setMainFrame(MainFrame mainFrame) {  / CHANGED from setInteractionPanel
        this.mainFrame = mainFrame;
    }*/

    

    
}