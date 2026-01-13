package com.artattack.view;

import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import com.artattack.inputcontroller.InputController;
import com.artattack.items.Cure;
import com.artattack.view.*;
import com.artattack.mapelements.Player;
import com.artattack.moves.Weapon;
import com.artattack.level.Maps;
import com.artattack.level.MapBuilder;
import com.artattack.level.TestMapBuilder;
import com.artattack.mapelements.*;
import com.artattack.level.*;


/**
 * Main Facade that coordinates all GUI components and sub-facades
 */
public class MainGUIFacade {
    private JFrame mainFrame;
    private InputController inputController;
    private GameFacade gameFacade;
    private MenuFacade menuFacade;
    
    private String currentState = "MENU"; // MENU, GAME, PAUSE
    
    public MainGUIFacade() {
        initializeMainFrame();
        initializeFacades();
    }
    
    private void initializeMainFrame() {
        mainFrame = new JFrame("Art Attack");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1920, 1080);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.getContentPane().setBackground(Color.BLACK);
    }
    
    private void initializeFacades() {
        menuFacade = new MenuFacade(this);
    }
    
    public void showMenu() {
        currentState = "MENU";
        mainFrame.getContentPane().removeAll();
        mainFrame.add(menuFacade.getMenuPanel(), BorderLayout.CENTER);
        mainFrame.revalidate();
        mainFrame.repaint();
    }
    
    public void startNewGame(Maps map, Player playerOne, Player playerTwo) {
        currentState = "GAME";
        
        // Initialize game facade with game data
        gameFacade = new GameFacade(mainFrame, map, playerOne);
        
        // Initialize input controller and register it as turn listener
        inputController = new InputController(gameFacade.getMainFrame());
        map.getConcreteTurnHandler().addTurnListener(inputController);
        
        // Clear and setup the display
        mainFrame.getContentPane().removeAll();
        mainFrame.add(gameFacade.getGamePanel(), BorderLayout.CENTER);
        
        // KEY FIX: Add InputController as KeyListener to multiple components
        mainFrame.addKeyListener(inputController);
        gameFacade.getGamePanel().addKeyListener(inputController);
        
        // Add to specific panels that need key input
        if (gameFacade.getMainFrame().getMapPanel() != null) {
            gameFacade.getMainFrame().getMapPanel().addKeyListener(inputController);
        }
        if (gameFacade.getMainFrame().getMovesPanel() != null) {
            gameFacade.getMainFrame().getMovesPanel().addKeyListener(inputController);
        }
        if (gameFacade.getMainFrame().getInventoryPanel() != null) {
            gameFacade.getMainFrame().getInventoryPanel().addKeyListener(inputController);
        }
        if (gameFacade.getMainFrame().getInteractionPanel() != null) {
            gameFacade.getMainFrame().getInteractionPanel().addKeyListener(inputController);
        }
        
        mainFrame.setFocusable(true);
        mainFrame.revalidate();
        mainFrame.repaint();
        
        // Request focus after GUI is built using invokeLater
        SwingUtilities.invokeLater(() -> {
            gameFacade.focusMapPanel();
            if (gameFacade.getMainFrame().getMapPanel() != null) {
                gameFacade.getMainFrame().getMapPanel().requestFocusInWindow();
            }
        });
        
        // Start the turn system
        map.getConcreteTurnHandler().start();
    }
    
    public void pauseGame() {
        currentState = "PAUSE";
        // Implementation for pause menu
    }
    
    public void exitGame() {
        System.exit(0);
    }
    
    public void show() {
        showMenu();
        mainFrame.setVisible(true);
    }
    
    public JFrame getMainFrame() {
        return mainFrame;
    }
    
    public GameFacade getGameFacade() {
        return gameFacade;
    }
}


/**
 * Facade for managing the game screen and all game-related panels
 */
