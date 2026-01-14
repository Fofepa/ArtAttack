package com.artattack.interactions;

import com.artattack.mapelements.Player;
import com.artattack.view.MainFrame;

public abstract class Interaction {
    private MainFrame mainFrame;  // CHANGED from InteractionPanel

    public Interaction(MainFrame mainFrame){
        this.mainFrame = mainFrame;
    }

    public abstract void doInteraction(Player player);

    public MainFrame getMainFrame(){  // CHANGED from getInteractionPanel
        return this.mainFrame;
    }
    
    public void setMainFrame(MainFrame mainFrame) {  // CHANGED from setInteractionPanel
        this.mainFrame = mainFrame;
    }
}