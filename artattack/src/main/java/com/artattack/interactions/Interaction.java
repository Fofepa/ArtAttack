package com.artattack.interactions;



import com.artattack.mapelements.Player;
import com.artattack.view.GameContext;
import com.artattack.view.MainFrame;

public abstract class Interaction {
    //private MainFrame mainFrame;  // CHANGED from InteractionPanel

    private InteractionType type;

    public Interaction(InteractionType type){
        this.type = type;
    }

    public abstract void doInteraction(GameContext gameContext, Player player);

    public InteractionType getType() {
        return type;
    }
    

    /*public MainFrame getMainFrame(){  // CHANGED from getInteractionPanel
        return this.mainFrame;
    }
    
    public void setMainFrame(MainFrame mainFrame) {  // CHANGED from setInteractionPanel
        this.mainFrame = mainFrame;
    }*/

    

    
}