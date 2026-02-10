package com.artattack.view;

import java.awt.Component;
import java.util.List;
import java.util.function.Consumer;

import com.artattack.inputcontroller.CombatStrategy;
import com.artattack.inputcontroller.InventoryStrategy;
import com.artattack.inputcontroller.MovementStrategy;
import com.artattack.items.Item;
import com.artattack.level.Coordinates;
import com.artattack.level.Maps;
import com.artattack.mapelements.MapElement;
import com.artattack.mapelements.Player;
import com.artattack.mapelements.skilltree.Node;
import com.artattack.mapelements.skilltree.SkillTree;

/**
 * MainFrame adapter that bridges the Facade pattern with the InputController
 * This class acts as the interface between the GUI and the game logic
 */
public class MainFrame implements UIManager {
    private Maps map;
    private Player currentPlayer;
    
    // Panels
    private MapPanel mapPanel;
    private MovesPanel movesPanel;
    private InventoryPanel inventoryPanel;
    private InteractionPanel interactionPanel;
    private StatsPanel statsPanel;
    private WeaponsPanel weaponsPanel;
    private TurnOrderPanel turnOrderPanel;
    private DetailsPanel detailsPanel;
    private SpritePanel spritePanel;
    private PausePanel pausePanel;

    private MainGUIFacade mainGUIFacade;
    
    // Current strategies
    private MovementStrategy movementStrategy;
    private CombatStrategy combatStrategy;
    private InventoryStrategy inventoryStrategy;

    private GameContext gameContext = null;
    
    public MainFrame(Maps map) {
        this.map = map;
        initializePanels();
    }

    public void setGameContext(GameContext gameContext){
        this.gameContext = gameContext;
    }
    
    private void initializePanels() {
        // Initialize map panel immediately since it doesn't depend on player
        if (map != null) {
            mapPanel = new MapPanel(map);
        }
        
        // Initialize sprite panel
        spritePanel = new SpritePanel();
        
        // Initialize interaction panel
        interactionPanel = new InteractionPanel();
        
        // Link panels to existing InteractableElements
        //linkInteractablePanels();
        
        // Other panels will be initialized when currentPlayer is set
    }
    
    /**
     * Links the sprite and interaction panels to all InteractableElements in the map
     * Call this after map is loaded and panels are initialized
     */
    /*public void linkInteractablePanels() {
        if (map != null && map.getDict() != null) {
            for (MapElement element : map.getDict().values()) {
                switch (element) {
                    case InteractableElement ie -> {
                        try {
                            ie.setSpritePanel(spritePanel);
                            ie.setMainFrame(this);  // CHANGED: Only set MainFrame, not InteractionPanel
                        } catch (Exception e) {
                            System.err.println("Could not set panels for InteractableElement: " + ie.getName());
                        }
                    }
                    case Trigger t -> {
                        try {
                            if (t.getTriggerGroup().getInteraction().getMainFrame() == null) {
                                t.getTriggerGroup().getInteraction().setMainFrame(this);
                            }
                        } catch (Exception e) {
                            System.err.println("Could not set panels for Trigger: " + t.getName());
                        }
                    }
                    default -> {
                    }
                }
            }
        }
    }*/


    public void setMainGUIFacade(MainGUIFacade facade) {
        this.mainGUIFacade = facade;
        if (pausePanel == null && facade != null) {
            pausePanel = new PausePanel(facade);
        }
    }
    
    // ========== Methods called by InputController ==========
    
    public Component getFocused() {
        // Check each panel to see which one has focus
        if (mapPanel != null && mapPanel.hasFocus()) {
            return mapPanel;
        }
        if (weaponsPanel != null && weaponsPanel.hasFocus()) {
            return weaponsPanel;
        }
        if (movesPanel != null && movesPanel.hasFocus()) {
            return movesPanel;
        }
        if (inventoryPanel != null && inventoryPanel.hasFocus()) {
            return inventoryPanel;
        }
        if (interactionPanel != null && interactionPanel.hasFocus()) {
            return interactionPanel;
        }
        
        // If nothing has focus, return mapPanel as default
        return mapPanel;
    }
    
