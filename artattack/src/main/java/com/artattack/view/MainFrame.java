package com.artattack.view;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import com.artattack.interactions.Talk;
import com.artattack.level.AreaBuilder;
import com.artattack.level.Coordinates;
import com.artattack.level.MapBuilder;
import com.artattack.level.Maps;
import com.artattack.level.TestMapBuilder;
import com.artattack.mapelements.ActiveElement;
import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.EnemyType;
import com.artattack.mapelements.InteractableElement;
import com.artattack.mapelements.MovieDirector;
import com.artattack.mapelements.Musician;
import com.artattack.mapelements.Player;
import com.artattack.moves.Move;
import com.artattack.moves.Weapon;
import com.artattack.turns.ConcreteTurnHandler;
import com.artattack.turns.ConcreteTurnQueue;

public class MainFrame extends JFrame {
    
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private MenuPanel menuPanel;
    private GamePanel gamePanel;

    private Maps initialMap;
    
    
    private ConcreteTurnQueue turnQueue;
    private ConcreteTurnHandler turnHandler;
    
    public static final String MENU_CARD = "MENU";
    public static final String GAME_CARD = "GAME";
    
    public MainFrame() {

        this.initialMap = null;
        
        setTitle("Art Attack");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        
        menuPanel = new MenuPanel(this);
        gamePanel = new GamePanel(this);
        
        
        mainPanel.add(menuPanel, MENU_CARD);
        mainPanel.add(gamePanel, GAME_CARD);
        
        add(mainPanel);
        
        
        showMenu();
    }
    
    public void showMenu() {
        cardLayout.show(mainPanel, MENU_CARD);
        menuPanel.requestFocusInWindow();
    }
    
    public void showGame() {
        cardLayout.show(mainPanel, GAME_CARD);
        SwingUtilities.invokeLater(() -> {
            

            if (initialMap== null) {
                gamePanel.loadInitialMap();
            }else{
                loadInitialMap(initialMap);
            }
            
            gamePanel.requestFocusInWindow();
            
            
            Timer initTimer = new Timer(100, e -> {
                initializeTurnSystem();
                ((Timer)e.getSource()).stop();
            });
            initTimer.setRepeats(false);
            initTimer.start();
        });
    }


    // FACADE - MAP PANEL AND MAP MANAGER
    public void loadInitialMap(Maps map){
        if(map == null){
            System.err.println("Attempted to load null map");
            return;
        }

        System.out.println("Loading map (FACADE)");
        gamePanel.loadInitialMapFacade(map);

        /* //Turn System initialization
        Timer initTimer = new Timer(100,e-> {
            initializeTurnSystemFromMap(map);
            ((Timer)e.getSource()).stop();
        });
        initTimer.setRepeats(false);
        initTimer.start(); */
    }


    //FACADE - DIALOGUES

    public void showDialog(List<String> phrases){
        showDialog(phrases, null); 
    }


    public void showDialog(List<String> phrases, Runnable onFinished){
        if(phrases == null || phrases.isEmpty()){
            System.err.println(" Attempted to show empty dialog");
            return;
        }

        System.out.println("Showing Dialog (FACADE)");
        gamePanel.showDialog(phrases, onFinished);
    }

    public void showDialogWithChoice(String question, List<String> options, InteractionPanel.DialogCallback callback){
        if (question == null || options == null || options.isEmpty()) {
            System.err.println(" Invalid dialog with choice parameters");
            return;
        }
        System.out.println("Dialogue with choice (FACADE)");
        gamePanel.showDialogWithChoice(question,options,callback);
    }

    public boolean isDialogActive(){
        return gamePanel.isDialogActive();
    }

    public void skipTextAnimation(){
        gamePanel.getInteractionPanel().skipTextAnimation();
    }

    public void confirmChoice(){
        gamePanel.getInteractionPanel().confirmChoice();
    }

    public void advanceDialogue(){
        gamePanel.getInteractionPanel().advanceDialogue();
    }

    public void closeDialogue(){
        gamePanel.getInteractionPanel().closeDialogue();
    }


    //FACADE SPRITE

    public void showSprite(String imagePath){
        if(imagePath == null || imagePath.isEmpty()){
            System.err.println("Invalid sprite path");
            return;
        } 

        System.out.println("Showing Sprite (FACADE)");
        gamePanel.showSprite(imagePath);
    }


    public void clearSprite() {
        gamePanel.clearSprite();
    }

    // FACADE Focus

    public void focusMapPanel(){
        gamePanel.getMapPanel().giveFocus();
    }

    public void  focusInteractionPanel(){
        gamePanel.getInteractionPanel().giveFocus();
    }

    public void focusMovesPanel(){
        gamePanel.getMovesPanel().giveFocus();
    }

    public void focusInventoryPanel(){
        // TODO: add invetoryPanel
    }

    public JPanel getFocused(){

    }


    //FACADE Repaint

    public void repaintMapPanel(){
        gamePanel.getMapPanel().repaint();
    }

    public void repaintMovesPanel(){
        gamePanel.getMovesPanel().repaint();
    }

    public void repaintTurnPanel(){
        gamePanel.getTurnPanel().repaint();
    }

    public void repaintStatsPanel(){
        gamePanel.getStatsPanel.repaint();
    }



    //FACADE  attackArea

    public void updateAttackArea(){
    gamePanel.getMovesPanel().updateAttackArea();
    }

    public void clearAttackArea(){
        gamePanel.getMapPanel().clearAttackArea();
    }


        //Update attackArea after player movement
    public void refreshAttackArea(){
        gamePanel.getMovesPanel().refreshAttackArea();
    }


    //FACADE turns

