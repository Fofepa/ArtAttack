package com.artattack;

import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.artattack.view.MainGUIFacade;
import com.artattack.level.*;
import com.artattack.*;
import com.artattack.mapelements.*;
import com.artattack.moves.Weapon;
import com.artattack.items.Item;

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
    
    /**
     * Example method showing how to start a game with the Facade
     */
    public static void startTestGame(MainGUIFacade facade) {
        // Build the map using your MapBuilder pattern
        MapBuilder builder = new TestMapBuilder();
        
        // Create players
        Player playerOne = new Player(1, '@', "Zappa", new Coordinates(0, 1), List.of(new Weapon("Hoe", "", 0)), 5,5, null, 20, 20,PlayerType.MUSICIAN, 0, 20, 1, 5, 2, null, null, null);
        Player playerTwo = new Player(0, '@', "Lynch", new Coordinates(5, 5),List.of(new Weapon("Hoe", "", 0)), 5, 5 , null, 20, 20, PlayerType.MOVIE_DIRECTOR, 0, 20, 1, 5, 2, null, null, null);
        
        // Create enemies (optional)
        AreaBuilder areaBuilder = new AreaBuilder();
        areaBuilder.addShape("8");
        Enemy enemy = new Enemy(0, 'E', "Frank", new Coordinates(10,10),EnemyType.GUARD, 20, 20, 3,
                                 null,5,5,null,areaBuilder.getResult(),null,null,0);
        
        // Create interactable elements (optional)
        List<InteractableElement> interactables = new ArrayList<>();
        
        // Build the map
        builder.setDimension(40, 140);  // Based on your screenshot dimensions
        builder.setPlayerOne(playerOne);
        builder.setPlayerTwo(playerTwo);
        builder.setEnemies(List.of(enemy));
        builder.setInteractableElements(interactables);
        builder.setDict();
        builder.setTurnQueue();
        builder.startMap();
        
        Maps map = builder.getResult();

        MapManager mapManager = new MapManager(new HashMap<Integer, Maps>(), map.getID());
        mapManager.getLevels().put(map.getID(), map);
        
        // Start the game through the facade
        facade.startNewGame(mapManager, playerOne, playerTwo);
    }
}


