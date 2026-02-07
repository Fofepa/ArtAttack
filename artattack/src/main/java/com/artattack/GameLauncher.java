package com.artattack;

import javax.swing.SwingUtilities;

import com.artattack.view.MainGUIFacade;

/**
 * Main application entry point demonstrating the Facade pattern integration
 */
public class GameLauncher {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create the main GUI facade
            MainGUIFacade mainFacade = new MainGUIFacade();
            
            // Show menu first
            mainFacade.show();
            
            // Example: Start game with test data
            // Uncomment below to skip menu and go straight to game
            // startTestGame(mainFacade);
        });
    }
    
}
