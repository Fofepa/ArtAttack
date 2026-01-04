package com.artattack.view;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class MainFrame extends JFrame {
    
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private MenuPanel menuPanel;
    private GamePanel gamePanel;
    
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
            gamePanel.loadInitialMap();
            gamePanel.requestFocusInWindow();
        });
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