/**
 * =============================================================================
 * COMPREHENSIVE USAGE GUIDE
 * =============================================================================
 * 
 * ARCHITECTURE OVERVIEW:
 * 
 * MainGUIFacade (Top-level coordinator)
 *   ├── MenuFacade (Menu screen)
 *   └── GameFacade (Game screen)
 *        ├── MainFrame (Adapter for InputController)
 *        ├── LeftPanelFacade (6 left panels)
 *        │    ├── InventoryPanel
 *        │    ├── DetailsPanel
 *        │    ├── WeaponsPanel
 *        │    ├── MovesPanel
 *        │    ├── TurnOrderPanel
 *        │    └── StatsPanel
 *        └── CenterPanelFacade (Main game area)
 *             ├── MapPanel (with cyan border)
 *             └── InteractionPanel (bottom dialog)
 * 
 * 
 * KEY INTEGRATION POINTS:
 * 
 * 1. InputController Integration:
 *    - InputController receives MainFrame instance
 *    - MainFrame provides all methods InputController needs
 *    - No changes needed to your InputController!
 * 
 * 2. Strategy Pattern Integration:
 *    - MovementStrategy works with Maps and Coordinates
 *    - CombatStrategy manages weapon/move selection
 *    - InventoryStrategy handles item selection
 *    - All strategies are automatically created by MainFrame
 * 
 * 3. Turn System Integration:
 *    - InputController registers as TurnListener
 *    - updateTurn() called automatically by turn system
 *    - MainFrame updates displays when turns change
 * 
 * 4. Map Builder Integration:
 *    - Use your TestMapBuilder or any MapBuilder implementation
 *    - Pass the built Maps object to facade.startNewGame()
 *    - Everything connects automatically!
 * 
 * 
 * STEP-BY-STEP USAGE:
 * 
 * 1. Create Main Application:
 *    ```java
 *    public static void main(String[] args) {
 *        SwingUtilities.invokeLater(() -> {
 *            MainGUIFacade facade = new MainGUIFacade();
 *            facade.show();  // Shows menu
 *        });
 *    }
 *    ```
 * 
 * 2. Build Your Game:
 *    ```java
 *    MapBuilder builder = new TestMapBuilder();
 *    builder.setDimension(40, 140);
 *    builder.setPlayerOne(player1);
 *    builder.setPlayerTwo(player2);
 *    builder.setEnemies(enemyList);
 *    builder.setDict();
 *    builder.setTurnQueue();
 *    builder.startMap();
 *    Maps map = builder.getResult();
 *    ```
 * 
 * 3. Start Game:
 *    ```java
 *    facade.startNewGame(map, player1, player2);
 *    ```
 * 
 * 4. Your InputController Automatically Works:
 *    - Press M to focus map
 *    - Press F to focus moves
 *    - Press I to focus inventory
 *    - WASD or arrows to move
 *    - E to interact
 *    - SPACE to skip turn
 *    - All your existing key bindings work!
 * 
 * 
 * BENEFITS FOR YOUR UNIVERSITY PROJECT:
 * 
 * ✅ Clear Facade Pattern Implementation
 *    - MainGUIFacade coordinates everything
 *    - Sub-facades handle specific areas
 *    - Clean separation of concerns
 * 
 * ✅ Works with Your Existing Code
 *    - No changes to InputController
 *    - No changes to Strategy classes
 *    - No changes to Maps/MapBuilder
 *    - No changes to Coordinates
 * 
 * ✅ Professional Architecture
 *    - Adapter pattern (MainFrame)
 *    - Builder pattern (your MapBuilder)
 *    - Strategy pattern (your strategies)
 *    - Observer pattern (TurnListener)
 *    - Facade pattern (all the facades)
 * 
 * ✅ Easy to Extend
 *    - Add PauseFacade for pause menu
 *    - Add SettingsFacade for settings
 *    - Add LoadGameFacade for save/load
 *    - Each facade is independent
 * 
 * ✅ Matches Your Screenshot
 *    - 6 left panels in 3 rows
 *    - Map with cyan border
 *    - Interaction panel at bottom
 *    - Exact layout from your image
 * 
 * 
 * DESIGN PATTERNS DEMONSTRATED:
 * 
 * 1. Facade Pattern ⭐ (Main focus)
 *    - MainGUIFacade, GameFacade, LeftPanelFacade, etc.
 *    - Simplifies complex GUI subsystems
 * 
 * 2. Adapter Pattern
 *    - MainFrame adapts JFrame for InputController
 * 
 * 3. Strategy Pattern (Your existing code)
 *    - MovementStrategy, CombatStrategy, InventoryStrategy
 * 
 * 4. Builder Pattern (Your existing code)
 *    - MapBuilder, TestMapBuilder
 * 
 * 5. Observer Pattern (Your existing code)
 *    - TurnListener, InputController
 * 
 * 
 * TROUBLESHOOTING:
 * 
 * Q: Panels not showing?
 * A: Make sure to call facade.startNewGame() with valid map and players
 * 
 * Q: Input not working?
 * A: Check that mainFrame.addKeyListener(inputController) is called
 * 
 * Q: Map not rendering?
 * A: Verify your MapBuilder.startMap() creates the char[][] properly
 * 
 * Q: Turn system not working?
 * A: Ensure setTurnQueue() is called on your MapBuilder
 * 
 * 
 * NEXT STEPS:
 * 
 * 1. Implement your Player class fully
 * 2. Implement your Weapon and Move classes
 * 3. Implement your Item class
 * 4. Customize the panel rendering in paintComponent()
 * 5. Add more visual polish to match your desired aesthetic
 * 6. Test all input modes (map, moves, inventory, interaction)
 * 
 * =============================================================================
 */