    public Maps getMap() {
        return map;
    }

    public GameContext getGameContext(){
        return this.gameContext;
    }
    
    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
        
        // Initialize player-dependent panels if not already done
        if (movesPanel == null && player != null) {
            movesPanel = new MovesPanel(player);
        }
        if (inventoryPanel == null && player != null) {
            inventoryPanel = new InventoryPanel(player);
        }
        if (statsPanel == null && player != null) {
            statsPanel = new StatsPanel(player);
        }
        if (weaponsPanel == null && player != null) {
            weaponsPanel = new WeaponsPanel(player);
        }
        if (detailsPanel == null) {
            detailsPanel = new DetailsPanel();
        }
        if (turnOrderPanel == null && map != null) {
            turnOrderPanel = new TurnOrderPanel(map.getConcreteTurnHandler());
        }
        
        // Initialize strategies
        if (map != null && player != null) {
            movementStrategy = new MovementStrategy(map, player);
            movementStrategy.setMainFrame(this);
            combatStrategy = new CombatStrategy(map, player);
            combatStrategy.setMainFrame(this);
            inventoryStrategy = new InventoryStrategy(map, player);
            inventoryStrategy.setMainFrame(this);
        }



        weaponsPanel.setPlayer(player);
        movesPanel.setPlayer(player);
        statsPanel.setPlayer(player);
        inventoryPanel.setPlayer(player);
        
