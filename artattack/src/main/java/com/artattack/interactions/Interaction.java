package com.artattack.interactions;

import com.google.gson.Gson;


import com.artattack.mapelements.Player;
import com.artattack.view.GameContext;
import com.artattack.view.MainFrame;

public abstract class Interaction {
    //private MainFrame mainFrame;  // CHANGED from InteractionPanel

    InteractionType type;

    public Interaction(/*MainFrame mainFrame*/){
        //this.mainFrame = mainFrame;
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