class GameFacade {
    private MainFrame mainFrameAdapter;
    private JPanel gamePanel;
    private LeftPanelFacade leftPanelFacade;
    private CenterPanelFacade centerPanelFacade;
    private Maps map;
    private Player currentPlayer;
    
    public GameFacade(JFrame hostFrame, Maps map, Player player) {
        this.map = map;
        this.currentPlayer = player;
        
        // Create MainFrame adapter for InputController compatibility
        mainFrameAdapter = new MainFrame(map);
        mainFrameAdapter.setCurrentPlayer(player);
        
        initializeGamePanel();
    }
    
    private void initializeGamePanel() {
        gamePanel = new JPanel(new BorderLayout());
        gamePanel.setBackground(Color.BLACK);
        gamePanel.setFocusable(true);
        
        // Initialize sub-facades
        leftPanelFacade = new LeftPanelFacade(mainFrameAdapter, currentPlayer);
        centerPanelFacade = new CenterPanelFacade(mainFrameAdapter, map);
        
        // Add panels to game layout
        gamePanel.add(leftPanelFacade.getLeftPanel(), BorderLayout.WEST);
        gamePanel.add(centerPanelFacade.getCenterPanel(), BorderLayout.CENTER);
    }
    
    public JPanel getGamePanel() {
        return gamePanel;
    }
    
    public MainFrame getMainFrame() {
        return mainFrameAdapter;
    }
    
    public void focusMapPanel() {
        centerPanelFacade.focusMapPanel();
    }
    
    public void updateDisplay() {
        leftPanelFacade.updateAllPanels();
        centerPanelFacade.updateMapPanel();
    }
}


/**
 * Facade for managing the left side panels
 */
class LeftPanelFacade {
    private JPanel leftPanel;
    private InventoryPanel inventoryPanel;
    private DetailsPanel detailsPanel;
    private WeaponsPanel weaponsPanel;
    private MovesPanel movesPanel;
    private TurnOrderPanel turnOrderPanel;
    private StatsPanel statsPanel;
    private MainFrame mainFrame;
    private Player player;
    
    // Border containers for focus highlighting
    private JPanel inventoryContainer;
    private JPanel movesContainer;
    
    public LeftPanelFacade(MainFrame mainFrame, Player player) {
        this.mainFrame = mainFrame;
        this.player = player;
        initializeLeftPanel();
    }
    
    private void initializeLeftPanel() {
        leftPanel = new JPanel();
        leftPanel.setLayout(new GridBagLayout());
        leftPanel.setBackground(Color.BLACK);
        leftPanel.setPreferredSize(new Dimension(300, 1080));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.insets = new Insets(2, 2, 2, 2);
        
        // Top section (Inventory + Details)
        JPanel topSection = createTopSection();
        gbc.gridy = 0;
        gbc.weighty = 0.25;
        leftPanel.add(topSection, gbc);
        
        // Middle section (Weapons + Moves)
        JPanel middleSection = createMiddleSection();
        gbc.gridy = 1;
        gbc.weighty = 0.35;
        leftPanel.add(middleSection, gbc);
        
        // Bottom section (Turn Order + Stats)
        JPanel bottomSection = createBottomSection();
        gbc.gridy = 2;
        gbc.weighty = 0.40;
        leftPanel.add(bottomSection, gbc);
    }
    
