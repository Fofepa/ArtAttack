package com.artattack;

import javax.swing.SwingUtilities;

import com.artattack.view.MainGUIFacade;

public class GameLauncher {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create the main GUI facade
            MainGUIFacade mainFacade = new MainGUIFacade();
            
            // Show menu first
            mainFacade.show();
            
            
        });
    }
    
}