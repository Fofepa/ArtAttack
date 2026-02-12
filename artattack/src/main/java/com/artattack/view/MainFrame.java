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
import com.artattack.mapelements.Player;
import com.artattack.mapelements.skilltree.Node;
import com.artattack.mapelements.skilltree.SkillTree;

public class MainFrame implements UIManager {
    private Maps map;
    private Player currentPlayer;
    private GameContext gameContext = null;
    private MainGUIFacade mainGUIFacade;
    
    private MapPanel mapPanel;
    private MovesPanel movesPanel;
    private InventoryPanel inventoryPanel;
    private InteractionPanel interactionPanel;
    private StatsPanel statsPanel;
    private WeaponsPanel weaponsPanel;
    private TurnOrderPanel turnOrderPanel;
    private DetailsPanel detailsPanel;
    private PausePanel pausePanel;

    private MovementStrategy movementStrategy;
    private CombatStrategy combatStrategy;
    private InventoryStrategy inventoryStrategy;

    public MainFrame(Maps map) {
        this.map = map;
        initializePanels();
    }

    public void setGameContext(GameContext gameContext){
        this.gameContext = gameContext;
    }
    
    private void initializePanels() {
        if (map != null) mapPanel = new MapPanel(map);
        interactionPanel = new InteractionPanel();
    }

    public void setMainGUIFacade(MainGUIFacade facade) {
        this.mainGUIFacade = facade;
        if (pausePanel == null && facade != null) {
            pausePanel = new PausePanel(facade);
        }
    }
    
    public Component getFocused() {
        if (mapPanel != null && mapPanel.hasFocus()) return mapPanel;
        if (weaponsPanel != null && weaponsPanel.hasFocus()) return weaponsPanel;
        if (movesPanel != null && movesPanel.hasFocus()) return movesPanel;
        if (inventoryPanel != null && inventoryPanel.hasFocus()) return inventoryPanel;
        if (interactionPanel != null && interactionPanel.hasFocus()) return interactionPanel;
        return mapPanel;
    }
    
    public Maps getMap() { return map; }
    public GameContext getGameContext(){ return this.gameContext; }
    
    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
        
        if (movesPanel == null && player != null) movesPanel = new MovesPanel(player);
        if (inventoryPanel == null && player != null) inventoryPanel = new InventoryPanel(player);
        if (statsPanel == null && player != null) statsPanel = new StatsPanel(player);
        if (weaponsPanel == null && player != null) weaponsPanel = new WeaponsPanel(player);
        if (detailsPanel == null) detailsPanel = new DetailsPanel();
        if (turnOrderPanel == null && map != null) {
            turnOrderPanel = new TurnOrderPanel(map.getConcreteTurnHandler());
        }
        
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
        
