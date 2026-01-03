package com.artattack.view;

import java.awt.CardLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import com.artattack.ActiveElement;
import com.artattack.ConcreteTurnHandler;
import com.artattack.ConcreteTurnQueue;

public class MainFrame extends JFrame {
    
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private MenuPanel menuPanel;
    private GamePanel gamePanel;
    
    // Sistema di turni - gestito dal MainFrame come facade
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
        
        // Crea il CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Crea i pannelli
        menuPanel = new MenuPanel(this);
        gamePanel = new GamePanel(this);
        
        // Aggiungi i pannelli al CardLayout
        mainPanel.add(menuPanel, MENU_CARD);
        mainPanel.add(gamePanel, GAME_CARD);
        
        add(mainPanel);
        
        // Mostra il menu all'inizio
        showMenu();
    }
    
    public void showMenu() {
        cardLayout.show(mainPanel, MENU_CARD);
        menuPanel.requestFocusInWindow();
    }
    
    public void showGame() {
        cardLayout.show(mainPanel, GAME_CARD);
        SwingUtilities.invokeLater(() -> {
            // Prima carica la mappa
            gamePanel.loadInitialMap();
            gamePanel.requestFocusInWindow();
            
            // Poi inizializza il sistema di turni con un piccolo delay
            // per assicurarsi che tutto sia caricato
            Timer initTimer = new Timer(100, e -> {
                initializeTurnSystem();
                ((Timer)e.getSource()).stop();
            });
            initTimer.setRepeats(false);
            initTimer.start();
        });
    }
    
    /**
     * Inizializza il sistema di turni con tutti gli elementi attivi del gioco.
     * Questo metodo viene chiamato dopo che la mappa è stata caricata.
     */
    private void initializeTurnSystem() {
        // Ottieni tutti gli elementi attivi dalla mappa caricata
        List<ActiveElement> activeElements = gamePanel.getActiveElements();
        
        if (activeElements == null || activeElements.isEmpty()) {
            System.out.println("⚠️ No active elements found for turn system");
            return;
        }
        
        System.out.println("=== Initializing Turn System in MainFrame ===");
        System.out.println("Active elements: " + activeElements.size());
        for (ActiveElement elem : activeElements) {
            System.out.println("  - " + elem.getName() + 
                             " (SPD: " + elem.getSpeed() + 
                             ", Symbol: " + elem.getMapSymbol() + ")");
        }
        
        // Crea la coda dei turni
        turnQueue = new ConcreteTurnQueue(activeElements);
        turnHandler = (ConcreteTurnHandler) turnQueue.createTurnHandler();
        
        // Inizia il primo turno
        if (turnHandler.hasNext()) {
            ActiveElement first = turnHandler.next();
            System.out.println("✓ First turn: " + first.getName());
        }
        
        // Passa il sistema di turni al GamePanel che lo distribuirà
        gamePanel.setTurnSystem(turnQueue, turnHandler);
        
        System.out.println("✓ Turn system initialized successfully");
    }
    
    /**
     * Resetta il sistema di turni (utile per restart del gioco)
     */
    public void resetTurnSystem() {
        if (turnHandler != null) {
            turnHandler.resetIndex();
            System.out.println("Turn system reset");
        }
        
        // Re-inizializza se necessario
        if (turnQueue != null && !turnQueue.getTurnQueue().isEmpty()) {
            if (turnHandler.hasNext()) {
                turnHandler.next();
            }
        }
    }
    
    /**
     * Ritorna la coda dei turni
     */
    public ConcreteTurnQueue getTurnQueue() {
        return turnQueue;
    }
    
    /**
     * Ritorna il gestore dei turni
     */
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
            
            // Timer per il repaint del cursore lampeggiante del menu
            Timer timer = new Timer(500, e -> frame.menuPanel.repaint());
            timer.start();
        });
    }
}