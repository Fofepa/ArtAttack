package com.artattack.interactions;

import com.artattack.mapelements.Player;
import com.artattack.view.MainFrame;

public abstract class Interaction {
    private MainFrame mainFrame;

    public Interaction(MainFrame mainFrame){
        this.mainFrame = mainFrame;
    }

    public abstract void doInteraction(Player player);

    public MainFrame getMainFrame(){
        return this.mainFrame;
    }
}