    public void updateTurnDisplay(ActiveElement activeElement){
        gamePanel.getTurnPanel().updateTurnDisplay();
    }

    //Set Player for MapPanel StatsPanel and MovesPanel
    public void setCurrentPlayer(Player player){
        gamePanel.setCurrentPlayer(player);
    }

    //Turn System

    /* private void initializeTurnSystem() {
        
        List<ActiveElement> activeElements = initialMap.getConcreteTurnHandler().getConcreteTurnQueue().getTurnQueue();
        
        if (activeElements == null || activeElements.isEmpty()) {
            System.out.println(" No active elements found for turn system");
            return;
        }
        
        System.out.println("=== Initializing Turn System in MainFrame ===");
        System.out.println("Active elements: " + activeElements.size());
        for (ActiveElement elem : activeElements) {
            System.out.println("  - " + elem.getName() + 
                             " (SPD: " + elem.getSpeed() + 
                             ", Symbol: " + elem.getMapSymbol() + ")");
        }
        
        if (turnHandler.hasNext()) {
            ActiveElement first = turnHandler.next();
            System.out.println("✓ First turn: " + first.getName());
        }
        
        
        gamePanel.setTurnSystem(initialMap.getConcreteTurnHandler().getConcreteTurnQueue(), initialMap.getConcreteTurnHandler());
        
        System.out.println("✓ Turn system initialized successfully");
    }



    private void initializeTurnSystemFromMap(Maps map){
        List<ActiveElement> activeElements = new ArrayList<>();

        activeElements.add(map.getPlayerOne());
        activeElements.add(map.getPlayerTwo());
        activeElements.addAll(map.getEnemies());

        if(activeElements.isEmpty()){
            System.out.println("No active elements in map");
            return;
        }

        System.out.println("Initializing Turn  System From Map (FACADE)");
        
        turnQueue = new ConcreteTurnQueue(activeElements);
        turnHandler = (ConcreteTurnHandler) turnQueue.createTurnHandler();

        if (turnHandler.hasNext()) {
            ActiveElement first = turnHandler.next();
            System.out.println("✓ First turn: " + first.getName());
        }
        
        
        gamePanel.setTurnSystem(turnQueue, turnHandler);
        
        System.out.println("✓ Turn system initialized successfully");
    }
    
    
    public void resetTurnSystem() {
        if (turnHandler != null) {
            turnHandler.resetIndex();
            System.out.println("Turn system reset");
        }
        
        
        if (turnQueue != null && !turnQueue.getTurnQueue().isEmpty()) {
            if (turnHandler.hasNext()) {
                turnHandler.next();
            }
        }
    }
    
    
    public ConcreteTurnQueue getTurnQueue() {
        return turnQueue;
    }
    
    
    public ConcreteTurnHandler getTurnHandler() {
        return turnHandler;
    } */


    public void setInitialMap(Maps initialMap) {
        this.initialMap = initialMap;
    }
    
    
    public static void main(String[] args) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Could not set Nimbus L&F.");
        }

        AreaBuilder areaBuilder = new AreaBuilder();
        areaBuilder.addShape("8");
        List<Coordinates> area8 = areaBuilder.getResult();
        areaBuilder.addShape("diamond", 4, true);
        List<Coordinates> bigArea = areaBuilder.getResult();
        MapBuilder mapBuilder = new TestMapBuilder();
        mapBuilder.setPlayerOne(new Musician(1, '@', "Zappa", new Coordinates(3, 3), List.of(new Weapon("Hoe", "", 0)), 5,5, bigArea, 20, 20, 0, 20, 1, 5, 2, null, null, null));
        mapBuilder.setPlayerTwo(new MovieDirector(0, '@', "Lynch", new Coordinates(5, 5), List.of(new Weapon("Hoe", "", 0)), 5,5, bigArea, 20, 20, 0, 20, 1, 5, 2, null, null, null));
        mapBuilder.setInteractableElements(List.of(
            new InteractableElement(0, '$', "Guitar", new Coordinates(10, 10),List.of(new Talk(new InteractionPanel(), List.of("Hi!"))), "",null,null),
            new InteractableElement(1, '$', "Drums", new Coordinates(15, 15),List.of(new Talk(new InteractionPanel(), List.of("Haloa!"))), "",null,null)));
        Move m1 = new Move(); m1.setName("Kick"); m1.setPower(3); m1.setAttackArea(bigArea); m1.setActionPoints(3);
        Move m2 = new Move(); m2.setName("Bump"); m2.setPower(5); m2.setAttackArea(area8); m2.setActionPoints(4);
        Weapon enemyWeapon = new Weapon(" ", " ", List.of(m1,m2), 0);
        mapBuilder.setEnemies(List.of(
            new Enemy(0, 'E', "Goblinstein", new Coordinates(10, 10),EnemyType.ROBOT, 20,20,3,List.of(enemyWeapon),15,5,area8, bigArea,null,null,0)
         ));
        mapBuilder.setDimension(36, 150);
        mapBuilder.setDict();
        mapBuilder.setTurnQueue();
        mapBuilder.startMap();
        Maps map = mapBuilder.getResult();
        map.getPlayerOne().getWeapons().get(0).addMove(m1);
        map.getPlayerOne().getWeapons().get(0).addMove(m2);
        map.getPlayerTwo().getWeapons().get(0).addMove(m1);
        map.getPlayerTwo().getWeapons().get(0).addMove(m2);
        
        
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setInitialMap(map);
            
            frame.setVisible(true);
            
            // menu cursor timer
            Timer timer = new Timer(500, e -> frame.menuPanel.repaint());
            timer.start();
        });
    }

    
}