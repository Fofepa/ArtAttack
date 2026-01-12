package com.artattack.inputcontroller;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import com.artattack.interactions.InteractionStrategy;
import com.artattack.mapelements.ActiveElement;
import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.Player;
import com.artattack.moves.Move;
import com.artattack.moves.Weapon;
import com.artattack.turns.TurnListener;
import com.artattack.view.GamePanel;
import com.artattack.view.InteractionPanel;
import com.artattack.view.MainFrame;
import com.artattack.view.MovesPanel;


public class InputController implements KeyListener, TurnListener {
    private PlayerStrategy currentState;
    private ActiveElement currentElement;
    private MainFrame mainFrame; 

    public InputController(MainFrame mainFrame){
        this.mainFrame = mainFrame;
    }
    


    @Override
    public void keyPressed(KeyEvent e){
        Component focused = mainFrame.getFocused();

        if(isMapPanelFocused(focused)){
            handleMapInput(e);
        }else if(isMovesPanelFocused(focused)){
            handleMovesInput(e);
        }else if(isInteractionPanelFocused(focused)){
            handleInteractionInput(e);
        }
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
        Component current = component;

        while (current != null) {
            if (current instanceof GamePanel) {
                return true;
            }
            current = current.getParent();
        }
        return false;
    }

    public void handleFocusInput(KeyEvent e){
        switch(e.getKeyCode()){
            case KeyEvent.VK_M:
                mainFrame.focusMapPanel();
                break;
            case KeyEvent.VK_F:
                mainFrame.focusMovesPanel();
                break;
            /* case KeyEvent.VK_I:
                mainFrame.focusInventoryPanel(); */
            /* case KeyEvent.VK_T:
                mainFrame.focusInteractionPanel(); */
            default:
                break;
        }
    }

    private void handleInteractionInput(KeyEvent e){
        boolean dialogActive = mainFrame.getDialogActive();

        if (!dialogActive) {
            return;
        }

        boolean choiceMode = mainFrame.getChoiceMode();
        boolean textFullyRevealed = mainFrame.getTextFullyRevealed();
        if (choiceMode) {
            
            int selectedOption = mainFrame.getSelectedOption();
            List<String> responseOptions = mainFrame.getResponseOptions();
            
                    switch (e.getKeyCode()) {
                    
                        case KeyEvent.VK_UP:
                        case KeyEvent.VK_W:
                            if (textFullyRevealed && selectedOption > 0) {
                                selectedOption--;
                                mainFrame.repaintInteractionPanel();
                            }
                            break;
                        
                        case KeyEvent.VK_DOWN:
                        case KeyEvent.VK_S:
                            if (textFullyRevealed && selectedOption < responseOptions.size() - 1) {
                                selectedOption++;
                                mainFrame.repaintInteractionPanel();
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
                    
                }

                else {
                    // Simple dialog mode
                    if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) {
                        if (!textFullyRevealed) {
                            // Skip animation - reveal all text immediately
                            mainFrame.skipTextAnimation();
                        } else {
                            // Text is fully revealed - advance to next phrase
                            mainFrame.advanceDialog();
                        }
                    }
                }

        
        
    }

    



    private void handleMovesInput(KeyEvent e){
        setStrategy(new CombatStrategy(this.mainFrame.getMap(), (Player) currentElement));
        
        CombatStrategy combatStrategy = (CombatStrategy) currentState;

        int selectedWeaponIndex = 0;
        int selectedMoveIndex = 0;
        boolean isInMoveSelection = false;

        List<Weapon> weapons = currentElement.getWeapons();
        


        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> {
                if (!isInMoveSelection) {
                    // Go up on the list
                    selectedWeaponIndex = Math.max(0, selectedWeaponIndex - 1);
                    
                    //  COMBAT STRATEGY CALL - Weapon selection (dy = 0)
                    combatStrategy.execute(selectedWeaponIndex, 0);
                    
                    System.out.println("Selected weapon: " + weapons.get(selectedWeaponIndex).getName());
                } else {
                    // Go down on the list
                    if (!weapons.get(selectedWeaponIndex).getMoves().isEmpty()) {
                        selectedMoveIndex = Math.max(0, selectedMoveIndex - 1);
                        
                        //  COMBAT STRATEGY - Move selection (dy = index + 1)
                        combatStrategy.execute(selectedWeaponIndex, selectedMoveIndex + 1);
                        
                        mainFrame.updateAttackArea();
                        System.out.println("Selected move: " + weapons.get(selectedWeaponIndex).getMoves().get(selectedMoveIndex).getName());
                    }
                }
                mainFrame.repaintMovesPanel();
            }
            
            case KeyEvent.VK_DOWN -> {
                if (!isInMoveSelection) {
                    // Go up on the list
                    selectedWeaponIndex = Math.min(weapons.size() - 1, selectedWeaponIndex + 1);
                    
                    // COMBATSTRATEGY CALL - Weapon selection (dy = 0)
                    combatStrategy.execute(selectedWeaponIndex, 0);
                    
                    System.out.println("Selected weapon: " + weapons.get(selectedWeaponIndex).getName());
                } else {
                    // Go down on the list
                    if (!weapons.get(selectedWeaponIndex).getMoves().isEmpty()) {
                        selectedMoveIndex = Math.min(weapons.get(selectedWeaponIndex).getMoves().size() - 1, selectedMoveIndex + 1);
                        
                        //  COMBATSTRATEGY CALL - Moves selection (dy = index + 1)
                        combatStrategy.execute(selectedWeaponIndex, selectedMoveIndex + 1);
                        
                        mainFrame.updateAttackArea();
                        System.out.println("Selected move: " + weapons.get(selectedWeaponIndex).getMoves().get(selectedMoveIndex).getName());
                    }
                }
                mainFrame.repaintMovesPanel();
            }
            
            case KeyEvent.VK_RIGHT -> {
                if (!isInMoveSelection && !weapons.isEmpty()) {
                    // Entering the moves selection
                    Weapon selectedWeapon = weapons.get(selectedWeaponIndex);
                    List<Move> moves = selectedWeapon.getMoves();
                    selectedMoveIndex = 0;
                    isInMoveSelection = true;
                    
                    if (!moves.isEmpty()) {
                        // Start with the first move
                        combatStrategy.execute(selectedWeaponIndex, 1); // dy = 1 per la prima mossa
                        mainFrame.updateAttackArea();
                    }
                    
                    System.out.println("Opened moves for: " + selectedWeapon.getName());
                    mainFrame.repaintMovesPanel();
                }
            }
            
            case KeyEvent.VK_LEFT -> {
                if (isInMoveSelection) {
                    // Go back to the Weapon selection
                    isInMoveSelection = false;
                    selectedMoveIndex = 0;
                    
                    // Reset strategy index for the weapons
                    combatStrategy.execute(selectedWeaponIndex, 0);
                    
                    if (mainFrame.getMapPanel() != null) {
                        mainFrame.clearAttackArea();
                    }
                    
                    System.out.println("Back to weapon list");
                    mainFrame.repaintMovesPanel();
                }
            }
            
            case KeyEvent.VK_ENTER -> {
                if (isInMoveSelection && !weapons.get(selectedWeaponIndex).getMoves().isEmpty()) {
                    //  MOVE USAGE - calls acceptMove()
                    System.out.println("=== Using Move ===");
                    System.out.println("Weapon: " + weapons.get(selectedWeaponIndex).getName());
                    System.out.println("Move: " + weapons.get(selectedWeaponIndex).getMoves()
                                        .get(selectedMoveIndex).getName());
                    
                    int result = combatStrategy.acceptMove();
                    
                    if (result != 0) {
                        System.out.println("✓ Move executed successfully! Damage: " + result);
                        
                        // After the move is been used clears all
                        isInMoveSelection = false;
                        selectedWeaponIndex = 0;
                        selectedMoveIndex = 0;
                        
                        if (mainFrame.getMapPanel() != null) {
                            mainFrame.clearAttackArea();
                            mainFrame.repaintMapPanel(); // Updates the map
                        }
                        
                        mainFrame.repaintMovesPanel();
                        
                    } else {
                        System.out.println("Move failed! (not enough AP or invalid target)");
                    }
                }
            }
            default ->{
                        handleFocusInput(e);
                        break;
                    }
        }
        
    }


