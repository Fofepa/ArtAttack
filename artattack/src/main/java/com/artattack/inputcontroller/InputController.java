package com.artattack.inputcontroller;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import com.artattack.enemystrategy.EnemyChoice;
import com.artattack.interactions.InteractionStrategy;
import com.artattack.items.Item;
import com.artattack.mapelements.ActiveElement;
import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.Player;
import com.artattack.mapelements.skilltree.SkillTree;
import com.artattack.moves.Move;
import com.artattack.moves.Weapon;
import com.artattack.turns.TurnListener;
import com.artattack.view.GameContext;
import com.artattack.view.InteractionPanel;
import com.artattack.view.InventoryPanel;
import com.artattack.view.MainFrame;
import com.artattack.view.MapPanel;
import com.artattack.view.MovesPanel;
import com.artattack.view.WeaponsPanel;


public class InputController implements KeyListener, TurnListener {
    private PlayerStrategy currentState;
    private ActiveElement currentElement;
    private MainFrame mainFrame; 
    private EnemyChoice currentEnemyChoice;
    private boolean isEnemyTurn;

    public InputController(MainFrame mainFrame){
        this.mainFrame = mainFrame;
        System.out.println("=== InputController created ===");
    }

    @Override
    public void keyPressed(KeyEvent e){

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.out.println("ESC pressed (global)");
            togglePause();
            return;
        }

        if (mainFrame.isPaused()) {
            System.out.println("Game is paused - input blocked");
            return;
        }

        if (mainFrame.getDialogActive()) {
            System.out.println("-> Dialog active, forcing Interaction Input");
            handleInteractionInput(e);
            return; 
        }

        if (e.getKeyCode() == KeyEvent.VK_F ||
            e.getKeyCode() == KeyEvent.VK_I ||
            e.getKeyCode() == KeyEvent.VK_M) {
            handleFocusInput(e);
            return;
        }

        // enemy turn case
        if(isEnemyTurn){
            if(e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE){
                continueEnemyTurn();
            }
            return; // blocks the other inputs during enemy turn
        }
        
        System.out.println("=== KEY PRESSED: " + KeyEvent.getKeyText(e.getKeyCode()) + " ===");
        System.out.println("Current Element: " + (currentElement != null ? currentElement.getName() : "NULL"));
        System.out.println("Current State: " + (currentState != null ? currentState.getClass().getSimpleName() : "NULL"));
        
        Component focused = mainFrame.getFocused();
        System.out.println("Focused component: " + (focused != null ? focused.getClass().getSimpleName() : "NULL"));

