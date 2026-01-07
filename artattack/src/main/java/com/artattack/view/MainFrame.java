package com.artattack.view;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import com.artattack.ActiveElement;
import com.artattack.ConcreteTurnHandler;
import com.artattack.ConcreteTurnQueue;
import com.artattack.Maps;

public class MainFrame extends JFrame {
    
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private MenuPanel menuPanel;
    private GamePanel gamePanel;
    
    
    private ConcreteTurnQueue turnQueue;
    private ConcreteTurnHandler turnHandler;
    
    public static final String MENU_CARD = "MENU";
    public static final String GAME_CARD = "GAME";
    
    public MainFrame() {
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
            
            gamePanel.loadInitialMap();
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

        //Turn System initialization
        Timer initTimer = new Timer(100,e-> {
            initializeTurnSystemFromMap(map);
            ((Timer)e.getSource()).stop();
        });
        initTimer.setRepeats(false);
        initTimer.start();
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


    
    //Turn System

    private void initializeTurnSystem() {
        
        List<ActiveElement> activeElements = gamePanel.getActiveElements();
        
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
        
        
        turnQueue = new ConcreteTurnQueue(activeElements);
        turnHandler = (ConcreteTurnHandler) turnQueue.createTurnHandler();
        
        
        if (turnHandler.hasNext()) {
            ActiveElement first = turnHandler.next();
            System.out.println("✓ First turn: " + first.getName());
        }
        
        
        gamePanel.setTurnSystem(turnQueue, turnHandler);
        
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
        
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
            
            // menu cursor timer
            Timer timer = new Timer(500, e -> frame.menuPanel.repaint());
            timer.start();
        });
    }
}