    private JPanel createTopSection() {
        JPanel topSection = new JPanel(new GridLayout(1, 2, 2, 0));
        topSection.setBackground(Color.BLACK);
        
        // Get panels from MainFrame
        inventoryPanel = mainFrame.getInventoryPanel();
        if (inventoryPanel == null) {
            inventoryPanel = new InventoryPanel(player);
        }
        inventoryPanel.setFocusable(true);
        inventoryContainer = createBorderedPanel(inventoryPanel, "INVENTORY");
        topSection.add(inventoryContainer);
        
        // Add focus listener to update border
        inventoryPanel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                updateBorder(inventoryContainer, true);
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                updateBorder(inventoryContainer, false);
            }
        });
        
        detailsPanel = mainFrame.getDetailsPanel();
        if (detailsPanel == null) {
            detailsPanel = new DetailsPanel();
        }
        topSection.add(createBorderedPanel(detailsPanel, "DETAILS"));
        
        return topSection;
    }
    
    private JPanel createMiddleSection() {
        JPanel middleSection = new JPanel(new GridLayout(1, 2, 2, 0));
        middleSection.setBackground(Color.BLACK);
        
        weaponsPanel = mainFrame.getWeaponsPanel();
        if (weaponsPanel == null) {
            weaponsPanel = new WeaponsPanel(player);
        }
        middleSection.add(createBorderedPanel(weaponsPanel, "WEAPONS"));
        
        movesPanel = mainFrame.getMovesPanel();
        if (movesPanel == null) {
            movesPanel = new MovesPanel(player);
        }
        movesPanel.setFocusable(true);
        movesContainer = createBorderedPanel(movesPanel, "MOVES");
        middleSection.add(movesContainer);
        
        // Add focus listener to update border
        movesPanel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                updateBorder(movesContainer, true);
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                updateBorder(movesContainer, false);
            }
        });
        
        return middleSection;
    }
    
    private JPanel createBottomSection() {
        JPanel bottomSection = new JPanel(new GridLayout(2, 1, 0, 2));
        bottomSection.setBackground(Color.BLACK);
        
        turnOrderPanel = mainFrame.getTurnOrderPanel();
        if (turnOrderPanel == null) {
            turnOrderPanel = new TurnOrderPanel(mainFrame.getMap().getConcreteTurnHandler());
        }
        bottomSection.add(createBorderedPanel(turnOrderPanel, "TURN ORDER"));
        
        statsPanel = mainFrame.getStatsPanel();
        if (statsPanel == null) {
            statsPanel = new StatsPanel(player);
        }
        bottomSection.add(createBorderedPanel(statsPanel, "STATS"));
        
        return bottomSection;
    }
    
    private JPanel createBorderedPanel(JPanel content, String title) {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.BLACK);
        
        // Title label
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 11));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(Color.BLACK);
        titleLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));
        
        container.add(titleLabel, BorderLayout.NORTH);
        container.add(content, BorderLayout.CENTER);
        container.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        
        return container;
    }
    
    private void updateBorder(JPanel container, boolean focused) {
        if (focused) {
            container.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2));
        } else {
            container.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        }
        container.repaint();
    }
    
    public JPanel getLeftPanel() {
        return leftPanel;
    }
    
    public void updateAllPanels() {
        if (inventoryPanel != null) inventoryPanel.repaint();
        if (movesPanel != null) movesPanel.repaint();
        if (detailsPanel != null) detailsPanel.repaint();
        if (statsPanel != null) statsPanel.repaint();
        if (weaponsPanel != null) weaponsPanel.repaint();
        if (turnOrderPanel != null) turnOrderPanel.repaint();
    }
}


/**
 * Facade for managing the center area (Map and Interaction panels)
 */
class CenterPanelFacade {
    private JPanel centerPanel;
    private MapPanel mapPanel;
    private InteractionPanel interactionPanel;
    private SpritePanel spritePanel;
    private MainFrame mainFrame;
    private Maps map;
    
    // Border container for map panel
    private JPanel mapContainer;
    
    public CenterPanelFacade(MainFrame mainFrame, Maps map) {
        this.mainFrame = mainFrame;
        this.map = map;
        initializeCenterPanel();
    }
    
    private void initializeCenterPanel() {
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.BLACK);
        
        // Map Panel (main view) with container for border
        mapPanel = mainFrame.getMapPanel();
        if (mapPanel == null) {
            mapPanel = new MapPanel(map);
        }
        mapPanel.setFocusable(true);
        
