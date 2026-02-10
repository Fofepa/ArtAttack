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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.artattack.inputcontroller.InputController;
import com.artattack.items.Item;
import com.artattack.level.AreaBuilder;
import com.artattack.level.Coordinates;
import com.artattack.level.MapBuilderTypeOne;
import com.artattack.level.MapDirector;
import com.artattack.level.MapManager;
import com.artattack.level.Maps;
import com.artattack.mapelements.ActiveElement;
import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.MapElement;
import com.artattack.mapelements.Player;
import com.artattack.mapelements.PlayerType;
import com.artattack.mapelements.skilltree.Node;
import com.artattack.mapelements.skilltree.SkillTree;
import com.artattack.mapelements.skilltree.SkillTreeFactory;
import com.artattack.moves.Move;
import com.artattack.moves.Weapon;
import com.artattack.saving.SaveManager;


/**
 * Main Facade that coordinates all GUI components and sub-facades
 */
public class MainGUIFacade {
    private JFrame mainFrame;
    private InputController inputController;
    private GameFacade gameFacade;
    private MenuPanel menuFacade;
    private PausePanel pausePanel;
    private CharacterSelectionPanel characterSelectionPanel;
    private SkillTreePanel skillTreePanel;
    private LevelCompletePanel levelCompletePanel;
    private long levelStartTime;
    
    private String currentState = "MENU"; 
    
    public MainGUIFacade() {
        
        initializeMainFrame();
        initializeFacades();
        this.levelStartTime = System.currentTimeMillis();
    }
    
    private void initializeMainFrame() {
        mainFrame = new JFrame("Art Attack");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // --- RESIZE MANAGER---
        mainFrame.setPreferredSize(new Dimension(1920, 1080));
        
        mainFrame.setMinimumSize(new Dimension(1280, 720));
        
        mainFrame.setResizable(true);

        mainFrame.setLayout(new BorderLayout());
        mainFrame.getContentPane().setBackground(Color.BLACK);
        
        mainFrame.pack(); 
        mainFrame.setLocationRelativeTo(null);
    }
    
    private void initializeFacades() {
        menuFacade = new MenuPanel(this);
        characterSelectionPanel = new CharacterSelectionPanel(this);
    }
    
