package com.artattack.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.artattack.level.MapBuilder;
import com.artattack.level.MapDirector;
import com.artattack.level.MapManager;
import com.artattack.level.Maps;
import com.artattack.level.TutorialMapBuilder;

/**
 * Facade for managing menu screens
 */
public class MenuPanel {
    private JPanel menuPanel;
    private MainGUIFacade mainFacade;
    
    public MenuPanel(MainGUIFacade mainFacade) {
        this.mainFacade = mainFacade;
        initializeMenuPanel();
    }
    
    private void initializeMenuPanel() {
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(Color.BLACK);
        
        // Logo
        LogoPanel logoPanel = new LogoPanel();
        logoPanel.setPreferredSize(new Dimension(800, 500));
        logoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));

        
        // Buttons
        JButton newGameBtn = createMenuButton("New Game");
        JButton loadGameBtn = createMenuButton("Load Game");
        JButton settingsBtn = createMenuButton("Settings");
        JButton exitBtn = createMenuButton("Exit");
        
        // Add spacing
        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(logoPanel);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 0)));
        menuPanel.add(newGameBtn);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(loadGameBtn);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(settingsBtn);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(exitBtn);
        menuPanel.add(Box.createVerticalGlue());
        
        // Button actions
        newGameBtn.addActionListener(e -> {
            System.out.println("Go to character selection...");
            mainFacade.showCharacterSelection();
        });
        
        loadGameBtn.addActionListener(e -> {
            System.out.println("Load game not implemented yet");
        });
        
        settingsBtn.addActionListener(e -> {
            System.out.println("Settings not implemented yet");
        });
        
        exitBtn.addActionListener(e -> mainFacade.exitGame());
    }
    
    /**
     * Creates and starts a new game with test data
     * Replace this with your actual game setup logic
     */
    private void startNewGameWithTestData() {
        try {
        MapBuilder builder = new TutorialMapBuilder();
        MapDirector director = new MapDirector(builder);

        director.make("Tutorial");
        Maps map = builder.getResult();

        director.make("1");
        Maps map2 = builder.getResult();
        
        MapManager mapManager = new MapManager(new HashMap<Integer, Maps>(), map.getID());
        mapManager.getLevels().put(map.getID(), map);
        mapManager.getLevels().put(map2.getID(), map2);

        System.out.println(map.getID());
        System.out.println(map2.getID());

        mainFacade.startNewGame(mapManager, map.getPlayerOne(), map.getPlayerTwo());
            
        } catch (Exception ex) {
            System.err.println("Error starting game:");
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                menuPanel, 
                "Error starting game: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(300, 50));
        button.setFont(new Font("Courier New", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(30, 30, 30));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(50, 50, 50));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(30, 30, 30));
            }
        });
        
        return button;
    }
    
    public JPanel getMenuPanel() {
        return menuPanel;
    }
}


class LogoPanel extends JPanel {

    private final Image logo;

    public LogoPanel() {
        setOpaque(false); 
        logo = new ImageIcon(
            getClass().getResource("/images/logo.png")
        ).getImage();
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (logo == null) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(
            RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BILINEAR
        );

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        int imgWidth = logo.getWidth(this);
        int imgHeight = logo.getHeight(this);

        // original rapport
        double ratio = (double) imgWidth / imgHeight;

        // width = 60% of the panel
        int drawWidth = (int) (panelWidth * 0.4);
        int drawHeight = (int) (drawWidth / ratio);

        // center the image
        int x = (panelWidth - drawWidth) / 2;
         int y = 1;

        g2.drawImage(logo, x, y, drawWidth, drawHeight, this);
    }
}