        if(isMapPanelFocused(focused)){
            System.out.println("-> Routing to handleMapInput");
            handleMapInput(e);
        }else if(isWeaponsPanelFocused(focused)){
            System.out.println("-> Routing to handleWeaponsInput");
            handleWeaponsInput(e);
        }else if(isMovesPanelFocused(focused)){
            System.out.println("-> Routing to handleMovesInput");
            handleMovesInput(e);
        }else if(isInteractionPanelFocused(focused)){
            System.out.println("-> Routing to handleInteractionInput");
            handleInteractionInput(e);
        }else if(isInventoryPanelFocused(focused)){
            System.out.println("-> Routing to handleInventoryInput");
            handleInventoryInput(e);
        }else{
            System.out.println("-> No specific panel focused, treating as map input");
            // Default to map input if nothing specific is focused
            handleMapInput(e);
        }
    }

    public boolean isInventoryPanelFocused(Component component) {
        Component current = component;
        while (current != null) {
            if (current instanceof InventoryPanel) {
                return true;
            }
            current = current.getParent();
        }
        return false;
    }

    public boolean isInteractionPanelFocused(Component component){
        Component current = component;
        while (current != null) {
            if (current instanceof InteractionPanel) {
                return true;
            }
            current = current.getParent();
        }
        return false;
    }
    
    public boolean isMovesPanelFocused(Component component){
        Component current = component;
        while (current != null) {
            if (current instanceof MovesPanel) {
                return true;
            }
            current = current.getParent();
        }
        return false;
    }

    public boolean isWeaponsPanelFocused(Component component) {
        Component current = component;
        while (current != null) {
            if (current instanceof WeaponsPanel) {
                return true;
            }
            current = current.getParent();
        }
        return false;
    }

    public boolean isMapPanelFocused(Component component){
        if (component == null) {
            return false;
        }
        Component current = component;
        while (current != null) {
            if (current instanceof MapPanel) {
                return true;
            }
            current = current.getParent();
        }
        return false;
    }

    public void handleFocusInput(KeyEvent e){
        switch(e.getKeyCode()){
            case KeyEvent.VK_M:
                System.out.println("Focusing MapPanel");
                mainFrame.focusMapPanel();
                break;
            case KeyEvent.VK_F:
                System.out.println("Focusing WeaponsPanel");
                mainFrame.focusWeaponsPanel();
                break;
            case KeyEvent.VK_I:
                System.out.println("Focusing InventoryPanel");
                mainFrame.focusInventoryPanel(); 


                //Force correct Details display
                if (currentElement instanceof Player && mainFrame.getInventoryStrategy() != null) {
                    setStrategy(mainFrame.getInventoryStrategy()); 
                    updateInventorySelectionDisplay(
                        mainFrame.getInventoryStrategy(), 
                        (Player) currentElement
                    );
                }

                break;
            default:
                break;
        }
    }

    private void handleInventoryInput(KeyEvent e) {
        if (currentElement == null || !(currentElement instanceof Player)) {
            System.out.println("ERROR: Cannot use inventory - currentElement is not a Player!");
            return;
        }
        
        if (mainFrame.getInventoryStrategy() == null) {
            System.out.println("ERROR: InventoryStrategy is NULL!");
            return;
        }
        
        setStrategy(mainFrame.getInventoryStrategy());
        InventoryStrategy inventoryStrategy = (InventoryStrategy) currentState;
        Player player = (Player) currentElement;

        if (e.getID() == KeyEvent.KEY_PRESSED) { 
        // Note: Update Details regardless of key pressed, 
        // Ensure panel sync.
            updateInventorySelectionDisplay(inventoryStrategy, player);
        }

        /* int selectedItemIndex = inventoryStrategy.getInventoryIndex(); */
        List<Item> inventory = player.getInventory();
        InventoryPanel inventoryPanel = mainFrame.getInventoryPanel();
        
        if (inventoryPanel == null) {
            System.out.println("InventoryPanel not initialized");
            return;
        }
        
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> {
                inventoryStrategy.execute(-1, 0);
                
                System.out.println("UP pressed on InventoryPanel");
                updateInventorySelectionDisplay(inventoryStrategy, player);
            }
            
            case KeyEvent.VK_DOWN -> {
                inventoryStrategy.execute(1, 0);

                System.out.println("DOWN pressed on InventoryPanel");
                updateInventorySelectionDisplay(inventoryStrategy, player);
                
            }
            
            case KeyEvent.VK_ENTER -> {
                inventoryStrategy.acceptItem((Player) currentElement);
                System.out.println("ENTER pressed on InventoryPanel");

                if (mainFrame.getInventoryPanel() != null) {
                    mainFrame.getInventoryPanel().setSelectedIndex(0); 
                }

                //If a dialogue is triggered after using the item, pass the focus to it.
                if (mainFrame.getDialogActive()) {
                    mainFrame.focusInteractionPanel();
                }
                mainFrame.repaintStatsPanel();
                mainFrame.repaintInventoryPanel();
                updateInventorySelectionDisplay(inventoryStrategy, player);
            }

            case KeyEvent.VK_G -> {
                System.out.println("G pressed in InventoryPanel");
                inventoryStrategy.giveItem();

                if (mainFrame.getInventoryPanel() != null) {
                    mainFrame.getInventoryPanel().setSelectedIndex(0); 
                }

                if (mainFrame.getDialogActive()) {
                    mainFrame.focusInteractionPanel();
                }
                mainFrame.repaintStatsPanel();
                mainFrame.repaintInventoryPanel();
                updateInventorySelectionDisplay(inventoryStrategy, player);
            }

            case KeyEvent.VK_P -> {
                System.out.println("P pressed in InventoryPanel.");
                inventoryStrategy.useItemOnOther();

                if (mainFrame.getInventoryPanel() != null) {
                    mainFrame.getInventoryPanel().setSelectedIndex(0); 
                }

                if (mainFrame.getDialogActive()) {
                    mainFrame.focusInteractionPanel();
                }
                mainFrame.repaintStatsPanel();
                mainFrame.repaintInventoryPanel();
                updateInventorySelectionDisplay(inventoryStrategy, player);
            }
            
            default -> {
                handleFocusInput(e);
            }
        }
    }   

    private void handleInteractionInput(KeyEvent e){
        boolean dialogActive = mainFrame.getDialogActive();

        if (!dialogActive) {
            System.out.println("No dialog active");
            mainFrame.focusMapPanel();
            return;
        }

        boolean choiceMode = mainFrame.getChoiceMode();
        boolean textFullyRevealed = mainFrame.getTextFullyRevealed();
        
        if (choiceMode) {
            
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W:
                    if (textFullyRevealed) {
                        mainFrame.selectUp();  // CHANGED: Use MainFrame method
                    }
                    break;
                
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                    if (textFullyRevealed) {
                        mainFrame.selectDown();  // CHANGED: Use MainFrame method
                    }
                    break;
                
                case KeyEvent.VK_ENTER:
                case KeyEvent.VK_SPACE:
                    if (!textFullyRevealed) {
                        mainFrame.skipTextAnimation();   
                    } else {
                        mainFrame.confirmChoice();       
                    }
                    break;
                    
                default:
                    handleFocusInput(e);
                    break;
            }
        } else {
            // Simple dialog mode
            if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) {
                if (!textFullyRevealed) {
                    mainFrame.skipTextAnimation();
                } else {
                    mainFrame.advanceDialog();

                    // Check if dialog finished
                    if (!mainFrame.getDialogActive()) {
                        returnToGameplay();
                    }
                }
            }
        }
    }

    /**
     * Resets the game state based on previous activity.
     */
    private void returnToGameplay() {
        System.out.println("Dialog finished. Checking context...");

        if (mainFrame.getInteractionPanel() != null) {
            mainFrame.getInteractionPanel().resetToDefaultImage();
        }
        
        // 1. Se stiamo combattendo (CombatStrategy), rimaniamo nel pannello delle mosse
        if (currentState instanceof CombatStrategy) {
            System.out.println("Context: Combat -> Returning focus to MovesPanel");
            // Non resettiamo currentState a MovementStrategy!
            mainFrame.focusMovesPanel();
            mainFrame.repaintMovesPanel();
        }
        // 2. Se eravamo nell'inventario (InventoryStrategy), torniamo lì (opzionale, se vuoi)
        else if (currentState instanceof InventoryStrategy) {
            System.out.println("Context: Inventory -> Returning focus to InventoryPanel");
            mainFrame.focusInventoryPanel();
            mainFrame.repaintInventoryPanel();
        }
        // 3. Altrimenti (Default), torniamo alla mappa
        else {
            System.out.println("Context: Exploration -> Returning to MapPanel");
            this.currentState = mainFrame.getMovementStrategy();
            mainFrame.focusMapPanel();
            mainFrame.repaintMapPanel();
        }
    }

    private void handleWeaponsInput(KeyEvent e) {
        if (currentElement == null || !(currentElement instanceof Player)) {
            System.out.println("ERROR: Cannot use weapons - currentElement is not a Player!");
            return;
        }

        if (mainFrame.getCombatStrategy() == null) {
            System.out.println("ERROR: CombatStrategy is NULL!");
            return;
        }

        setStrategy(mainFrame.getCombatStrategy());
        CombatStrategy combatStrategy = (CombatStrategy) currentState;

        List<Weapon> weapons = currentElement.getWeapons();
        WeaponsPanel weaponsPanel = mainFrame.getWeaponsPanel();

        if (weaponsPanel == null || weapons.isEmpty()) {
            System.out.println("WeaponsPanel not initialized or no weapons available");
            return;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> {
                combatStrategy.execute(1, 0);
                weaponsPanel.setSelectedWeaponIndex(combatStrategy.getWeaponIndex());
                System.out.println("Selected weapon: " + weapons.get(combatStrategy.getWeaponIndex()).getName());
                mainFrame.clearAttackArea();
                mainFrame.repaintWeaponsPanel();
            }

            case KeyEvent.VK_DOWN -> {
                combatStrategy.execute(-1, 0);
                weaponsPanel.setSelectedWeaponIndex(combatStrategy.getWeaponIndex());
                System.out.println("Selected weapon: " + weapons.get(combatStrategy.getWeaponIndex()).getName());
                mainFrame.clearAttackArea();
                mainFrame.repaintWeaponsPanel();
            }

            case KeyEvent.VK_RIGHT -> {
                Weapon selectedWeapon = weapons.get(combatStrategy.getWeaponIndex());
                List<Move> moves = selectedWeapon.getMoves();

                if (!moves.isEmpty()) {
                    combatStrategy.setIsSelected(true);
                    MovesPanel mp = mainFrame.getMovesPanel();

                    // Update MovesPanel with current weapon
                    if (mp!= null) {
                        mp.setPlayer((Player) currentElement);
                        mp.setSelectedWeaponIndex(combatStrategy.getWeaponIndex());
                        mp.setActive(true);
                        mainFrame.repaintMovesPanel();
                        mainFrame.updateAttackArea();
                    }

                    // Switch focus to MovesPanel
                    mainFrame.focusMovesPanel();
                    mainFrame.updateAttackArea();

                    System.out.println("Opened moves for: " + selectedWeapon.getName());
                }
            }

            case KeyEvent.VK_SPACE ->{
                System.out.println("SPACE pressed - Skip turn");
                mainFrame.getMap().getConcreteTurnHandler().next();
            }

            default -> {
                handleFocusInput(e);
            }
        }
    }

   private void handleMovesInput(KeyEvent e){
        if (currentElement == null) {
            System.out.println("ERROR: Cannot use moves - currentElement is NULL!");
            return;
        }

        if (mainFrame.getCombatStrategy() == null) {
            System.out.println("ERROR: CombatStrategy is NULL!");
            return;
        }

        setStrategy(mainFrame.getCombatStrategy());
        CombatStrategy combatStrategy = (CombatStrategy) currentState;

        List<Weapon> weapons = currentElement.getWeapons();
        MovesPanel movesPanel = mainFrame.getMovesPanel();

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> {
                if (!weapons.get(combatStrategy.getWeaponIndex()).getMoves().isEmpty()) {
                    combatStrategy.execute(1, 1);
                    if (movesPanel != null) {
                        movesPanel.setSelectedMoveIndex(combatStrategy.getMoveIndex());
                    }
                    mainFrame.updateAttackArea();
                    System.out.println("Selected move: " + weapons.get(combatStrategy.getWeaponIndex()).getMoves().get(combatStrategy.getMoveIndex()).getName());
                }
                mainFrame.repaintMovesPanel();
            }

            case KeyEvent.VK_DOWN -> {
                if (!weapons.get(combatStrategy.getWeaponIndex()).getMoves().isEmpty()) {
                    combatStrategy.execute(-1, 1);
                    if (movesPanel != null) {
                        movesPanel.setSelectedMoveIndex(combatStrategy.getMoveIndex());
                    }
                    mainFrame.updateAttackArea();
                    System.out.println("Selected move: " + weapons.get(combatStrategy.getWeaponIndex()).getMoves().get(combatStrategy.getMoveIndex()).getName());
                }
                mainFrame.repaintMovesPanel();
            }

            case KeyEvent.VK_LEFT -> {
                combatStrategy.setIsSelected(false);
                combatStrategy.setMoveIndex(0); // Reset move selection

                if (mainFrame.getMovesPanel()!= null) {
                    mainFrame.getMovesPanel().setActive(false);
                }

                if (mainFrame.getMapPanel() != null) {
                    mainFrame.clearAttackArea();
                }

                

                // Switch focus back to WeaponsPanel
                mainFrame.focusWeaponsPanel();

                System.out.println("Back to weapon list");
                mainFrame.repaintWeaponsPanel();
            }

            case KeyEvent.VK_ENTER -> {
                if (!weapons.get(combatStrategy.getWeaponIndex()).getMoves().isEmpty()) {
                    System.out.println("=== Using Move ===");
                    System.out.println("Weapon: " + weapons.get(combatStrategy.getWeaponIndex()).getName());
                    System.out.println("Move: " + weapons.get(combatStrategy.getWeaponIndex()).getMoves().get(combatStrategy.getMoveIndex()).getName());

                    int result = combatStrategy.acceptMove();

                    if (result != 0) {
                        System.out.println("✓ Move executed successfully! Damage: " + result);

                        if (mainFrame.getMapPanel() != null) {
                            mainFrame.clearAttackArea();
                            mainFrame.repaintMapPanel();
                        }

                        if (movesPanel != null) {
                            movesPanel.setSelectedMoveIndex(combatStrategy.getMoveIndex());
                        }

                        if (movesPanel != null) {
                            movesPanel.setSelectedMoveIndex(combatStrategy.getMoveIndex());
                        }
                        mainFrame.repaintMovesPanel();
                        mainFrame.repaintTurnOrderPanel();
                        mainFrame.focusMovesPanel();
                    } else {
                        mainFrame.focusMovesPanel();
                        System.out.println("Move failed! (not enough AP or invalid target)");
                    }
                }
            }

            case KeyEvent.VK_SPACE ->{
                System.out.println("SPACE pressed - Skip turn");
                mainFrame.getMap().getConcreteTurnHandler().next();
            }

            default -> {
                handleFocusInput(e);
            }
        }
    }

    private void handleMapInput(KeyEvent e){
        mainFrame.clearAttackArea();
        if (currentElement == null) {
            System.out.println("ERROR: Cannot handle map input - currentElement is NULL!");
            System.out.println("This means updateTurn() was never called or failed!");
            return;
        }
        
        if (mainFrame.getMovementStrategy() == null) {
            System.out.println("ERROR: MovementStrategy is NULL!");
            System.out.println("This means setCurrentPlayer() was never called!");
            return;
        }
        
        setStrategy(mainFrame.getMovementStrategy());
        MovementStrategy movementStrategy = (MovementStrategy) currentState;
        InteractionStrategy interactionStrategy = new InteractionStrategy(movementStrategy);

        int dx = 0, dy = 0;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP, KeyEvent.VK_W -> {
                dy = -1;
                System.out.println("UP pressed - moving cursor");
            }
            case KeyEvent.VK_DOWN, KeyEvent.VK_S -> {
                dy = 1;
                System.out.println("DOWN pressed - moving cursor");
            }
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> {
                dx = -1;
                System.out.println("LEFT pressed - moving cursor");
            }
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> {
                dx = 1;
                System.out.println("RIGHT pressed - moving cursor");
            }
            case KeyEvent.VK_ENTER -> {
                System.out.println("ENTER pressed - Confirming movement");
                if (movementStrategy.getSelectedState()) {
                    movementStrategy.acceptMovement();
                    
                    /* if (mainFrame.getMovesPanel() != null) {
                        mainFrame.refreshAttackArea();
                    } */
                    mainFrame.clearAttackArea();
                    mainFrame.repaintStatsPanel();
                    mainFrame.updateTurnDisplay();
                    mainFrame.repaintMapPanel();
                }
                return;
            }
            case KeyEvent.VK_E -> {
                System.out.println("E pressed - Interaction");
                if (movementStrategy.getSelectedState()) {
                    interactionStrategy.acceptInteraction();
                }
                return;
            }
            case KeyEvent.VK_SPACE -> {
                System.out.println("SPACE pressed - Skip turn");
                mainFrame.getMap().getConcreteTurnHandler().next();
                return;
            }
            case KeyEvent.VK_M, KeyEvent.VK_F, KeyEvent.VK_I -> {
                handleFocusInput(e);
                return;
            }
            default -> {
                return;
            }
        }
        
        // Execute movement strategy with dx, dy
        if (dx != 0 || dy != 0) {
            System.out.println("Executing movement: dx=" + dx + ", dy=" + dy);
            movementStrategy.execute(dx, dy);
            mainFrame.repaintMapPanel();
        }
    }

    // starts the enemy turn using the enemyChoice
    public void startEnemyTurn(EnemyChoice enemyChoice){
        this.currentEnemyChoice = enemyChoice;
        this.isEnemyTurn = true;

        mainFrame.repaintStatsPanel();
        mainFrame.updateTurnDisplay();
        mainFrame.repaintMapPanel();

        mainFrame.showDialog(List.of("Enemy Turn"));

        continueEnemyTurn();    // start the moves
    }

    // does the choice() method until hasFinished is true
    public void continueEnemyTurn(){
        if(this.currentElement == null || this.currentEnemyChoice.getHasFinished()){
            endEnemyTurn();
            return;
        }

        if(mainFrame.getDialogActive() && mainFrame.getInteractionPanel().getParent() != null){
            mainFrame.getInteractionPanel().getParent().setVisible(false);
        }

        this.currentEnemyChoice.choose();

        mainFrame.repaintStatsPanel();
        mainFrame.updateTurnDisplay();
        mainFrame.repaintMapPanel();

        if(this.currentEnemyChoice.getHasFinished()){
            endEnemyTurn();
        }
    }

    // ends the enemy turn
    public void endEnemyTurn(){
        this.isEnemyTurn = false;
        this.currentEnemyChoice = null;

        if(mainFrame.getDialogActive() && mainFrame.getInteractionPanel().getParent() != null){
            mainFrame.getInteractionPanel().getParent().setVisible(false);
        }

        mainFrame.getMap().getConcreteTurnHandler().next();
        
        mainFrame.repaintStatsPanel();
        mainFrame.updateTurnDisplay();
        mainFrame.repaintMapPanel();

        mainFrame.showDialog(List.of("Enemy turn ended"));
        System.out.println("enemy turn ended");
    }
    
    public void setStrategy(PlayerStrategy strategy){
        this.currentState = strategy;
    }

    @Override
    public void updateTurn(ActiveElement activeElement){
        System.out.println("\n=== UPDATE TURN CALLED ===");
        System.out.println("ActiveElement: " + (activeElement != null ? activeElement.getName() : "NULL"));
        
        if (activeElement != null) {
            System.out.println("HP: " + activeElement.getCurrHP() + "/" + activeElement.getMaxHP());
            System.out.println("AP: " + activeElement.getActionPoints());
        }
        
        this.currentElement = activeElement;
        
        mainFrame.clearAttackArea();
        mainFrame.clearMovementCursor();

        if (activeElement instanceof Player){ 
            System.out.println(">> PLAYER TURN: " + activeElement.getName());
            
            Player player = (Player) activeElement;
            mainFrame.setCurrentPlayer(player);
            mainFrame.focusMapPanel();
            

            InteractionPanel interactionPanel = mainFrame.getInteractionPanel();
            if (interactionPanel != null) {
                // 1. Update the default fallback image to the current player's sprite
                interactionPanel.setDefaultPlayerImage(player.getSpritePath());
                
                // 2. Force the current image to match the default (unless a dialog is already forcing another)
                if (!interactionPanel.isDialogActive()) {
                    interactionPanel.resetToDefaultImage();
                }
            }
            
            // Check if the player leveled up
            if (player.getLeveledUp()) {
                System.out.println(">>> PLAYER LEVELED UP! Opening Skill Tree...");
                handlePlayerLevelUp(player);
                return;
            }
            

            // Verify strategies were created
            System.out.println("MovementStrategy created: " + (mainFrame.getMovementStrategy() != null));
            System.out.println("CombatStrategy created: " + (mainFrame.getCombatStrategy() != null));
            System.out.println("InventoryStrategy created: " + (mainFrame.getInventoryStrategy() != null));
            
        } else if(activeElement instanceof Enemy){
            System.out.println(">> ENEMY TURN: " + activeElement.getName());
            //handleEnemyTurn()

            /* InteractionPanel interactionPanel = mainFrame.getInteractionPanel();
            if (interactionPanel != null) {
                interactionPanel.setSpeakerImage(activeElement.getSpritePath());
            }
            */

            EnemyChoice enemyChoice = new EnemyChoice(this.mainFrame);
            enemyChoice.setMap(mainFrame.getMap());
            enemyChoice.setEnemy((Enemy) currentElement);

            startEnemyTurn(enemyChoice);
        }
        
        mainFrame.updateTurnDisplay();
        mainFrame.repaintMapPanel();
        System.out.println("=== UPDATE TURN COMPLETE ===\n");
    }

    /**
     * Handles the player level up by showing the skill tree panel
     */
    private void handlePlayerLevelUp(Player player) {
        GameContext context = mainFrame.getGameContext();
        if (context == null) {
            System.err.println("ERROR: GameContext is null!");
            return;
        }
        
        // Get the appropriate skill tree based on player ID
        SkillTree skillTree = player.getSkillTree();
        /*if (player.getID() == 1) {
            skillTree = context.getPlayer1SkillTree();
        } else if (player.getID() == 2) {
            skillTree = context.getPlayer2SkillTree();
        }*/
        
        if (skillTree == null) {
            System.err.println("ERROR: SkillTree not found for player: " + player.getName());
            return;
        }
        
        // Show the skill tree panel
        mainFrame.showSkillTreePanel(player, skillTree, (selectedNode) -> {
            // This callback is called when the player confirms their selection
            System.out.println(">>> SKILL UNLOCKED: Node #" + selectedNode.getLabel());
            System.out.println(">>> Player: " + player.getName() + " | Type: " + selectedNode.getClass().getSimpleName());
            
            // Update all panels to reflect new stats
            mainFrame.repaintStatsPanel();
            mainFrame.repaintWeaponsPanel();
            mainFrame.repaintMovesPanel();
            mainFrame.repaintMapPanel();
            mainFrame.updateTurnDisplay();
            
            // Show a confirmation message
            String nodeType = getNodeTypeName(selectedNode);
            mainFrame.showDialog(List.of(
                player.getName() + " has unlocked a new skill!",
                "Skill: " + nodeType,
                "Press ENTER to continue..."
            ));
        });
    }
    
    /**
     * Gets a human-readable name for the node type
     */
    private String getNodeTypeName(com.artattack.mapelements.skilltree.Node node) {
        String className = node.getClass().getSimpleName();
        return switch (className) {
            case "HPNODE" -> "Health Boost";
            case "APNODE" -> "Action Points Boost";
            case "SPNODE" -> "Speed Boost";
            case "MANODE" -> "Movement Area Expansion";
            case "MAXWPNODE" -> "Weapon Slot Unlock";
            case "MAXMVNODE" -> "Move Capacity Increase";
            case "SpecialMoveNODE" -> "Special Move";
            default -> "Unknown Skill";
        };
    }


    public void togglePause(){
        if(mainFrame.isPauseMenuVisible()){
            System.out.println("Resuming game");
            mainFrame.hidePauseMenu();
        }else{
            System.out.println("Pausing game");
            mainFrame.showPauseMenu();
        }
    }


    private void updateInventorySelectionDisplay(InventoryStrategy strategy, Player player) {
        if (mainFrame.getInventoryPanel() != null) {
            mainFrame.getInventoryPanel().setSelectedIndex(strategy.getInventoryIndex());
            mainFrame.repaintInventoryPanel();
        }

        // Aggiorna il DetailsPanel con l'oggetto correntemente selezionato
        List<Item> inventory = player.getInventory();
        int idx = strategy.getInventoryIndex();

        if (idx >= 0 && idx < inventory.size()) {
            Item selectedItem = inventory.get(idx);
            mainFrame.updateItemDetails(selectedItem);
        } else {
            mainFrame.updateItemDetails(null);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {

        if (currentState == null) {
            return;
        }

        String s = "";
        s = switch (currentState.getType()) {
            case 0 -> "MovementStrategy";
            case 1 -> "CombatStrategy";
            case 2 -> "InventoryStrategy";
            default -> "Unknown";
        };
        System.out.println(e.getKeyChar() + " has been pressed in " + s);
    }
}