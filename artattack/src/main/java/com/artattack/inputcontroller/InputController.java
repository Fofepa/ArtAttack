package com.artattack.inputcontroller;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import com.artattack.interactions.InteractionStrategy;
import com.artattack.items.Item;
import com.artattack.mapelements.ActiveElement;
import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.Player;
import com.artattack.moves.Move;
import com.artattack.moves.Weapon;
import com.artattack.turns.TurnListener;
import com.artattack.view.InteractionPanel;
import com.artattack.view.InventoryPanel;
import com.artattack.view.MainFrame;
import com.artattack.view.MapPanel;
import com.artattack.view.MovesPanel;


public class InputController implements KeyListener, TurnListener {
    private PlayerStrategy currentState;
    private ActiveElement currentElement;
    private MainFrame mainFrame; 

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
        
        System.out.println("=== KEY PRESSED: " + KeyEvent.getKeyText(e.getKeyCode()) + " ===");
        System.out.println("Current Element: " + (currentElement != null ? currentElement.getName() : "NULL"));
        System.out.println("Current State: " + (currentState != null ? currentState.getClass().getSimpleName() : "NULL"));
        
        Component focused = mainFrame.getFocused();
        System.out.println("Focused component: " + (focused != null ? focused.getClass().getSimpleName() : "NULL"));

