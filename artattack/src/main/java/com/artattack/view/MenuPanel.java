package com.artattack.view;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import com.artattack.level.*;
import com.artattack.mapelements.*;
import com.artattack.items.*;
import com.artattack.moves.*;

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
        logoPanel.setPreferredSize(new Dimension(800, 250));

        
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
            System.out.println("Starting new game...");
            startNewGameWithTestData();
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
            // Build the map using your MapBuilder pattern
            MapBuilder builder = new TestMapBuilder();
            
            // Create players (adjust based on your actual Player constructors)
            AreaBuilder areaBuilder = new AreaBuilder();
            areaBuilder.addShape("8");
            List<Coordinates> moveArea = areaBuilder.getResult();
            List<Item> items = new ArrayList<>();
            items.add(new Cure("Potion", " ", 10));
            items.add(new Cure("SuperPotion", " ", 2));
            items.add(new Cure("IperPotion", "Sex on the beach ", 1));
            Player playerOne = new Musician(
                1, '@', "Zappa", 
                new Coordinates(2, 2), 
                List.of(new Weapon("Guitar", "A musical weapon", 10)), 
                5, 5, moveArea, 19, 20, 0, 20, 1, 5, 2, items, null, null
            );
            Player playerTwo = new MovieDirector(
                2, '@', "Lynch", 
                new Coordinates(5, 5),
                List.of(new Weapon("Camera", "A cinematic weapon", 10)), 
                5, 5, moveArea, 20, 20, 0, 20, 1, 5, 2, items, null, null
            );
            
            // Create enemies (optional)
            Enemy enemy = new Enemy(
                3, 'E', "Guard", 
                new Coordinates(10, 10),
                EnemyType.GUARD, 20, 20, 3,
                null, 5, 5, moveArea, 
                moveArea, null, null, 0
            );
            
            // Build the map
            builder.setDimension(40, 140);
            builder.setPlayerOne(playerOne);
            builder.setPlayerTwo(playerTwo);
            builder.setEnemies(List.of(enemy));
            builder.setInteractableElements(new java.util.ArrayList<>());
            builder.setDict();
            builder.setTurnQueue();
            builder.startMap();
            
            Maps map = builder.getResult();
            
            // Start the game through the facade
            mainFacade.startNewGame(map, playerOne, playerTwo);
            
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
        button.setFont(new Font("Monospaced", Font.BOLD, 18));
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
        int drawWidth = (int) (panelWidth * 0.6);
        int drawHeight = (int) (drawWidth / ratio);

        // center the image
        int x = (panelWidth - drawWidth) / 2;
        int y = (panelHeight - drawHeight) / 2;

        g2.drawImage(logo, x, y, drawWidth, drawHeight, this);
    }
}