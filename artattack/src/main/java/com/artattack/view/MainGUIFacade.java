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
import com.artattack.level.AreaBuilder;
import com.artattack.level.Coordinates;
import com.artattack.level.MapBuilderTypeOne;
import com.artattack.level.MapDirector;
import com.artattack.level.MapManager;
import com.artattack.level.Maps;
import com.artattack.mapelements.ActiveElement;
import com.artattack.mapelements.ConcretePlayerBuilder;
import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.MapElement;
import com.artattack.mapelements.Player;
import com.artattack.mapelements.PlayerBuilder;
import com.artattack.mapelements.PlayerDirector;
import com.artattack.mapelements.PlayerType;
import com.artattack.mapelements.skilltree.Node;
import com.artattack.mapelements.skilltree.SkillTree;
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
    private GameOverPanel gameOverPanel;
    private long levelStartTime;
    private DeathPanel deathPanel;
    
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

            //Creating MoveArea
            AreaBuilder ab = new AreaBuilder();
            ab.addShape("square", 20, true);
            List<Coordinates> moveArea = ab.getResult();
            
            //Creating Players
            PlayerBuilder pb = new ConcretePlayerBuilder();
            PlayerDirector pd = new PlayerDirector();
            pd.create(pb, p1Type, 0);
            Player p1 = pb.getResult(); // Tutorial: 29, 23 | Lv1: 28, 2 | BossRoom1: 14, 37 | Reception: 19, 1 | BigEnemyArea: 1, 1
            pd.create(pb, p2Type, 1);
            Player p2 = pb.getResult(); // Tutorial: 26, 23 | Lv1: 28, 4 | BossRoom1: 17, 37 | Reception: 20, 1 | BigEnemyArea: 1, 3
            /* Player playerOne = createPlayerFromType(p1Type, 1, new Coordinates(20, 38), moveArea, new ArrayList<>()); / Tutorial: 29, 23 | Lv1: 28, 2
            Player playerTwo = createPlayerFromType(p2Type, 2, new Coordinates(22, 38), moveArea, new ArrayList<>()); / Tutorial: 26, 23 | Lv1: 28, 4 */

            // Creating Map
            MapBuilderTypeOne mb1 = new MapBuilderTypeOne();
            MapDirector md = new MapDirector(mb1);

            md.make("Tutorial"); 
            mb1.setID(0);
            mb1.setPlayerOne(p1);
            mb1.setPlayerTwo(p2);
            mb1.setDict();
            mb1.setTurnQueue();
            mb1.startMap();
            Maps map_t = mb1.getResult();
            mm.getLevels().put(map_t.getID(), map_t);

            md.make("1");
            Maps map_1 = mb1.getResult();
            mm.getLevels().put(map_1.getID(), map_1);

            md.make("BossRoom1");
            Maps map_b1 = mb1.getResult();
            mm.getLevels().put(map_b1.getID(), map_b1);

            md.make("Reception");
            Maps map_r = mb1.getResult();
            mm.getLevels().put(map_r.getID(), map_r);

            md.make("BigEnemyArea");
            Maps map_B = mb1.getResult();
            mm.getLevels().put(map_B.getID(), map_B);

            md.make("PreBoss");
            Maps map_p = mb1.getResult();
            mm.getLevels().put(map_p.getID(), map_p);

            md.make("ChestRoom");
            Maps map_c = mb1.getResult();
            mm.getLevels().put(map_c.getID(), map_c);

            md.make("KeyRoom");
            Maps map_k = mb1.getResult();
            mm.getLevels().put(map_k.getID(), map_k);

            md.make("BossArena");
            Maps map_a = mb1.getResult();
            mm.getLevels().put(map_a.getID(), map_a);

           
            startNewGame(mm, p1, p2);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void startNewGame(MapManager maps, Player playerOne, Player playerTwo) {
        currentState = "GAME";
        
        gameFacade = new GameFacade(mainFrame, maps, playerOne);        

        gameFacade.getMainFrame().setMainGUIFacade(this);
        
        pausePanel = gameFacade.getMainFrame().getPausePanel();

        if (pausePanel == null) {
            pausePanel = new PausePanel(this);

            gameFacade.getMainFrame().setPausePanel(pausePanel);
        }
        pausePanel.setVisible(false);

        inputController = new InputController(gameFacade.getMainFrame());
        maps.getLevels().get(maps.getCurrMap()).getConcreteTurnHandler().addTurnListener(inputController);
        
        if (gameFacade.getMainFrame().getInteractionPanel() != null) {
            gameFacade.getMainFrame().getInteractionPanel().setDefaultPlayerImage(playerOne.getSpritePath());
        }
        
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

SwingUtilities.invokeLater(() -> {
    System.out.println("\n--- DEBUG RICERCA NPC ---");
    
    if (currentLevel.getInteractableElements() == null) {
        System.out.println("Error: getInteractableElements() is NULL!");
    } else if (currentLevel.getInteractableElements().isEmpty()) {
        System.out.println("Warning: getInteractableElements() is EMPTY!");
    } else {
        System.out.println("The list contains " + currentLevel.getInteractableElements().size() + " elements.");
        
        boolean trovato = false;
        MapElement speaker = null;

        for (com.artattack.mapelements.InteractableElement element : currentLevel.getInteractableElements()) {
            System.out.println(" -> Element checked: '" + element.getName() + "'");
            
            if ("Georges Méliès".equals(element.getName())) {
                System.out.println("   >>> Found! <<<");
                System.out.println("   Sprite Path: '" + element.getSpritePath() + "'");
                speaker = element;
                trovato = true;
                break; 
            }
        }
        
        if (!trovato) {
            System.out.println("Failed: no element corresponds to 'Georges Méliès'.");
        }

        gameFacade.getMainFrame().showDialog(List.of("Hey, over here! Mr. Zappa! Mr. Lynch!", 
                "What a mess! Look at what they've done to our peaceful resting place! It's now a soul-harvesting data center! You need to do something about this, or else we'll never be able to return to our well-earned slumber!",
                "Me? I'm George Melies! You gentlemen surely know who I am! I hate this place, last century the world wasn't nothing like this!", 
                "I hate this place with all my heart!", "Oh, I'm nowhere near as powerful as you guys, I wouldn't last a second against these guys.",
                "You guys don't look so good... What happened to you?",
                "Oh! Right! You two were dead, just like me! Hahahaha!",
                "What's that? You don't remember how to do things? Oh yeah, I guess it makes sense considering you were six feet deep just a few moments ago... You must be rusty on what it takes to walk the earth.",
                "But it's fine, I can teach you everything you need to know.",
                "You see all these panels in front of you? You can access all of them by pressing specific keys. You'll know when you're accessing a panel because it'll be highlighted.",
                "If you press M, you'll access the MAP PANEL, where you can see the MAP. By focusing on it, you'll be able to interact with the world!",
                "Each one of you has a CURSOR. It has many uses, like moving!",
                "You can position your CURSOR using WASD or the Arrow Keys while you're focused on the MAP PANEL. Then, use the ENTER Key to move where the CURSOR is!",
                "Or, you can position your CURSOR on something that interests you and INTERACT with it using the E Key. You can also talk to people this way!",
                "Keep in mind that you have to be right next to something to INTERACT with it. After all, it has to be within an arm's reach!",
                "That's the basics. If you want me to refresh your memory, just talk to me.",
                "Now, go! Get to the top floor and defeat these power-hungry nerds!"
    ), speaker.getSpritePath()); 
    }
    System.out.println("-------------------------\n");
});
        
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

            List<ActiveElement> active = new ArrayList<>();
            Player playerOne = mm.getLevels().get(mm.getCurrMap()).getPlayerOne(); 
            Player playerTwo = mm.getLevels().get(mm.getCurrMap()).getPlayerTwo(); 

            if(!playerOne.isAlive()){
                playerOne.updateHP(10);
                if(playerOne.getType() == PlayerType.MUSICIAN)
                    playerOne.setMapSymbol('♫'); 
                else  playerOne.setMapSymbol('◉');
            }
            active.add(playerOne);
            if(!playerTwo.isAlive()){
                playerTwo.updateHP(10);
                if(playerTwo.getType() == PlayerType.MUSICIAN)
                    playerTwo.setMapSymbol('♫'); 
                else  playerTwo.setMapSymbol('◉');
            }
             active.add(playerTwo);
            for(Enemy enemy : mm.getLevels().get(mm.getCurrMap()).getEnemies()){
                if(enemy.getIsActive())
                    active.add(enemy);
            }
            mm.getLevels().get(mm.getCurrMap()).setTurnQueue(active, mm.getTurnIndex());

            
            gameFacade = new GameFacade(mainFrame, mm, playerOne, saveManager);            

            gameFacade.getMainFrame().setMainGUIFacade(this);
            
            pausePanel = gameFacade.getMainFrame().getPausePanel();

            if (pausePanel == null) {
                pausePanel = new PausePanel(this);
                gameFacade.getMainFrame().setPausePanel(pausePanel);
            }
            pausePanel.setVisible(false);

            inputController = new InputController(gameFacade.getMainFrame());
            mm.getLevels().get(mm.getCurrMap()).getConcreteTurnHandler().addTurnListener(inputController);
            
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
    
    // shows the skillTreePanel of the current player
    public void showSkillTreePanel(Player player, SkillTree skillTree, Consumer<Node> callback) {
        currentState = "SKILL_TREE";
        
        skillTreePanel = new SkillTreePanel(skillTree, player, (selectedNode) -> {
            // Just notify - InputController handles the rest
            if (callback != null) {
                callback.accept(selectedNode);
            }
        });
        
        // Add InputController as KeyListener to SkillTreePanel
        if (inputController != null) {
            skillTreePanel.addKeyListener(inputController);
        }
        
        JLayeredPane layeredPane = (JLayeredPane) mainFrame.getContentPane().getComponent(0);
        
        skillTreePanel.setBounds(0, 0, mainFrame.getWidth(), mainFrame.getHeight());
        skillTreePanel.setVisible(true);
        skillTreePanel.setFocusable(true);
        
        layeredPane.add(skillTreePanel, JLayeredPane.POPUP_LAYER);
        layeredPane.moveToFront(skillTreePanel);
        
        mainFrame.revalidate();
        mainFrame.repaint();
        
        // Request focus after layout - use multiple invocations for reliability
        javax.swing.SwingUtilities.invokeLater(() -> {
            if (skillTreePanel != null) {
                skillTreePanel.requestFocus();
                skillTreePanel.requestFocusInWindow();
                System.out.println("Skill tree panel focus requested");
            }
        });
    }
    
    public void hideSkillTreePanel() {
        if (skillTreePanel != null) {
            JLayeredPane layeredPane = (JLayeredPane) mainFrame.getContentPane().getComponent(0);
            layeredPane.remove(skillTreePanel);
            skillTreePanel = null;
            currentState = "GAME";
            
            if (gameFacade != null && gameFacade.getMainFrame() != null) {
                gameFacade.getMainFrame().focusMapPanel();
            }
            
            mainFrame.revalidate();
            mainFrame.repaint();
        }
    }

    public void focusSkillTreePanel() {
        if (this.skillTreePanel != null) {
            this.skillTreePanel.setVisible(true);
            // Use both methods to ensure focus is grabbed
            javax.swing.SwingUtilities.invokeLater(() -> {
                if (skillTreePanel != null) {
                    skillTreePanel.requestFocus();
                    skillTreePanel.requestFocusInWindow();
                    System.out.println("Focus forcefully set to SkillTreePanel");
                }
            });
        }
    }

    public SkillTreePanel getSkillTreePanel() {
        return this.skillTreePanel;
    }

    public void showLevelComplete(Maps nextMap) {
        currentState = "LEVEL_COMPLETE";
        
        long elapsedMillis = System.currentTimeMillis() - levelStartTime;
        long seconds = (elapsedMillis / 1000) % 60;
        long minutes = (elapsedMillis / (1000 * 60)) % 60;
        String timeString = String.format("%02d:%02d", minutes, seconds);

        if (levelCompletePanel != null) {
            mainFrame.getLayeredPane().remove(levelCompletePanel);
        }

        levelCompletePanel = new LevelCompletePanel(this, nextMap, timeString);
        levelCompletePanel.setBounds(0, 0, mainFrame.getWidth(), mainFrame.getHeight());
        
        JLayeredPane layeredPane = mainFrame.getLayeredPane();
        layeredPane.add(levelCompletePanel, JLayeredPane.POPUP_LAYER);
        
        mainFrame.revalidate();
        mainFrame.repaint();
        
        levelCompletePanel.requestFocusInWindow();
    }

    public void finalizeMapSwitch(Maps nextMap) {
        System.out.println("finalizeMapSwitch called");
        if (levelCompletePanel != null) {
            mainFrame.getLayeredPane().remove(levelCompletePanel);
            levelCompletePanel = null;
        }

        if (gameFacade != null && gameFacade.getMainFrame() != null) {
            gameFacade.getMainFrame().switchMap(nextMap);
        }
        
        currentState = "GAME";
        this.levelStartTime = System.currentTimeMillis();
        
        if (gameFacade != null) {
            gameFacade.focusMapPanel();
        }
        
        mainFrame.revalidate();
        mainFrame.repaint();
    }

 
    public void showGameVictory() {
        if (gameFacade != null && gameFacade.getMainFrame() != null) {
            gameFacade.getMainFrame().getInteractionPanel().deactivate();
        }

        currentState = "GAME_OVER";

        long elapsedMillis = System.currentTimeMillis() - levelStartTime;
        long seconds = (elapsedMillis / 1000) % 60;
        long minutes = (elapsedMillis / (1000 * 60)) % 60;
        long hours = (elapsedMillis / (1000 * 60 * 60));
        String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        mainFrame.getContentPane().removeAll();

        gameOverPanel = new GameOverPanel(this, timeString);
        mainFrame.add(gameOverPanel, BorderLayout.CENTER);

        mainFrame.revalidate();
        mainFrame.repaint();

        gameOverPanel.requestFocusInWindow();
    }


    public void showDeathScreen() {
        if (gameFacade != null && gameFacade.getMainFrame() != null) {
            if (gameFacade.getMainFrame().getInteractionPanel() != null) {
                gameFacade.getMainFrame().getInteractionPanel().deactivate();
            }
        }

        currentState = "DEATH";
        
        long elapsedMillis = System.currentTimeMillis() - levelStartTime;
        long seconds = (elapsedMillis / 1000) % 60;
        long minutes = (elapsedMillis / (1000 * 60)) % 60;
        long hours = (elapsedMillis / (1000 * 60 * 60));
        String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        mainFrame.getContentPane().removeAll();

        deathPanel = new DeathPanel(this, timeString);
        mainFrame.add(deathPanel, BorderLayout.CENTER);
        
        mainFrame.revalidate();
        mainFrame.repaint();
        
        deathPanel.requestFocusInWindow();
    }

    public String getCurrentState() {
        return currentState;
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
        
        mainFrameAdapter = new MainFrame(map);
        mainFrameAdapter.setCurrentPlayer(player);
        mainFrameAdapter.setGameContext(new GameContext(mainFrameAdapter, maps));
        
        initializeGamePanel();
    }

        public GameFacade(JFrame hostFrame, MapManager maps, Player player, SaveManager saveManager) {
        this.map = maps.getLevels().get(maps.getCurrMap());
        this.currentPlayer = player;
        
        mainFrameAdapter = new MainFrame(map);
        mainFrameAdapter.setCurrentPlayer(player);
        mainFrameAdapter.setGameContext(new GameContext(mainFrameAdapter, maps, saveManager));
        
        initializeGamePanel();
    }
    
    private void initializeGamePanel() {
        gamePanel = new JPanel(new BorderLayout());
        gamePanel.setBackground(Color.BLACK);
        gamePanel.setFocusable(true);
        
        leftPanelFacade = new LeftPanelFacade(mainFrameAdapter, currentPlayer);
        centerPanelFacade = new CenterPanelFacade(mainFrameAdapter, map);
        
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
        
        JPanel topSection = createTopSection();
        gbc.gridy = 0;
        gbc.weighty = 0.25;
        leftPanel.add(topSection, gbc);
        
        JPanel middleSection = createMiddleSection();
        gbc.gridy = 1;
        gbc.weighty = 0.35;
        leftPanel.add(middleSection, gbc);
        
        JPanel bottomSection = createBottomSection();
        gbc.gridy = 2;
        gbc.weighty = 0.40;
        leftPanel.add(bottomSection, gbc);
    }
    
    private JPanel createTopSection() {
        JPanel topSection = new JPanel(new GridLayout(1, 2, 2, 0));
        topSection.setBackground(Color.BLACK);
        
        inventoryPanel = mainFrame.getInventoryPanel();
        if (inventoryPanel == null) {
            inventoryPanel = new InventoryPanel(player);
        }
        inventoryPanel.setFocusable(true);
        inventoryContainer = createBorderedPanel(inventoryPanel, "INVENTORY");
        topSection.add(inventoryContainer);
        
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

class CenterPanelFacade {
    private JPanel centerPanel;
    private MapPanel mapPanel;
    private InteractionPanel interactionPanel;
    private MainFrame mainFrame;
    private Maps map;
    
    private JPanel mapContainer;
    
    public CenterPanelFacade(MainFrame mainFrame, Maps map) {
        this.mainFrame = mainFrame;
        this.map = map;
        initializeCenterPanel();
    }
    
    private void initializeCenterPanel() {
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.BLACK);
        
        mapPanel = mainFrame.getMapPanel();
        if (mapPanel == null) {
            mapPanel = new MapPanel(map);
        }
        mapPanel.setFocusable(true);
        
        mapContainer = new JPanel(new BorderLayout());
        mapContainer.setBackground(Color.BLACK);
        mapContainer.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2));
        mapContainer.add(mapPanel, BorderLayout.CENTER);
        centerPanel.add(mapContainer, BorderLayout.CENTER);
        
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
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.BLACK);
        bottomPanel.setPreferredSize(new Dimension(0, 200));
        
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
                if (mapContainer != null) {
                    mapContainer.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
                    mapContainer.repaint();
                }
            }

            @Override
            public void componentHidden(java.awt.event.ComponentEvent e) {
                if (mapContainer != null && mapPanel != null && mapPanel.hasFocus()) {
                    mapContainer.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2));
                    mapContainer.repaint();
                }
            }
        });
        bottomPanel.add(interactionContainer, BorderLayout.CENTER);
        
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
        
        /* spritePanel = mainFrame.getSpritePanel();
        if (spritePanel == null) {
            spritePanel = new SpritePanel();
        }
        spritePanel.setPreferredSize(new Dimension(200, 200));
        spritePanel.setVisible(false); / Hidden by default
        bottomPanel.add(spritePanel, BorderLayout.EAST); */
        
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
    
   /*  public void showInteractionPanel(boolean show) {
        interactionPanel.setVisible(show);
        spritePanel.setVisible(show);
        centerPanel.revalidate();
    }
    
    public SpritePanel getSpritePanel() {
        return spritePanel;
    } */
    
    public InteractionPanel getInteractionPanel() {
        return interactionPanel;
    }
}