        // Create a container for the map panel to hold the border
        mapContainer = new JPanel(new BorderLayout());
        mapContainer.setBackground(Color.BLACK);
        mapContainer.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2));
        mapContainer.add(mapPanel, BorderLayout.CENTER);
        centerPanel.add(mapContainer, BorderLayout.CENTER);
        
        // Add focus listener to update border
        mapPanel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                mapContainer.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2));
                mapContainer.repaint();
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                mapContainer.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
                mapContainer.repaint();
            }
        });
        
        // Bottom panel container (for InteractionPanel and SpritePanel)
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.BLACK);
        bottomPanel.setPreferredSize(new Dimension(0, 200));
        
        // Interaction Panel (bottom left)
        interactionPanel = mainFrame.getInteractionPanel();
        if (interactionPanel == null) {
            interactionPanel = new InteractionPanel();
        }
        interactionPanel.setFocusable(true);
        
        JPanel interactionContainer = new JPanel(new BorderLayout());
        interactionContainer.setBackground(Color.BLACK);
        interactionContainer.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        interactionContainer.add(interactionPanel, BorderLayout.CENTER);
        interactionContainer.setVisible(false); // Hidden by default
        bottomPanel.add(interactionContainer, BorderLayout.CENTER);
        
        // Add focus listener to interaction panel
        interactionPanel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                interactionContainer.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2));
                interactionContainer.setVisible(true);
                interactionContainer.repaint();
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                interactionContainer.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
                interactionContainer.repaint();
            }
        });
        
        // Sprite Panel (bottom right)
        spritePanel = mainFrame.getSpritePanel();
        if (spritePanel == null) {
            spritePanel = new SpritePanel();
        }
        spritePanel.setPreferredSize(new Dimension(200, 200));
        spritePanel.setVisible(false); // Hidden by default
        bottomPanel.add(spritePanel, BorderLayout.EAST);
        
        centerPanel.add(bottomPanel, BorderLayout.SOUTH);
    }
    
    public JPanel getCenterPanel() {
        return centerPanel;
    }
    
    public void focusMapPanel() {
        mapPanel.requestFocusInWindow();
    }
    
    public void updateMapPanel() {
        mapPanel.repaint();
    }
    
    public void showInteractionPanel(boolean show) {
        interactionPanel.setVisible(show);
        spritePanel.setVisible(show);
        centerPanel.revalidate();
    }
    
    public SpritePanel getSpritePanel() {
        return spritePanel;
    }
    
    public InteractionPanel getInteractionPanel() {
        return interactionPanel;
    }
}


/**
 * Facade for managing menu screens
 */
class MenuFacade {
    private JPanel menuPanel;
    private MainGUIFacade mainFacade;
    
    public MenuFacade(MainGUIFacade mainFacade) {
        this.mainFacade = mainFacade;
        initializeMenuPanel();
    }
    
    private void initializeMenuPanel() {
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(Color.BLACK);
        
        // Title
        JLabel title = new JLabel("ART ATTACK");
        title.setFont(new Font("Monospaced", Font.BOLD, 48));
        title.setForeground(Color.CYAN);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Buttons
        JButton newGameBtn = createMenuButton("New Game");
        JButton loadGameBtn = createMenuButton("Load Game");
        JButton settingsBtn = createMenuButton("Settings");
        JButton exitBtn = createMenuButton("Exit");
        
        // Add spacing
        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(title);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 50)));
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
            Player playerOne = new Musician(
                1, '@', "Zappa", 
                new Coordinates(2, 2), 
                List.of(new Weapon("Guitar", "A musical weapon", 10)), 
                5, 5, moveArea, 20, 20, 0, 20, 1, 5, 2, List.of(new Cure("Potion", " ", 10)), null, null
            );
            
            Player playerTwo = new MovieDirector(
                2, '@', "Lynch", 
                new Coordinates(5, 5),
                List.of(new Weapon("Camera", "A cinematic weapon", 10)), 
                5, 5, moveArea, 20, 20, 0, 20, 1, 5, 2, List.of(new Cure("Potion", " ", 10)), null, null
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
        button.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2));
        
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