        updateCurrentPlayerImage();
    }
    
    public void updateCurrentPlayerImage() {
        if (currentPlayer != null && interactionPanel != null) {
            interactionPanel.setDefaultPlayerImage(getPlayerImagePath(currentPlayer));
        }
    }
    
    private String getPlayerImagePath(Player player) {
        if (player != null && player.hasSprite()) return player.getSpritePath();
        if (player.getType() != null) return "resources/images/" + player.getType().toString().toLowerCase() + ".png";
        return "resources/images/player" + player.getID() + ".png";
    }
    
    
    public Player getCurrentPlayer() { return currentPlayer; }

    public void updateItemDetails(Item item) {
        if (detailsPanel != null) detailsPanel.showItemDetails(item);
    }
    
    // ========== Focus Management ==========
    
    public void focusMapPanel() { if (mapPanel != null) mapPanel.requestFocusInWindow(); }
    public void focusWeaponsPanel() { if (weaponsPanel != null) weaponsPanel.requestFocusInWindow(); }   
    public void focusMovesPanel() { if (movesPanel != null) movesPanel.requestFocusInWindow(); }
    public void focusInventoryPanel() { if (inventoryPanel != null) inventoryPanel.requestFocusInWindow(); }
    public void focusInteractionPanel() { if (interactionPanel != null) interactionPanel.requestFocusInWindow(); }
    
    // ========== Panel Getters ==========
    
    public MapPanel getMapPanel() { return mapPanel; }
    public MovesPanel getMovesPanel() { return movesPanel; }
    public InventoryPanel getInventoryPanel() { return inventoryPanel; }
    public InteractionPanel getInteractionPanel() { return interactionPanel; }
    public StatsPanel getStatsPanel() { return statsPanel; }
    public WeaponsPanel getWeaponsPanel() { return weaponsPanel; }
    public TurnOrderPanel getTurnOrderPanel() { return turnOrderPanel; }
    public DetailsPanel getDetailsPanel() { return detailsPanel; }
    
    // ========== Strategy Getters ==========
    
    public MovementStrategy getMovementStrategy() { return movementStrategy; }
    public CombatStrategy getCombatStrategy() { return combatStrategy; }
    public InventoryStrategy getInventoryStrategy() { return inventoryStrategy; }
    
    // ========== Repaint Methods ==========
    @Override
    public void repaintMapPanel() { if (mapPanel != null) mapPanel.repaint(); }
    public void repaintMovesPanel() { if (movesPanel != null) movesPanel.repaint(); }
    @Override
    public void repaintInventoryPanel() { if (inventoryPanel != null) inventoryPanel.repaint(); }
    public void repaintInteractionPanel() { if (interactionPanel != null) interactionPanel.repaint(); }
    
    public void repaintStatsPanel() { if (statsPanel != null) statsPanel.repaint(); }
    @Override
    public void repaintWeaponsPanel() { if (weaponsPanel != null) weaponsPanel.repaint(); }
    public void repaintTurnOrderPanel() { if (turnOrderPanel != null) turnOrderPanel.repaint(); }
    public void repaintDetailsPanel() { if (detailsPanel != null) detailsPanel.repaint(); }
   
    
    // ========== Visual Updates ==========
    
    public void updateAttackArea() {
        if (mapPanel != null && combatStrategy != null) mapPanel.updateAttackArea(combatStrategy);
    }
    
    public void clearAttackArea() {
        if (mapPanel != null) mapPanel.clearAttackArea();
    }
    
    public void refreshAttackArea() {
        if (mapPanel != null && combatStrategy != null) mapPanel.refreshAttackArea(combatStrategy);
    }
    
    public void updateTurnDisplay() {
        repaintTurnOrderPanel();
        repaintStatsPanel();
    }
    
    // ========== Interaction & Dialog Management ==========

    public void showInteractionPanel(boolean show) {
        if (interactionPanel != null && interactionPanel.getParent() != null) {
             interactionPanel.getParent().setVisible(show);
        }
    }

    public void showDialog(List<String> messages) {
        showDialog(messages, null);
    }
    
    public void showDialog(List<String> messages, String speakerImagePath) {
        if (interactionPanel != null) {
            interactionPanel.showDialog(messages, speakerImagePath);
            refreshInteractionVisibility();
        }
    }
    

    private void refreshInteractionVisibility() {
        interactionPanel.setVisible(true);
        if (interactionPanel.getParent() != null) {
            interactionPanel.getParent().setVisible(true);
            interactionPanel.getParent().revalidate();
        }
        interactionPanel.activateAndFocus();
        interactionPanel.revalidate();
        interactionPanel.repaint();
    }

    public void showDialogWithChoice(String question, List<String> options, Consumer<Integer> callback) {
        showDialogWithChoice(question, options, callback, null);
    }
    
    public void showDialogWithChoice(String question, List<String> options, Consumer<Integer> callback, String speakerImagePath) {
        if (interactionPanel != null) {
            interactionPanel.showDialogWithChoice(question, options, callback, speakerImagePath);
            refreshInteractionVisibility();
        }
    }
    

    public void selectUp() { if (interactionPanel != null) interactionPanel.selectUp(); }
    public void selectDown() { if (interactionPanel != null) interactionPanel.selectDown(); }

    public boolean getDialogActive() { return interactionPanel != null && interactionPanel.isDialogActive(); }
    public boolean getChoiceMode() { return interactionPanel != null && interactionPanel.isChoiceMode(); }
    public boolean getTextFullyRevealed() { return interactionPanel != null && interactionPanel.isTextFullyRevealed(); }
    public int getSelectedOption() { return interactionPanel != null ? interactionPanel.getSelectedOption() : 0; }
    
    public List<String> getResponseOptions() {
        return interactionPanel != null ? interactionPanel.getResponseOptions() : new java.util.ArrayList<>();
    }
    
    public void skipTextAnimation() { if (interactionPanel != null) interactionPanel.skipTextAnimation(); }
    public void confirmChoice() { if (interactionPanel != null) interactionPanel.confirmChoice(); }
    public void advanceDialog() { if (interactionPanel != null) interactionPanel.advanceDialog(); }
    
    // ========== Strategy Visuals ==========
    
    public void updateMovementCursor(Coordinates cursor) {
        if (mapPanel != null && movementStrategy != null) mapPanel.updateMovementCursor(cursor);
    }
    
    public void clearMovementCursor() {
        if (mapPanel != null) mapPanel.clearMovementCursor();
    }
    
    public void showMoveArea(List<Coordinates> moveArea, Coordinates playerPos) {
        if (mapPanel != null) mapPanel.showMoveArea(moveArea, playerPos);
    }
    
    // ========== Global UI Components ==========
    

    public PausePanel getPausePanel() { return pausePanel; }
    public void setPausePanel(PausePanel pausePanel){ this.pausePanel = pausePanel; }
    public void showPauseMenu() { if (pausePanel != null) mainGUIFacade.showPauseMenu(); }
    public void hidePauseMenu() { if (pausePanel != null) mainGUIFacade.hidePauseMenu(); }
    public boolean isPauseMenuVisible() { return pausePanel != null && pausePanel.isVisible(); }
    public boolean isPaused(){ return mainGUIFacade.isPaused(); }

    public void showSkillTreePanel(Player player, SkillTree skillTree, Consumer<Node> callback) {
        if (mainGUIFacade != null) mainGUIFacade.showSkillTreePanel(player, skillTree, callback);
    }

    public void hideSkillTreePanel(){
        mainGUIFacade.hideSkillTreePanel();
    }

    public void focusSkillTreePanel() {
        mainGUIFacade.focusSkillTreePanel();
    }

    public SkillTreePanel getSkillTreePanel() {
        if (mainGUIFacade != null) {
            return mainGUIFacade.getSkillTreePanel(); 
        }
        return null;
    }

    public boolean isSkillTreeVisible() {
        SkillTreePanel panel = getSkillTreePanel();
        return panel != null && panel.isVisible() && panel.getParent() != null;
    }

    public void switchMap(Maps map){
        System.out.println("MainFrame.switchMap called");
        this.map = map;
        this.mapPanel.setMap(map); 
        this.map.getConcreteTurnHandler().addTurnListener(this.mainGUIFacade.getInputController());
        this.map.getConcreteTurnHandler().start();
        this.turnOrderPanel.setTurnHandler(map.getConcreteTurnHandler());
        this.movementStrategy.setMap(map);
        this.combatStrategy.setMap(map);
        repaintMapPanel();
        repaintTurnOrderPanel();
    }

    public String getCurrentState() {
        return mainGUIFacade.getCurrentState();
    }
    
    public MainGUIFacade getMainGUIFacade(){
        return this.mainGUIFacade;
    }

    @Override
    public void showLevelComplete(Maps nextMap) {
        if (mainGUIFacade != null) mainGUIFacade.showLevelComplete(nextMap);
    }
}