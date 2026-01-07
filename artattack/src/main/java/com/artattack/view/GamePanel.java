package com.artattack.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.artattack.ActiveElement;
import com.artattack.ConcreteTurnHandler;
import com.artattack.ConcreteTurnQueue;
import com.artattack.Coordinates;
import com.artattack.Enemy;
import com.artattack.EnemyType;
import com.artattack.InteractableElement;
import com.artattack.MapBuilder;
import com.artattack.Maps;
import com.artattack.MovieDirector;
import com.artattack.Musician;
import com.artattack.Player;
import com.artattack.Talk;
import com.artattack.TestMapBuilder;
import com.artattack.Weapon;

public class GamePanel extends JPanel {

    //Dividers Proportions
    private final double mainVerticalProportion = 0.2;
    private final double inventoriesVerticalProportion = 0.8;
    private final double mapHorizontalProportion = 0.75;
    
    private final double inventorySubDivision = 0.5;
    private final double dialogueSubDivision = 0.8;

    private final double  statsSubDivision = 0.5;
    

    //Panels
    private MapPanel mapPanel;
    private final InteractionPanel interactionPanel;
    private final SpritePanel spritePanel;
    private final TurnPanel turnPanel;
    private final StatsPanel statsPanel;
    private MainFrame mainFrame;
    private MovesPanel movesPanel;

    private Maps currentMap;
    private MovieDirector director;
    private Musician musician;
    private List<Enemy> enemies;
    
    public GamePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setFocusable(true);
        