    public void showMenu() {
        currentState = "MENU";
        mainFrame.getContentPane().removeAll();
        mainFrame.add(menuFacade.getMenuPanel(), BorderLayout.CENTER);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    public void showCharacterSelection() {
        currentState = "SELECTION";
        mainFrame.getContentPane().removeAll();
        
        characterSelectionPanel = new CharacterSelectionPanel(this);
        
        mainFrame.add(characterSelectionPanel, BorderLayout.CENTER);
        mainFrame.revalidate();
        mainFrame.repaint();
        
        // KeyListener Focus
        characterSelectionPanel.requestFocusInWindow();
    }

    public void finalizeGameSetup(CharacterType p1Type, CharacterType p2Type) {
        System.out.println("Setup game with: " + p1Type + " vs " + p2Type);
        
        createGameFromSelection(p1Type, p2Type);
    }

    private void createGameFromSelection(CharacterType p1Type, CharacterType p2Type) {
         try {

            //Creating MapManager
            MapManager mm = new MapManager(new HashMap<Integer, Maps>(), 0);

            // Creating MoveArea
            AreaBuilder ab = new AreaBuilder();
            ab.addShape("square", 20, true);
            List<Coordinates> moveArea = ab.getResult();
            
            // Creating Players
            Player playerOne = createPlayerFromType(p1Type, 1, new Coordinates(29, 23), moveArea, new ArrayList<>()); // Tutorial: 29, 23 | Lv1: 28, 2
            Player playerTwo = createPlayerFromType(p2Type, 2, new Coordinates(26, 23), moveArea, new ArrayList<>()); // Tutorial: 26, 23 | Lv1: 28, 4
            
            // Creating Map
            MapBuilderTypeOne mb1 = new MapBuilderTypeOne();
            MapDirector md = new MapDirector(mb1);
            md.make("Tutorial");
            mb1.setPlayerOne(playerOne);
            mb1.setPlayerTwo(playerTwo);
            mb1.setID(0);
            mb1.setDict();
            mb1.setTurnQueue();
            mb1.startMap();
            Maps map_t = mb1.getResult();
            mm.getLevels().put(map_t.getID(), map_t);
            md.make("1");
            mb1.setID(1);
            mb1.setDict();
            mb1.setTurnQueue();
            mb1.startMap();
            Maps map_1 = mb1.getResult();
            mm.getLevels().put(map_1.getID(), map_1);
            md.make("BossRoom1");
            mb1.setPlayerOne(playerOne);
            mb1.setPlayerTwo(playerTwo);
            mb1.setID(2);
            mb1.setDict();
            mb1.setTurnQueue();
            mb1.startMap();
            map_t = mb1.getResult();
            mm.getLevels().put(map_t.getID(), map_t);

            // Start Game and set skill trees
            startNewGame(mm, playerOne, playerTwo);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Player createPlayerFromType(CharacterType type, int id, Coordinates coords, List<Coordinates> moveArea, List<Item> items) {
        
        // Character stats come From the enum class CharacterType
        AreaBuilder areaBuilder = new AreaBuilder();
        areaBuilder.addShape("x",2);
        List<Coordinates> zappaMA = areaBuilder.getResult();
        areaBuilder.addShape("4");
        zappaMA.addAll(areaBuilder.getResult());
        areaBuilder.addShape("square",1,true);
        List<Coordinates> lynchMA = areaBuilder.getResult();
        areaBuilder.addShape("4");
        List<Coordinates> area4 = areaBuilder.getResult();
        Move m1 = new Move(); m1.setName("Kick"); m1.setPower(3); m1.setAttackArea(area4); m1.setActionPoints(2);
        Move m2 = new Move(); m2.setName("Bump"); m2.setPower(5); m2.setAttackArea(area4); m2.setActionPoints(1);
        switch (type) {
            case MUSICIAN -> {
                Weapon musicianWeapon = new Weapon(type.getWeaponName(), "Default Weapon", 4, List.of(m1,m2), PlayerType.MUSICIAN);
                return new Player(id, '♫', type.getName(), coords,
                        List.of(musicianWeapon),
                        15, 15, zappaMA, 19, type.getMaxHP(), 10,
                        20, 1, type.getSpeed(), 2, items, null, null, PlayerType.MUSICIAN, SkillTreeFactory.createSkillTree(PlayerType.MUSICIAN),
                        "/images/frank-zappa-fotor-20260206135640.jpg" );
            }
            
            case DIRECTOR -> {
                Weapon directorWeapon = new Weapon(type.getWeaponName(), "Default Weapon", 4, List.of(m1,m2), PlayerType.MOVIE_DIRECTOR);
                return new Player(id, '◉', type.getName(), coords,
                        List.of(directorWeapon),
                        15, 15, lynchMA, 20, type.getMaxHP(),
                        10, 20, 1, type.getSpeed(), 2, items, null, null, PlayerType.MOVIE_DIRECTOR, SkillTreeFactory.createSkillTree(PlayerType.MOVIE_DIRECTOR),
                        "/images/ozxg45isal6ve56l7tl6-fotor-20260206135846.jpg");
            }
                    
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        }
    }
    
    public void startNewGame(MapManager maps, Player playerOne, Player playerTwo) {
        currentState = "GAME";
        
        // Initialize game facade with game data
        gameFacade = new GameFacade(mainFrame, maps, playerOne);

        //Maps map = maps.getLevels().get(maps.getCurrMap());
        

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
        maps.getLevels().get(maps.getCurrMap()).getConcreteTurnHandler().addTurnListener(inputController);
        
        if (gameFacade.getMainFrame().getInteractionPanel() != null) {
            gameFacade.getMainFrame().getInteractionPanel().setDefaultPlayerImage(playerOne.getSpritePath());
        }
        
        // Clear and setup the display
        mainFrame.getContentPane().removeAll();

        JLayeredPane layeredPane = new JLayeredPane() {
            @Override
            public void doLayout() {
                // Internal components size
                int w = getWidth();
                int h = getHeight();
                for (java.awt.Component c : getComponents()) {
                    c.setBounds(0, 0, w, h);
                }
            }
        };
        JPanel gamePanel = gameFacade.getGamePanel();
        
        layeredPane.add(gamePanel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(pausePanel, JLayeredPane.PALETTE_LAYER);
        
        mainFrame.add(layeredPane, BorderLayout.CENTER);
        
        
        
        
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

        mainFrame.requestFocusInWindow();
        gameFacade.getGamePanel().requestFocusInWindow();

        
        Maps currentLevel = maps.getLevels().get(maps.getCurrMap());
// In MainGUIFacade.java, dentro startNewGame

SwingUtilities.invokeLater(() -> {
    System.out.println("\n--- DEBUG RICERCA NPC ---");
    
    // 1. Verifichiamo se la lista esiste
    if (currentLevel.getInteractableElements() == null) {
        System.out.println("❌ ERRORE GRAVE: La lista getInteractableElements() è NULL!");
    } else if (currentLevel.getInteractableElements().isEmpty()) {
        System.out.println("⚠️ ATTENZIONE: La lista getInteractableElements() è VUOTA!");
    } else {
        System.out.println("✅ La lista contiene " + currentLevel.getInteractableElements().size() + " elementi.");
        
        // 2. Stampiamo tutti i nomi per vedere se c'è un errore di battitura
        boolean trovato = false;
        MapElement speaker = null;

        for (com.artattack.mapelements.InteractableElement element : currentLevel.getInteractableElements()) {
            System.out.println(" -> Esamino elemento: '" + element.getName() + "'");
            
            // Confronto esatto stringhe
            if ("Georges Méliès".equals(element.getName())) {
                System.out.println("   >>> TROVATO! <<<");
                System.out.println("   Sprite Path associato: '" + element.getSpritePath() + "'");
                speaker = element;
                trovato = true;
                break; 
            }
        }
        
        if (!trovato) {
            System.out.println("❌ FALLITO: Nessun elemento corrisponde a 'Georges Méliès'.");
        }

        // Passiamo quello che abbiamo trovato (o null)
        gameFacade.getMainFrame().showDialog(List.of(
            "Hey, over here! Mr. Zappa! Mr. Lynch!", 
            "What a mess! Look at what they've done to our peaceful resting place! It's now a soul-harvesting data center! You need to do something about this, or else we'll never be able to return to our well-earned slumber!",
            "Me? Oh, I'm nowhere near as powerful as you guys, I wouldn't last a second against these guys.",
            "You guys don't look so good... What happened to you?",
            "Oh! Right! You two were dead, just like me! Hahahaha!",
            "What's that? You don't remember how to do things? Oh yeah, I guess it makes sense considering you were six feet deep just a few moments ago... You must be rusty on what it takes to walk the earth.",
            "But it's fine, I can teach you everything you need to know.",
            "Each one of you has a CURSOR. It has many uses, like moving!",
            "You can position your CURSOR using WASD or the Arrow Keys. Then, use Enter to move where the CURSOR is!",
            "Instead, if you see something that interests you, you can position your CURSOR on it and press the E key to interact with it. You can also talk to people this way!",
            "Keep in mind that if you want to interact with something, you have to move next to it first!",
            "That's the basics. If you want me to refresh your memory, just talk to me.",
            "Now, go! Get to the top floor and defeat these power-hungry nerds!",
            "Wait... Look! That red guy over there! It's an employee! You have to defeat him to get to the next floor!",
            "Press M to shift your focus to the map and go teach him a lesson!"
        ), speaker.getSpritePath()); 
    }
    System.out.println("-------------------------\n");
});
        
        // Start the turn system
        maps.getLevels().get(maps.getCurrMap()).getConcreteTurnHandler().start();
        this.levelStartTime = System.currentTimeMillis();
    }

    public void loadGame() {
        currentState = "GAME";

        SaveManager saveManager = new SaveManager();
        try{
            MapManager mm = saveManager.load();

            for(Maps map : mm.getLevels().values()){
                map.setDict();
            }

            //Initialize turn
            List<ActiveElement> active = new ArrayList<>();
            active.add(mm.getLevels().get(mm.getCurrMap()).getPlayerOne());
            active.add(mm.getLevels().get(mm.getCurrMap()).getPlayerTwo());
            for(Enemy enemy : mm.getLevels().get(mm.getCurrMap()).getEnemies()){
                if(enemy.getIsActive())
                    active.add(enemy);
            }
            mm.getLevels().get(mm.getCurrMap()).setTurnQueue(active, mm.getTurnIndex());


            Player playerOne = mm.getLevels().get(mm.getCurrMap()).getPlayerOne();
            
            // Initialize game facade with game data
            gameFacade = new GameFacade(mainFrame, mm, playerOne, saveManager);

            //Maps map = maps.getLevels().get(maps.getCurrMap());
            

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
            mm.getLevels().get(mm.getCurrMap()).getConcreteTurnHandler().addTurnListener(inputController);
            
            // Clear and setup the display
            mainFrame.getContentPane().removeAll();

            JLayeredPane layeredPane = new JLayeredPane() {
                @Override
                public void doLayout() {
                    int w = getWidth();
                    int h = getHeight();
                    for (java.awt.Component c : getComponents()) {
                        c.setBounds(0, 0, w, h);
                    }
                }
            };
            
            JPanel gamePanel = gameFacade.getGamePanel();
            layeredPane.add(gamePanel, JLayeredPane.DEFAULT_LAYER);
            layeredPane.add(pausePanel, JLayeredPane.PALETTE_LAYER);
            mainFrame.add(layeredPane, BorderLayout.CENTER);
            
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

            mainFrame.requestFocusInWindow();
            gameFacade.getGamePanel().requestFocusInWindow();


            //Start turn system
            mm.getLevels().get(mm.getCurrMap()).getConcreteTurnHandler().start(mm.getTurnIndex());
            mm.getLevels().get(mm.getCurrMap()).getConcreteTurnHandler().updateTurn();
            this.levelStartTime = System.currentTimeMillis();
            
        } catch(IOException e){
            e.printStackTrace();
        }
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

            if (gameFacade != null) {
                SwingUtilities.invokeLater(() -> {
                    gameFacade.focusMapPanel(); 
                });
            }
        }
    }
    
    public boolean isPaused() {
        return currentState.equals("PAUSE");
    }
    
    /**
     * Show the skill tree panel to allow the player to choose a power-up
     */
    public void showSkillTreePanel(Player player, SkillTree skillTree, Consumer<Node> callback) {
        currentState = "SKILL_TREE";
        
        // Create the skill tree panel
        skillTreePanel = new SkillTreePanel(skillTree, player, (selectedNode) -> {
            
            selectedNode.setSkill(player);
            player.setLeveledUp(false);
            
            // Close the panel and resume the game
            hideSkillTreePanel();
            
            // Callback to notify that the selection is complete
            if (callback != null) {
                callback.accept(selectedNode);
            }
        });
        
        // Get the layered bread
        JLayeredPane layeredPane = (JLayeredPane) mainFrame.getContentPane().getComponent(0);
        
        // Add the skill tree panel above everything
        skillTreePanel.setBounds(0, 0, mainFrame.getWidth(), mainFrame.getHeight());
        layeredPane.add(skillTreePanel, JLayeredPane.POPUP_LAYER);
        skillTreePanel.requestFocusInWindow();
        mainFrame.revalidate();
        mainFrame.repaint();
    }
    
    /**
     * Hides the skill tree panel
     */
    public void hideSkillTreePanel() {
        if (skillTreePanel != null) {
            JLayeredPane layeredPane = (JLayeredPane) mainFrame.getContentPane().getComponent(0);
            layeredPane.remove(skillTreePanel);
            skillTreePanel = null;
            currentState = "GAME";
            
            // Ridai focus al gioco
            if (gameFacade != null && gameFacade.getMainFrame() != null) {
                gameFacade.getMainFrame().focusMapPanel();
            }
            
            mainFrame.revalidate();
            mainFrame.repaint();
        }
    }

    public void showLevelComplete(Maps nextMap) {
        currentState = "LEVEL_COMPLETE";
        
        // 1. Get  elapsed time
        long elapsedMillis = System.currentTimeMillis() - levelStartTime;
        long seconds = (elapsedMillis / 1000) % 60;
        long minutes = (elapsedMillis / (1000 * 60)) % 60;
        String timeString = String.format("%02d:%02d", minutes, seconds);

        // 2. clean old panels
        if (levelCompletePanel != null) {
            mainFrame.getLayeredPane().remove(levelCompletePanel);
        }

        // 3. Create new panels
        levelCompletePanel = new LevelCompletePanel(this, nextMap, timeString);
        levelCompletePanel.setBounds(0, 0, mainFrame.getWidth(), mainFrame.getHeight());
        
        // 4. Add to the layer
        JLayeredPane layeredPane = mainFrame.getLayeredPane();
        layeredPane.add(levelCompletePanel, JLayeredPane.POPUP_LAYER);
        
        mainFrame.revalidate();
        mainFrame.repaint();
        
        levelCompletePanel.requestFocusInWindow();
    }

    public void finalizeMapSwitch(Maps nextMap) {
        // 1. Remove level complete panel
        if (levelCompletePanel != null) {
            mainFrame.getLayeredPane().remove(levelCompletePanel);
            levelCompletePanel = null;
        }

        // 2. Switch maps
        if (gameFacade != null && gameFacade.getMainFrame() != null) {
            gameFacade.getMainFrame().switchMap(nextMap);
        }
        
        // 3. Reset timer and state
        currentState = "GAME";
        this.levelStartTime = System.currentTimeMillis();
        
        // 4.Five focus to the map
        if (gameFacade != null) {
            gameFacade.focusMapPanel();
        }
        
        mainFrame.revalidate();
        mainFrame.repaint();
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

    public InputController getInputController(){
        return this.inputController;
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
    
    public GameFacade(JFrame hostFrame, MapManager maps, Player player) {
        this.map = maps.getLevels().get(maps.getCurrMap());
        this.currentPlayer = player;
        
        // Create MainFrame adapter for InputController compatibility
        mainFrameAdapter = new MainFrame(map);
        mainFrameAdapter.setCurrentPlayer(player);
        mainFrameAdapter.setGameContext(new GameContext(mainFrameAdapter, maps));
        
        initializeGamePanel();
    }

        public GameFacade(JFrame hostFrame, MapManager maps, Player player, SaveManager saveManager) {
        this.map = maps.getLevels().get(maps.getCurrMap());
        this.currentPlayer = player;
        
        // Create MainFrame adapter for InputController compatibility
        mainFrameAdapter = new MainFrame(map);
        mainFrameAdapter.setCurrentPlayer(player);
        mainFrameAdapter.setGameContext(new GameContext(mainFrameAdapter, maps, saveManager));
        
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
            
                if (interactionPanel != null && interactionPanel.isVisible()) {
                    mapContainer.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
                } else {
                    mapContainer.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2));
                }
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
        interactionContainer.setVisible(true);

        interactionContainer.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                // APPENA il dialogo diventa visibile -> Spegni bordo mappa
                if (mapContainer != null) {
                    mapContainer.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
                    mapContainer.repaint();
                }
            }

            @Override
            public void componentHidden(java.awt.event.ComponentEvent e) {
                // Quando il dialogo si chiude -> Se la mappa ha il focus, riaccendi bordo
                if (mapContainer != null && mapPanel != null && mapPanel.hasFocus()) {
                    mapContainer.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2));
                    mapContainer.repaint();
                }
            }
        });
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