        // ========== SET DEFAULT PLAYER IMAGE ==========
        updateCurrentPlayerImage();
    }
    
    /**
     * Updates the interaction panel to show the current player's image
     */
    public void updateCurrentPlayerImage() {
        if (currentPlayer != null && interactionPanel != null) {
            String imagePath = getPlayerImagePath(currentPlayer);
            interactionPanel.setDefaultPlayerImage(imagePath);
        }
    }
    
    /**
     * Gets the image path for a given player based on their type or ID
     */
    private String getPlayerImagePath(Player player) {
        // ⭐ PRIORITÀ 1: Se il player ha uno sprite, usa quello
        if (player != null && player.hasSprite()) {
            return player.getSpritePath();
        }
        
        // PRIORITÀ 2: Basato sul tipo di player
        if (player.getType() != null) {
            return "resources/images/" + player.getType().toString().toLowerCase() + ".png";
        }
        
        // PRIORITÀ 3: Basato sull'ID del player
        return "resources/images/player" + player.getID() + ".png";
    }
    
    /**
     * Gets the image path for a MapElement (NPCs, enemies, etc.)
     */
    private String getElementImagePath(MapElement element) {
        // ⭐ PRIORITÀ 1: Se l'elemento ha uno sprite, usa quello
        if (element != null && element.hasSprite()) {
            return element.getSpritePath();
        }
        
        // PRIORITÀ 2: Genera path basato sul nome
        if (element != null) {
            String cleanName = element.getName().toLowerCase().replaceAll("\\s+", "_");
            return "resources/images/" + cleanName + ".png";
        }
        
        return null;
    }
    
    public Player getCurrentPlayer() {
        return currentPlayer;
    }


    public void updateItemDetails(Item item) {
        if (detailsPanel != null) {
            detailsPanel.showItemDetails(item);
        }
    }
    
    // ========== Focus Management ==========
    
    public void focusMapPanel() {
        if (mapPanel != null) {
            mapPanel.requestFocusInWindow();
            System.out.println("Focus set to MapPanel");
        }
    }

    public void focusWeaponsPanel() {
        if (weaponsPanel != null) {
            weaponsPanel.requestFocusInWindow();
            /* weaponsPanel.setFocusable(true);
            weaponsPanel.requestFocus(); */
            System.out.println("Focus set to WeaponsPanel");
        }
    }   
    
    public void focusMovesPanel() {
        if (movesPanel != null) {
            movesPanel.requestFocusInWindow();
            System.out.println("Focus set to MovesPanel");
        }
    }
    
    public void focusInventoryPanel() {
        if (inventoryPanel != null) {
            inventoryPanel.requestFocusInWindow();
            System.out.println("Focus set to InventoryPanel");
        }
    }
    
    public void focusInteractionPanel() {
        if (interactionPanel != null) {
            interactionPanel.requestFocusInWindow();
            System.out.println("Focus set to InteractionPanel");
        }
    }
    
    // ========== Panel Getters ==========
    
    public MapPanel getMapPanel() {
        return mapPanel;
    }
    
    public MovesPanel getMovesPanel() {
        return movesPanel;
    }
    
    public InventoryPanel getInventoryPanel() {
        return inventoryPanel;
    }
    
    public InteractionPanel getInteractionPanel() {
        return interactionPanel;
    }
    
    public StatsPanel getStatsPanel() {
        return statsPanel;
    }
    
    public WeaponsPanel getWeaponsPanel() {
        return weaponsPanel;
    }
    
    public TurnOrderPanel getTurnOrderPanel() {
        return turnOrderPanel;
    }
    
    public DetailsPanel getDetailsPanel() {
        return detailsPanel;
    }
    
    public SpritePanel getSpritePanel() {
        return spritePanel;
    }
    
    // ========== Strategy Getters ==========
    
    public MovementStrategy getMovementStrategy() {
        return movementStrategy;
    }
    
    public CombatStrategy getCombatStrategy() {
        return combatStrategy;
    }
    
    public InventoryStrategy getInventoryStrategy() {
        return inventoryStrategy;
    }
    
    // ========== Repaint Methods ==========
    
    public void repaintMapPanel() {
        if (mapPanel != null) {
            mapPanel.repaint();
        }
    }
    
    public void repaintMovesPanel() {
        if (movesPanel != null) {
            movesPanel.repaint();
        }
    }
    
    public void repaintInventoryPanel() {
        if (inventoryPanel != null) {
            inventoryPanel.repaint();
        }
    }
    
    public void repaintInteractionPanel() {
        if (interactionPanel != null) {
            interactionPanel.repaint();
        }
    }
    
    public void repaintStatsPanel() {
        if (statsPanel != null) {
            statsPanel.repaint();
        }
    }
    
    public void repaintWeaponsPanel() {
        if (weaponsPanel != null) {
            weaponsPanel.repaint();
        }
    }
    
    public void repaintTurnOrderPanel() {
        if (turnOrderPanel != null) {
            turnOrderPanel.repaint();
        }
    }
    
    public void repaintDetailsPanel() {
        if (detailsPanel != null) {
            detailsPanel.repaint();
        }
    }
    
    public void repaintSpritePanel() {
        if (spritePanel != null) {
            spritePanel.repaint();
        }
    }
    
    // ========== Attack Area Management (for combat) ==========
    
    public void updateAttackArea() {
        if (mapPanel != null && combatStrategy != null) {
            mapPanel.updateAttackArea(combatStrategy);
        }
    }
    
    public void clearAttackArea() {
        if (mapPanel != null) {
            mapPanel.clearAttackArea();
        }
    }
    
    public void refreshAttackArea() {
        if (mapPanel != null && combatStrategy != null) {
            mapPanel.refreshAttackArea(combatStrategy);
        }
    }
    
    // ========== Turn Display ==========
    
    public void updateTurnDisplay() {
        repaintTurnOrderPanel();
        repaintStatsPanel();
    }
    
    // ========== Dialog Management (for InteractionPanel) ==========


    public void showInteractionPanel(boolean show) {
        if (interactionPanel != null && interactionPanel.getParent() != null) {
             // Il parent è interactionContainer in CenterPanelFacade
             interactionPanel.getParent().setVisible(show);
             
             // Gestiamo anche lo sprite panel che è fratello nel layout
             if (spritePanel != null) {
                 spritePanel.setVisible(show);
             }
        }
    }

    /**
     * Starts an interaction with a speaker (NPC, object, etc.)
     * Shows their sprite image if they have one
     */
    public void startInteraction(String message, String spriteName, MapElement speaker) {
        // 1. Carica lo sprite (vecchio sistema, se ancora lo usi)
        if (spriteName != null && spritePanel != null) {
            spritePanel.loadImage(spriteName);
        } else if (spritePanel != null) {
            spritePanel.clearSprite();
        }

        // 2. Mostra il dialogo con lo sprite del MapElement
        showDialog(List.of(message), speaker);
    }
    
    /**
     * Overload for backward compatibility
     */
    public void startInteraction(String message, String spriteName) {
        startInteraction(message, spriteName, null);
    }

    /**
     * Shows dialog without changing the speaker image (uses default player image)
     */
    public void showDialog(List<String> messages) {
        showDialog(messages, (MapElement) null);
    }
    
    /**
     * Shows dialog with optional speaker image from path
     */
    public void showDialog(List<String> messages, String speakerImagePath) {
        if (interactionPanel != null) {
            interactionPanel.showDialog(messages, speakerImagePath);
            
            // Ensure the panel is visible
            interactionPanel.setVisible(true);
            
            // If the panel is inside a container (like in the Facade), 
            // we must ensure the parent container is also visible
            if (interactionPanel.getParent() != null) {
                interactionPanel.getParent().setVisible(true);
                interactionPanel.getParent().revalidate();
            }

            // Trigger focus and internal state
            interactionPanel.activateAndFocus();
            
            // Ensure the UI refreshes to show the new content
            interactionPanel.revalidate();
            interactionPanel.repaint();
        }
    }
    
    /**
     * Shows dialog with speaker as MapElement (uses element's sprite if available)
     * If speaker is null or has no sprite, uses default player image
     */
    public void showDialog(List<String> messages, MapElement speaker) {
        if (interactionPanel != null) {
            interactionPanel.showDialog(messages, speaker);
            
            interactionPanel.setVisible(true);
            
            if (interactionPanel.getParent() != null) {
                interactionPanel.getParent().setVisible(true);
                interactionPanel.getParent().revalidate();
            }

            interactionPanel.activateAndFocus();
            
            interactionPanel.revalidate();
            interactionPanel.repaint();
        }
    }

    /**
     * Shows a dialog with multiple choice options (uses default player image)
     */
    public void showDialogWithChoice(String question, List<String> options, Consumer<Integer> callback) {
        showDialogWithChoice(question, options, callback, (MapElement) null);
    }
    
    /**
     * Shows a dialog with multiple choice options and speaker image from path
     */
    public void showDialogWithChoice(String question, List<String> options, Consumer<Integer> callback, String speakerImagePath) {
        if (interactionPanel != null) {
            interactionPanel.showDialogWithChoice(question, options, callback, speakerImagePath);
            
            interactionPanel.setVisible(true);
            
            if (interactionPanel.getParent() != null) {
                interactionPanel.getParent().setVisible(true);
                interactionPanel.getParent().revalidate();
            }

            interactionPanel.activateAndFocus();
            
            interactionPanel.revalidate();
            interactionPanel.repaint();
        }
    }
    
    /**
     * Shows a dialog with multiple choice options and speaker as MapElement
     * Uses element's sprite if available
     */
    public void showDialogWithChoice(String question, List<String> options, Consumer<Integer> callback, MapElement speaker) {
        if (interactionPanel != null) {
            interactionPanel.showDialogWithChoice(question, options, callback, speaker);
            
            interactionPanel.setVisible(true);
            
            if (interactionPanel.getParent() != null) {
                interactionPanel.getParent().setVisible(true);
                interactionPanel.getParent().revalidate();
            }

            interactionPanel.activateAndFocus();
            
            interactionPanel.revalidate();
            interactionPanel.repaint();
        }
    }

    /**
     * Moves selection up in choice dialogs
     */
    public void selectUp() {
        if (interactionPanel != null) {
            interactionPanel.selectUp();
        }
    }

    /**
     * Moves selection down in choice dialogs
     */
    public void selectDown() {
        if (interactionPanel != null) {
            interactionPanel.selectDown();
        }
    }

    
    public boolean getDialogActive() {
        return interactionPanel != null && interactionPanel.isDialogActive();
    }
    
    public boolean getChoiceMode() {
        return interactionPanel != null && interactionPanel.isChoiceMode();
    }
    
    public boolean getTextFullyRevealed() {
        return interactionPanel != null && interactionPanel.isTextFullyRevealed();
    }
    
    public int getSelectedOption() {
        return interactionPanel != null ? interactionPanel.getSelectedOption() : 0;
    }
    
    public List<String> getResponseOptions() {
        return interactionPanel != null ? interactionPanel.getResponseOptions() : new java.util.ArrayList<>();
    }
    
    public void skipTextAnimation() {
        if (interactionPanel != null) {
            interactionPanel.skipTextAnimation();
        }
    }
    
    public void confirmChoice() {
        if (interactionPanel != null) {
            interactionPanel.confirmChoice();
        }
    }
    
    public void advanceDialog() {
        if (interactionPanel != null) {
            interactionPanel.advanceDialog();
        }
    }
    
    // ========== Movement Display (for MovementStrategy) ==========
    
    public void updateMovementCursor(Coordinates cursor) {
        if (mapPanel != null && movementStrategy != null) {
            mapPanel.updateMovementCursor(cursor);
        }
    }
    
    public void clearMovementCursor() {
        if (mapPanel != null) {
            mapPanel.clearMovementCursor();
        }
    }
    
    public void showMoveArea(List<Coordinates> moveArea, Coordinates playerPos) {
        if (mapPanel != null) {
            mapPanel.showMoveArea(moveArea, playerPos);
        }
    }
    
    // ========== Sprite Panel Management ==========
    
    public void loadSprite(String spritePath) {
        if (spritePanel != null) {
            spritePanel.loadImage(spritePath);
            repaintSpritePanel();
        }
    }
    
    public void clearSprite() {
        if (spritePanel != null) {
            spritePanel.clearSprite();
            repaintSpritePanel();
        }
    }


    // ========== Pause Panel Management ==========

    public PausePanel getPausePanel() {
        return pausePanel;
    }

    public void setPausePanel(PausePanel pausePanel){
        this.pausePanel = pausePanel;
    }

    public void showPauseMenu() {
        if (pausePanel != null) {
           mainGUIFacade.showPauseMenu();
        }
    }

    public void hidePauseMenu() {
        if (pausePanel != null) {
            mainGUIFacade.hidePauseMenu();
        }
    }

    public boolean isPauseMenuVisible() {
        return pausePanel != null && pausePanel.isVisible();
    }

    public boolean isPaused(){
        return mainGUIFacade.isPaused();
    }

    // ========== Skill Tree Panel Management ==========
    
    public void showSkillTreePanel(Player player, SkillTree skillTree, Consumer<Node> callback) {
        if (mainGUIFacade != null) {
            mainGUIFacade.showSkillTreePanel(player, skillTree, callback);
        }
    }

    // ========== Switch Map Management ==========
    public void switchMap(Maps map){
        this.map = map;
        this.mapPanel.setMap(map); //Need to add updateMap
        //linkInteractablePanels();
        this.map.getConcreteTurnHandler().addTurnListener(this.mainGUIFacade.getInputController());
        this.map.getConcreteTurnHandler().start();
        this.turnOrderPanel.setTurnHandler(map.getConcreteTurnHandler());
        this.movementStrategy.setMap(map); //movementStrategy has mainFrame. Should we get map from ManeFrame??
        this.combatStrategy.setMap(map); //Same as movementStrategy
        repaintMapPanel();
        repaintTurnOrderPanel();
    }
    
    /**
     * Called when dialog ends to reset the image back to current player
     */
    public void resetDialogImage() {
        if (interactionPanel != null) {
            interactionPanel.resetToDefaultImage();
        }
    }


   
    @Override
    public void showLevelComplete(Maps nextMap) {
        if (mainGUIFacade != null) {
            mainGUIFacade.showLevelComplete(nextMap);
        }
    }
}