        //ESC listener
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke("ESCAPE"), "showMenu");
        getActionMap().put("showMenu", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showMenu();
            }
        });
        
        //Inventory and moves Split
        JPanel inventoryPanel = createBlackPanel("Inventory");
        movesPanel = new MovesPanel(); 
        JSplitPane movesInventorySplit = new JSplitPane(
            JSplitPane.VERTICAL_SPLIT,
            inventoryPanel,
            movesPanel
        );
        
        movesInventorySplit.setResizeWeight(inventorySubDivision);
        movesInventorySplit.setDividerSize(2);
        movesInventorySplit.setDividerLocation(inventorySubDivision);
        whiteLineDivider(movesInventorySplit);

        
        //Turn and Stats Split
        statsPanel = new StatsPanel();
        turnPanel = new TurnPanel();

        JSplitPane legendPanelSplit = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            turnPanel,
            createTestPanel()
        );

        legendPanelSplit.setResizeWeight(statsSubDivision);
        legendPanelSplit.setDividerSize(2);
        legendPanelSplit.setDividerLocation(statsSubDivision);
        whiteLineDivider(legendPanelSplit);

        //left most split
        JSplitPane downLeftSplit = new JSplitPane(
            JSplitPane.VERTICAL_SPLIT,
            movesInventorySplit,
            legendPanelSplit
        );
        downLeftSplit.setResizeWeight(inventoriesVerticalProportion);
        downLeftSplit.setDividerSize(2);
        downLeftSplit.setDividerLocation(inventoriesVerticalProportion);
        
        
        
        mapPanel = new MapPanel();
        
        
        //bottom Split
        interactionPanel = new InteractionPanel();
        spritePanel = new SpritePanel();
        
        JSplitPane downRightSplit = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            interactionPanel,
            spritePanel
        );
        downRightSplit.setResizeWeight(dialogueSubDivision);
        downRightSplit.setDividerSize(2);
        downRightSplit.setDividerLocation(dialogueSubDivision);
        whiteLineDivider(downRightSplit);
        
        

        //split between map and interaction and sprite panel
        JSplitPane rightSplit = new JSplitPane(
            JSplitPane.VERTICAL_SPLIT,
            mapPanel,
            downRightSplit
        );
        rightSplit.setResizeWeight(mapHorizontalProportion);
        rightSplit.setDividerSize(2);
        rightSplit.setDividerLocation(mapHorizontalProportion);
        
        
        // Split between map and the leftmost objects
        JSplitPane mainVerticalSplit = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            downLeftSplit,
            rightSplit
        );
        mainVerticalSplit.setResizeWeight(mainVerticalProportion);
        mainVerticalSplit.setDividerSize(2);
        
        
        whiteLineDivider(downLeftSplit);
        whiteLineDivider(rightSplit);
        whiteLineDivider(mainVerticalSplit);
        
        add(mainVerticalSplit);

        //add focus to the map when first opened
        addComponentListener(new java.awt.event.ComponentAdapter() {
            private boolean firstTime = true;
        
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                if (firstTime) {
                    firstTime = false;
                    SwingUtilities.invokeLater(() -> {
                        mapPanel.requestFocusInWindow();
                    });
                }
            }
        });
        
        movesPanel.setMapPanel(mapPanel);
        mapPanel.setMovesPanel(movesPanel);
       
        
        // Focus manager with TAB
        mapPanel.setFocusable(true);
        inventoryPanel.setFocusable(true);
        movesPanel.setFocusable(true);
        interactionPanel.setFocusable(true);
        
        List<JComponent> focusOrder = Arrays.asList(
            mapPanel, 
            inventoryPanel, 
            movesPanel, 
            interactionPanel);
        
        for (JComponent c : focusOrder) {
            installTabLoop(c, focusOrder);
        }
    }
    
    private JPanel createTestPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel.setLayout(new BorderLayout());

        JLabel label = new JLabel("STATS", SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Monospaced", Font.BOLD, 18));

        panel.add(label, BorderLayout.CENTER);

        return panel;
    }


    private JPanel createBlackPanel(String name) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel.setLayout(new BorderLayout());
        
        JLabel label = new JLabel(name, SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Monospaced", Font.PLAIN, 14));
        panel.add(label);
        
        panel.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                panel.setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                panel.setBorder(null);
            }
        });
        
        return panel;
    }
    

    private void nextFocus(List<JComponent> order) {
        KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        Component current = kfm.getFocusOwner();
        int index = order.indexOf(current);
        
        if (index == -1) {
            order.get(0).requestFocusInWindow();
            return;
        }
        
        int next = (index + 1) % order.size();
        order.get(next).requestFocusInWindow();
    }
    
    private void installTabLoop(JComponent c, List<JComponent> order) {
        c.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("TAB"), "nextFocus");
        
        c.getActionMap().put("nextFocus", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextFocus(order);
            }
        });
    }
    
    
    
    private void whiteLineDivider(JSplitPane splitPane) {
        splitPane.setUI(new javax.swing.plaf.basic.BasicSplitPaneUI() {
            @Override
            public javax.swing.plaf.basic.BasicSplitPaneDivider createDefaultDivider() {
                return new javax.swing.plaf.basic.BasicSplitPaneDivider(this) {
                    @Override
                    public void paint(Graphics g) {
                        g.setColor(Color.WHITE);
                        g.fillRect(0, 0, getWidth(), getHeight());
                    }
                };
            }
        });
    }
    
    public void loadInitialMap() {
        Weapon prova = new Weapon("prova", "prova", 2);

        musician = new Musician(1, '@', "Zappa", new Coordinates(10, 5));
        director = new MovieDirector(0, '@', "Lynch", new Coordinates(5, 5));

        enemies = List.of(
            new Enemy(0, 'E', "Employee", new Coordinates(20, 20), EnemyType.EMPLOYEE),
            new Enemy(1, 'E', "Guard", new Coordinates(25, 25), EnemyType.GUARD)
        );

        MapBuilder mapBuilder = new TestMapBuilder();
        mapBuilder.setDimension(36, 150);
        mapBuilder.setPlayerOne(director);
        mapBuilder.setPlayerTwo(musician);
        mapBuilder.setEnemies(enemies);
        mapBuilder.setInteractableElements(List.of(
                new InteractableElement(0, 'G', "Gurlukovich", new Coordinates(10, 10), 
                    List.of(new Talk(interactionPanel, List.of("HELP ME!!", "...","I'M STUCK BETWEEN THE WALLS!!!"))),
                    "artattack\\src\\main\\java\\com\\artattack\\view\\assets\\Gurluk htlm.png", 
                    spritePanel, interactionPanel)
            ));
        mapBuilder.setDict();
        mapBuilder.setTurnQueue();
        mapBuilder.startMap();
        
        currentMap = mapBuilder.getResult();

        
        mapPanel.setMap(currentMap, director, musician);
        
        
        
        
        mapPanel.revalidate();
        mapPanel.repaint();
        mapPanel.requestFocusInWindow();
        
        
        System.out.println("Map loaded! Panel size: " + mapPanel.getWidth() + "x" + mapPanel.getHeight());
    }

    public void loadInitialMapFacade(Maps mapFacade){
        mapPanel.setMap(mapFacade,mapFacade.getPlayerOne() , mapFacade.getPlayerTwo());
        mapPanel.revalidate();
        mapPanel.repaint();
        mapPanel.requestFocusInWindow();
        System.out.println("Map loaded! Panel size: " + mapPanel.getWidth() + "x" + mapPanel.getHeight());
    }


    
    public List<ActiveElement> getActiveElements() {
        List<ActiveElement> elements = new ArrayList<>();
        
        if (musician != null) {
            elements.add(musician);
            System.out.println("Added Musician: " + musician.getName());
        }
        
        if (director != null) {
            elements.add(director);
            System.out.println("Added Director: " + director.getName());
        }
        
        if (enemies != null) {
            elements.addAll(enemies);
            System.out.println("Added " + enemies.size() + " enemies");
        }
        
        return elements;
    }

    public void setTurnSystem(ConcreteTurnQueue queue, ConcreteTurnHandler handler) {
        System.out.println("GamePanel: Setting turn system");
        
        // Passa il sistema a MapPanel
        mapPanel.setTurnSystem(queue, handler, turnPanel);
        
        // Passa il sistema a TurnPanel
        turnPanel.setTurnSystem(queue, handler);
        
        
        if (handler != null && handler.getIndex() > 0) {
            try {
                ActiveElement current = handler.current();
                if (current instanceof Player) {
                    mapPanel.setCurrentPlayer((Player) current);
                    movesPanel.setCurrentPlayer((Player) current);
                    System.out.println("✓ Initial player set to: " + current.getName());
                }
            } catch (Exception e) {
                System.err.println("Error setting initial player: " + e.getMessage());
            }
        }

        // Aggiorna le visualizzazioni
        turnPanel.repaint();
        mapPanel.repaint();
    }

    //Dialog manager
    public void showDialog(List<String> phrases, Runnable onFinished) {
        System.out.println("GamePanel: Showing dialog");
        
        if(onFinished!= null){
            interactionPanel.setOnDialogFinished(onFinished);
        }
        interactionPanel.showDialog(phrases);

        SwingUtilities.invokeLater(()->{
            interactionPanel.activateAndFocus();
        });
    }

    public void showDialogWithChoice(String question, List<String> options, 
                                    InteractionPanel.DialogCallback callback) {
        System.out.println("GamePanel: Showing dialog with choice");
                                    
        interactionPanel.showDialogWithChoice(question, options, callback);
                                    
        SwingUtilities.invokeLater(() -> {
            interactionPanel.activateAndFocus();
        });
    }

    public boolean isDialogActive() {
        return interactionPanel.isDialogActive();
    }


    //Sprite manager

    public void showSprite(String imagePath) {
    System.out.println("GamePanel: Showing sprite: " + imagePath);
        spritePanel.loadImage(imagePath);
    }

    public void clearSprite() {
    System.out.println("GamePanel: Clearing sprite");
        spritePanel.clearImage();
    }
    

    private static class StatsPanel {

        public StatsPanel() {
        }
    }
}