    private void handleMapInput(KeyEvent e){
        setStrategy(new MovementStrategy(this.mainFrame.getMap(), (Player) currentElement));

        MovementStrategy movementStrategy = (MovementStrategy) currentState;
        InteractionStrategy interactionStrategy = new InteractionStrategy(movementStrategy);

        int dx = 0, dy = 0;

        switch (e.getKeyCode()) {
                    case java.awt.event.KeyEvent.VK_UP, java.awt.event.KeyEvent.VK_W -> {
                        dy = -1;
                        System.out.println("UP pressed");
                    }
                    case java.awt.event.KeyEvent.VK_DOWN, java.awt.event.KeyEvent.VK_S -> {
                        dy = 1;
                        System.out.println("DOWN pressed");
                    }
                    case java.awt.event.KeyEvent.VK_LEFT, java.awt.event.KeyEvent.VK_A -> {
                        dx = -1;
                        System.out.println("LEFT pressed");
                    }
                    case java.awt.event.KeyEvent.VK_RIGHT, java.awt.event.KeyEvent.VK_D -> {
                        dx = 1;
                        System.out.println("RIGHT pressed");
                    }

                    case java.awt.event.KeyEvent.VK_ENTER -> {
                        System.out.println("ENTER pressed - Confirming movement");
                        if (movementStrategy.getSelectedState()) {
                            movementStrategy.acceptMovement();

                            // Update attackArea after player movement
                            if (mainFrame.getMovesPanel() != null) {
                                mainFrame.refreshAttackArea();
                            }
                            mainFrame.repaintStatsPanel();
                            mainFrame.updateTurnDisplay();
                            //endTurn();
                            mainFrame.repaintMapPanel();
                        }
                        return;
                    }

                    case java.awt.event.KeyEvent.VK_E -> {
                        System.out.println("E pressed - Interaction");
                        if (movementStrategy.getSelectedState()) {
                            interactionStrategy.acceptInteraction();
                        }
                        return;
                    }
                    
                    case java.awt.event.KeyEvent.VK_SPACE -> {
                        System.out.println("SPACE pressed - Skip turn");
                        //endTurn(); 
                        mainFrame.getMap().getConcreteTurnHandler().next();
                        return;
                    }
                    default ->{
                        handleFocusInput(e);
                        break;
                    }

        }
    }
        
    
    
    public void setStrategy(PlayerStrategy strategy){
        this.currentState = strategy;
    }

    @Override
    public void updateTurn(ActiveElement activeElement){

        System.out.println("===== UPDATING TURN ====");

        this.currentElement =  activeElement;
        if (activeElement instanceof Player){ // The next activeElement on the queue is a Player
            mainFrame.setCurrentPlayer((Player)currentElement);
        }
        else if(activeElement instanceof Enemy){
            
        }
        
        mainFrame.updateTurnDisplay();

        mainFrame.repaintMapPanel();
    }

    // unused for now
    @Override
    public void keyReleased(KeyEvent e) {}


    // unused for now
    @Override
    public void keyTyped(KeyEvent e) {}
}