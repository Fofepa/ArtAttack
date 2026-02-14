package com.artattack.inputcontroller;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import com.artattack.enemystrategy.EnemyChoice;
import com.artattack.interactions.InteractionStrategy;
import com.artattack.items.Item;
import com.artattack.level.AreaBuilder;
import com.artattack.level.Maps;
import com.artattack.mapelements.ActiveElement;
import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.EnemyType;
import com.artattack.mapelements.Player;
import com.artattack.moves.Move;
import com.artattack.moves.Weapon;
import com.artattack.turns.TurnListener;
import com.artattack.view.InteractionPanel;
import com.artattack.view.InventoryPanel;
import com.artattack.view.MainFrame;
import com.artattack.view.MapPanel;
import com.artattack.view.MovesPanel;
import com.artattack.view.SkillTreePanel;
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


        if ("DEATH".equals(mainFrame.getCurrentState()) || "GAME_OVER".equals(mainFrame.getCurrentState())) {
            return; 
        }

        // Handle ESC - check skill tree first
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.out.println("ESC pressed");
            
            // If skill tree is open, close it
            if (mainFrame.getMainGUIFacade() != null && 
                "SKILL_TREE".equals(mainFrame.getMainGUIFacade().getCurrentState())) {
                System.out.println("Closing skill tree");
                mainFrame.hideSkillTreePanel();
                mainFrame.focusMapPanel();
                return;
            }
            
            // Otherwise toggle pause
            togglePause();
            return;
        }

        if (mainFrame.isPaused()) {
            System.out.println("Game is paused - input blocked");
            return;
        }
        
        // Route to skill tree if open
        if (mainFrame.getMainGUIFacade() != null && 
            "SKILL_TREE".equals(mainFrame.getMainGUIFacade().getCurrentState())) {
            System.out.println("-> Routing to handleSkillTreeInput (state is SKILL_TREE)");
            handleSkillTreeInput(e);
            return;
        }

        if (mainFrame.getDialogActive()) {
            System.out.println("-> Dialog active, forcing Interaction Input");
            handleInteractionInput(e);
            return; 
        }
        
        // Handle O key to open skill tree (only during player turn)
        if (e.getKeyCode() == KeyEvent.VK_O && currentElement instanceof Player && !isEnemyTurn) {
            Player player = (Player) currentElement;
            if (player.getSkillTree() != null && !player.getSkillTree().isComplete()) {
                System.out.println("Opening skill tree with O key");
                openSkillTree(player);
                return;
            } else if (player.getSkillTree() == null) {
                System.out.println("No skill tree for this player");
            } else {
                System.out.println("Skill tree is complete");
                mainFrame.showDialog(List.of("Skill tree is complete!"));
            }
            return;
        }

        if (e.getKeyCode() == KeyEvent.VK_F ||
            e.getKeyCode() == KeyEvent.VK_I ||
            e.getKeyCode() == KeyEvent.VK_M) {
            handleFocusInput(e);
            return;
        }

        
        if(isEnemyTurn){
            if(e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE){
                continueEnemyTurn();
            }
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
            case KeyEvent.VK_M -> {
                System.out.println("Focusing MapPanel");
                mainFrame.clearAttackArea();
                mainFrame.focusMapPanel();
            }
            case KeyEvent.VK_F -> {
                System.out.println("Focusing WeaponsPanel");
                mainFrame.focusWeaponsPanel();
            }
            case KeyEvent.VK_I -> {
                System.out.println("Focusing InventoryPanel");
                mainFrame.focusInventoryPanel();
                
                if (currentElement instanceof Player && mainFrame.getInventoryStrategy() != null) {
                    setStrategy(mainFrame.getInventoryStrategy()); 
                    updateInventorySelectionDisplay(
                            mainFrame.getInventoryStrategy(),
                            (Player) currentElement
                    );
                }
            }
            default -> {
            }
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
        
            updateInventorySelectionDisplay(inventoryStrategy, player);
        }

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

                if (mainFrame.getDialogActive()) {
                    mainFrame.focusInteractionPanel();
                }
                mainFrame.repaintStatsPanel();
                mainFrame.repaintInventoryPanel();
                mainFrame.repaintTurnOrderPanel();
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

    // skill tree handling
    private void handleSkillTreeInput(KeyEvent e) {
        SkillTreePanel skillTreePanel = mainFrame.getSkillTreePanel();
        if (skillTreePanel == null) {
            System.err.println("ERROR: SkillTreePanel is null");
            return;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP, KeyEvent.VK_W -> {
                System.out.println("Skill Tree: UP");
                skillTreePanel.selectUp();
            }
            case KeyEvent.VK_DOWN, KeyEvent.VK_S -> {
                System.out.println("Skill Tree: DOWN");
                skillTreePanel.selectDown();
            }
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> {
                System.out.println("Skill Tree: LEFT");
                skillTreePanel.selectLeft();
            }
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> {
                System.out.println("Skill Tree: RIGHT");
                skillTreePanel.selectRight();
            }
            case KeyEvent.VK_ENTER, KeyEvent.VK_SPACE -> {
                System.out.println("Skill Tree: CONFIRM");
                boolean success = skillTreePanel.confirmSelection();
                if (success) {
                    // Node unlocked - update UI
                    mainFrame.repaintStatsPanel();
                    mainFrame.repaintWeaponsPanel();
                    mainFrame.repaintMovesPanel();
                    mainFrame.repaintMapPanel();
                    mainFrame.updateTurnDisplay();
                    // Panel stays open even without skill points
                }
            }
            default -> {
                // Ignore other keys
            }
        }
    }

    
    // opens the skillTree for each player
    private void openSkillTree(Player player) {
        if (player == null || player.getSkillTree() == null) {
            System.err.println("ERROR: Cannot open skill tree");
            return;
        }
        
        System.out.println("=== OPENING SKILL TREE ===");
        System.out.println("Available nodes: " + player.getSkillTree().getSupportList().size());

        mainFrame.showSkillTreePanel(player, player.getSkillTree(), (selectedNode) -> {
            if (selectedNode != null) {
                System.out.println(">>> Node unlocked: " + selectedNode.getLabel());
            }
        });

        mainFrame.focusSkillTreePanel();
        System.out.println("=== SKILL TREE OPENED ===");
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
                case KeyEvent.VK_UP, KeyEvent.VK_W -> {
                    if (textFullyRevealed) {
                        mainFrame.selectUp();  
                    }
                }
                case KeyEvent.VK_DOWN, KeyEvent.VK_S -> {
                    if (textFullyRevealed) {
                        mainFrame.selectDown();  
                    }
                }
                
                case KeyEvent.VK_ENTER, KeyEvent.VK_SPACE -> {
                    if (!textFullyRevealed) {
                        mainFrame.skipTextAnimation();   
                    } else {
                        mainFrame.confirmChoice();       
                    }
                }
                default -> handleFocusInput(e);
            }
        } else {
            
            if (e.getKeyCode() == KeyEvent.VK_ENTER /* || e.getKeyCode() == KeyEvent.VK_SPACE */) {
                if (!textFullyRevealed) {
                    mainFrame.skipTextAnimation();
                } else {
                    mainFrame.advanceDialog();

                    if (!mainFrame.getDialogActive()) {
                        if (isEnemyTurn) {
                           
                            System.out.println("Dialog closed during enemy turn -> Continuing execution immediately");
                            continueEnemyTurn();
                        } else {
                            
                            returnToGameplay();
                        }
                    }
                }
            }
        }
    }

    private void returnToGameplay() {
        System.out.println("Dialog finished. Checking context...");

        if (mainFrame.getInteractionPanel() != null) {
            mainFrame.getInteractionPanel().deactivate();
        }
        
        if (currentState instanceof CombatStrategy) {
            System.out.println("Context: Combat -> Returning focus to MovesPanel");
            mainFrame.focusMovesPanel();
            mainFrame.repaintMovesPanel();
        }
        else if (currentState instanceof InventoryStrategy) {
            System.out.println("Context: Inventory -> Returning focus to InventoryPanel");
            mainFrame.focusInventoryPanel();
            mainFrame.repaintInventoryPanel();
        }
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

                    if (mp!= null) {
                        mp.setPlayer((Player) currentElement);
                        mp.setSelectedWeaponIndex(combatStrategy.getWeaponIndex());
                        mp.setActive(true);
                        mainFrame.repaintMovesPanel();
                        mainFrame.updateAttackArea();
                    }

                    mainFrame.focusMovesPanel();
                    mainFrame.updateAttackArea();

                    if (mainFrame.getMapPanel() != null) {
                        mainFrame.getMapPanel().showMoveArea(null, null);
                    }

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
                    combatStrategy.execute(-1, 1);
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
                    combatStrategy.execute(1, 1);
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
                combatStrategy.setMoveIndex(0); 

                if (mainFrame.getMovesPanel()!= null) {
                    mainFrame.getMovesPanel().setActive(false);
                }

                if (mainFrame.getMapPanel() != null) {
                    mainFrame.clearAttackArea();
                }

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


                        mainFrame.repaintMovesPanel();
                        mainFrame.repaintTurnOrderPanel();
                        mainFrame.focusMovesPanel();
                        mainFrame.repaintMovesPanel();
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
        if ("GAME_OVER".equals(mainFrame.getCurrentState())) return;
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
        
        if (dx != 0 || dy != 0) {
            System.out.println("Executing movement: dx=" + dx + ", dy=" + dy);
            movementStrategy.execute(dx, dy);
            mainFrame.repaintMapPanel();
        }
    }

    public void startEnemyTurn(EnemyChoice enemyChoice){
        this.currentEnemyChoice = enemyChoice;
        this.isEnemyTurn = true;

        mainFrame.repaintStatsPanel();
        mainFrame.updateTurnDisplay();
        mainFrame.repaintMapPanel();

        mainFrame.showDialog(List.of("Enemy Turn"));
    
        continueEnemyTurn(); 
    }

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

        if (checkGameOver()) {
            return; 
        }
        
        if(this.currentEnemyChoice.getHasFinished()){// case no more AP
            endEnemyTurn();
        }
    }

    public void endEnemyTurn(){

        if(this.currentElement instanceof Enemy){
            ((Enemy)this.currentElement).setIsStunned(false);
        }
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

        if (checkGameOver()) {
            return;
        }
        System.out.println("ActiveElement: " + (activeElement != null ? activeElement.getName() : "NULL"));
        
        if (activeElement != null) {
            System.out.println("HP: " + activeElement.getCurrHP() + "/" + activeElement.getMaxHP());
            System.out.println("AP: " + activeElement.getActionPoints());
        }
        
        this.currentElement = activeElement;
        
        mainFrame.clearAttackArea();
        mainFrame.clearMovementCursor();

        if (activeElement instanceof Player){ // player turn
            System.out.println(">> PLAYER TURN: " + activeElement.getName());
            
            
            Player player = (Player) activeElement;
            mainFrame.setCurrentPlayer(player);
            mainFrame.focusMapPanel();
            mainFrame.focusMapPanel();
            

            InteractionPanel interactionPanel = mainFrame.getInteractionPanel();
            if (interactionPanel != null) {
                
                interactionPanel.setDefaultPlayerImage(player.getSpritePath());
                
                if (!interactionPanel.isDialogActive()) {
                    interactionPanel.deactivate();
                }
            }
            
            
            // Level up notification (console only, no dialog)
            if(player.getLeveledUp() > 0){
                System.out.println(">>> PLAYER LEVELED UP! Skill points: " + player.getSkillPoints());
                player.setLeveledUp(); // Decrement counter
            }
            

            
            System.out.println("MovementStrategy created: " + (mainFrame.getMovementStrategy() != null));
            System.out.println("CombatStrategy created: " + (mainFrame.getCombatStrategy() != null));
            System.out.println("InventoryStrategy created: " + (mainFrame.getInventoryStrategy() != null));
            
        } else if(activeElement instanceof Enemy c){
            System.out.println(">> ENEMY TURN: " + c.getName());
           
            if(c.getEnemyType() == EnemyType.BOB && c.getCurrHP() < (c.getMaxHP() / 2) && c.getWeapons().get(0).getMoves().size() < 4){
                AreaBuilder ab = new AreaBuilder();
                ab.addShape("circle", 5, true);
                Move bossm4 = new Move();
                bossm4.setName("Steam Wave"); bossm4.setActionPoints(8); bossm4.setPower(7); bossm4.setAreaAttack(true); bossm4.setAttackArea(ab.getResult());
                c.getWeapons().get(0).getMoves().add(bossm4);
            }

            mainFrame.updateMovementCursor(c.getCoordinates());

            EnemyChoice enemyChoice = new EnemyChoice(this.mainFrame);
            enemyChoice.setMap(mainFrame.getMap());
            enemyChoice.setEnemy((Enemy) currentElement);

            // checks if the enemy is stunned otherwise it continues with the turn
            if(((Enemy)currentElement).isStunned()){
                System.out.println("Enemy was stunned!");
                
                this.currentEnemyChoice = enemyChoice;
                this.currentEnemyChoice.setHasFinished();   
                this.isEnemyTurn = true;
                
                mainFrame.showDialog(List.of("The enemy is stunned! He is now taking a short break!"));
            }
            else{
                startEnemyTurn(enemyChoice);
            }  
        }
        
        mainFrame.updateTurnDisplay();
        mainFrame.repaintMapPanel();
        System.out.println("=== UPDATE TURN COMPLETE ===\n");
    }

    public void togglePause(){
        if(mainFrame.isPauseMenuVisible()){
            System.out.println("Resuming game");
            mainFrame.hidePauseMenu();
            mainFrame.focusMapPanel();
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

        
        List<Item> inventory = player.getInventory();
        int idx = strategy.getInventoryIndex();

        if (idx >= 0 && idx < inventory.size()) {
            Item selectedItem = inventory.get(idx);
            mainFrame.updateItemDetails(selectedItem);
        } else {
            mainFrame.updateItemDetails(null);
        }
    }


    private boolean checkGameOver() {
        if (mainFrame == null || mainFrame.getMap() == null) return false;

        Maps map = mainFrame.getMap();
        Player p1 = map.getPlayerOne();
        Player p2 = map.getPlayerTwo();

        if (p1 != null && p2 != null) {
            if (!p1.isAlive() && !p2.isAlive()) {
                System.out.println("!!! GAME OVER TRIGGERED: Both players are down !!!");
                
                if (mainFrame.getMainGUIFacade() != null) {
                    mainFrame.getMainGUIFacade().showDeathScreen();
                    return true; 
                }
            }
        }
        return false;
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {

        if (currentState == null) {
            return;
        }

        String s ;
        s = switch (currentState.getType()) {
            case 0 -> "MovementStrategy";
            case 1 -> "CombatStrategy";
            case 2 -> "InventoryStrategy";
            default -> "Unknown";
        };
        System.out.println(e.getKeyChar() + " has been pressed in " + s);
    }
}