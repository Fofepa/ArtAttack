package com.artattack.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.artattack.inputcontroller.InputController;
import com.artattack.level.Maps;
import com.artattack.mapelements.Player;


/**
 * Main Facade that coordinates all GUI components and sub-facades
 */
public class MainGUIFacade {
    private JFrame mainFrame;
    private InputController inputController;
    private GameFacade gameFacade;
    private MenuPanel menuFacade;
    private PausePanel pausePanel;
    
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
        menuFacade = new MenuPanel(this);
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

        gameFacade.getMainFrame().setMainGUIFacade(this);
        
        //Initialize pause panel
        pausePanel = gameFacade.getMainFrame().getPausePanel();

        if (pausePanel == null) {
            pausePanel = new PausePanel(this);
            // Store it back in MainFrame for consistency

            gameFacade.getMainFrame().setPausePanel(pausePanel);
        }
        pausePanel.setVisible(false);

        // Initialize input controller and register it as turn listener
        inputController = new InputController(gameFacade.getMainFrame());
        map.getConcreteTurnHandler().addTurnListener(inputController);
        
        // Clear and setup the display
        mainFrame.getContentPane().removeAll();

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
        
        JPanel gamePanel = gameFacade.getGamePanel();
        gamePanel.setBounds(0, 0, 1920, 1080);
        pausePanel.setBounds(0, 0, 1920, 1080);
        
        layeredPane.add(gamePanel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(pausePanel, JLayeredPane.PALETTE_LAYER);
        
        mainFrame.add(layeredPane, BorderLayout.CENTER);
        
        
        
        // KEY FIX: Add InputController as KeyListener to multiple components
        mainFrame.addKeyListener(inputController);
        gameFacade.getGamePanel().addKeyListener(inputController);
        pausePanel.addKeyListener(inputController);
        
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
        if (gameFacade.getMainFrame().getWeaponsPanel() != null) {
            gameFacade.getMainFrame().getWeaponsPanel().addKeyListener(inputController);
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


    public void showPauseMenu() {
        if (pausePanel != null) {
            pausePanel.showPauseMenu();
            currentState = "PAUSE";
        }
    }
    
    public void hidePauseMenu() {
        if (pausePanel != null) {
            pausePanel.hidePauseMenu();
            currentState = "GAME";
        }
    }
    
    public boolean isPaused() {
        return currentState.equals("PAUSE");
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
    private JPanel weaponsContainer;
    
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
        weaponsPanel.setFocusable(true);
        weaponsContainer = createBorderedPanel(weaponsPanel, "WEAPONS");
        middleSection.add(weaponsContainer);
        
        movesPanel = mainFrame.getMovesPanel();
        if (movesPanel == null) {
            movesPanel = new MovesPanel(player);
        }
        movesPanel.setFocusable(true);
        movesContainer = createBorderedPanel(movesPanel, "MOVES");
        middleSection.add(movesContainer);
        
        // Add focus listener to update border
        weaponsPanel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                updateBorder(weaponsContainer, true);
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                updateBorder(weaponsContainer, false);
            }
        });

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