        if(isMapPanelFocused(focused)){
            System.out.println("-> Routing to handleMapInput");
            handleMapInput(e);
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
                System.out.println("Focusing MovesPanel");
                mainFrame.focusMovesPanel();
                break;
            case KeyEvent.VK_I:
                System.out.println("Focusing InventoryPanel");
                mainFrame.focusInventoryPanel(); 
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

        /* int selectedItemIndex = inventoryStrategy.getInventoryIndex(); */
        List<Item> inventory = ((Player) currentElement).getInventory();
        InventoryPanel inventoryPanel = mainFrame.getInventoryPanel();
        
        if (mainFrame.getInventoryPanel() == null) {
            System.out.println("InventoryPanel not initialized");
            return;
        }
        
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> {
                inventoryStrategy.execute(-1, 0);

                if (inventoryPanel != null) {
                inventoryPanel.setSelectedIndex(inventoryStrategy.getInventoryIndex());
            }

                System.out.println("UP pressed on InventoryPanel");
                mainFrame.repaintInventoryPanel();
            }
            
            case KeyEvent.VK_DOWN -> {
                inventoryStrategy.execute(1, 0);

                if (inventoryPanel != null) {
                inventoryPanel.setSelectedIndex(inventoryStrategy.getInventoryIndex());
            }

                System.out.println("DOWN pressed on InventoryPanel");
                mainFrame.repaintInventoryPanel();
            }
            
            case KeyEvent.VK_ENTER -> {
                inventoryStrategy.acceptItem((Player) currentElement);
                System.out.println("ENTER pressed on InventoryPanel");

                if (inventoryPanel != null) {
                inventoryPanel.setSelectedIndex(0); 
                }   

                //If a dialogue is triggered after using the item, pass the focus to it.
                if (mainFrame.getDialogActive()) {
                    mainFrame.focusInteractionPanel();
                }
                mainFrame.repaintStatsPanel();
                mainFrame.repaintInventoryPanel();
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
     * Resets the game state to Map Exploration and refocuses the Map Panel.
     * This is called automatically whenever any dialog finishes.
     */
    private void returnToGameplay() {
        System.out.println("Dialog finished: Returning to Map Exploration...");

        // 1. Hide the dialog UI container (Facade cleanup)
        if (mainFrame.getInteractionPanel().getParent() != null) {
            mainFrame.getInteractionPanel().getParent().setVisible(false);
        }
        
        // 2. Set the internal controller state back to Movement
        // This ensures keys (I, M, Arrows) control the player on the map again
        this.currentState = mainFrame.getMovementStrategy();
        
        // 3. Physically move the keyboard focus back to the MapPanel
        mainFrame.focusMapPanel();
        
        // 4. Update the visual display
        mainFrame.repaintMapPanel();
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

        /* int selectedWeaponIndex = combatStrategy.getWeaponIndex();
        int selectedMoveIndex = combatStrategy.getMoveIndex(); */
        /* boolean isInMoveSelection = (selectedMoveIndex > 0 || combatStrategy.isSelected()); */

        List<Weapon> weapons = currentElement.getWeapons();

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> {
                if (!combatStrategy.isSelected()) {
                    combatStrategy.execute(1, 0);
                    System.out.println("Selected weapon: " + weapons.get(combatStrategy.getWeaponIndex()).getName());
                } else {
                    if (!weapons.get(combatStrategy.getWeaponIndex()).getMoves().isEmpty()) {
                        combatStrategy.execute(1,1);
                        mainFrame.updateAttackArea();
                        System.out.println("Selected move: " + weapons.get(combatStrategy.getWeaponIndex()).getMoves().get(combatStrategy.getMoveIndex()).getName());
                    }
                }
                mainFrame.repaintMovesPanel();
            }
            
            case KeyEvent.VK_DOWN -> {
                if (!combatStrategy.isSelected()) {
                    combatStrategy.execute(-1, 0);
                    System.out.println("Selected weapon: " + weapons.get(combatStrategy.getWeaponIndex()).getName());
                } else {
                    if (!weapons.get(combatStrategy.getWeaponIndex()).getMoves().isEmpty()) {
                        combatStrategy.execute(-1,1);
                        mainFrame.updateAttackArea();
                        System.out.println("Selected move: " + weapons.get(combatStrategy.getWeaponIndex()).getMoves().get(combatStrategy.getMoveIndex()).getName());
                    }
                }
                mainFrame.repaintMovesPanel();
            }
            
            case KeyEvent.VK_RIGHT -> {
                if (!combatStrategy.isSelected() && !weapons.isEmpty()) {
                    Weapon selectedWeapon = weapons.get(combatStrategy.getWeaponIndex());
                    List<Move> moves = selectedWeapon.getMoves();
                    
                    if (!moves.isEmpty()) {
                        combatStrategy.setIsSelected(true);
                        mainFrame.updateAttackArea();
                    }
                    
                    System.out.println("Opened moves for: " + selectedWeapon.getName());
                    mainFrame.repaintMovesPanel();
                }
            }
            
            case KeyEvent.VK_LEFT -> {
                if (combatStrategy.isSelected()) {
                    combatStrategy.setIsSelected(false);
                    
                    if (mainFrame.getMapPanel() != null) {
                        mainFrame.clearAttackArea();
                    }
                    
                    System.out.println("Back to weapon list");
                    mainFrame.repaintMovesPanel();
                }
            }
            
            case KeyEvent.VK_ENTER -> {
                if (combatStrategy.isSelected() && !weapons.get(combatStrategy.getWeaponIndex()).getMoves().isEmpty()) {
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
                        
                        mainFrame.repaintMovesPanel();
                    } else {
                        System.out.println("Move failed! (not enough AP or invalid target)");
                    }
                }
            }
            
            default -> {
                handleFocusInput(e);
            }
        }
    }

    private void handleMapInput(KeyEvent e){
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
                    
                    if (mainFrame.getMovesPanel() != null) {
                        mainFrame.refreshAttackArea();
                    }
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
        
        if (activeElement instanceof Player){ 
            System.out.println(">> PLAYER TURN: " + activeElement.getName());
            mainFrame.setCurrentPlayer((Player)currentElement);
            
            // Verify strategies were created
            System.out.println("MovementStrategy created: " + (mainFrame.getMovementStrategy() != null));
            System.out.println("CombatStrategy created: " + (mainFrame.getCombatStrategy() != null));
            System.out.println("InventoryStrategy created: " + (mainFrame.getInventoryStrategy() != null));
            
        } else if(activeElement instanceof Enemy){
            System.out.println(">> ENEMY TURN: " + activeElement.getName());
            System.out.println("Enemy AI should execute here (not implemented yet)");
            // Enemy AI logic here
            // For now, just skip the enemy turn
            mainFrame.getMap().getConcreteTurnHandler().next();
        }
        
        mainFrame.updateTurnDisplay();
        mainFrame.repaintMapPanel();
        System.out.println("=== UPDATE TURN COMPLETE ===\n");
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

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}