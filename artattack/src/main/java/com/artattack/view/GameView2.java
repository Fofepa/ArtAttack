package com.artattack.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.artattack.Coordinates;
import com.artattack.Enemy;
import com.artattack.InteractableElement;
import com.artattack.Maps;
import com.artattack.MovieDirector;
import com.artattack.Musician;
import com.artattack.Talk;

public class GameView2 extends JFrame {
    
    private double mainVerticalProportion = 0.3;
    private double inventoriesVerticalProportion = 0.8;
    private double mapHorizontalProportion = 0.75;
    
    // Proporzioni per le divisioni interne
    private double inventorySubDivision = 0.5;
    private double dialogueSubDivision = 0.8;
    
    private MapPanel mapPanel;
    private InteractionPanel interactionPanel;
    private SpritePanel spritePanel;
    
    public GameView2() {
        setTitle("Map");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Pannello alto sinistra diviso in 2 parti uguali
        JPanel inventoryPanel = createBlackPanel("Inventory");
        JPanel movesPanel = createBlackPanel("Moves");
        
        JSplitPane movesInventorySplit = new JSplitPane(
            JSplitPane.VERTICAL_SPLIT,
            inventoryPanel,
            movesPanel
        );
        
        movesInventorySplit.setResizeWeight(inventorySubDivision);
        movesInventorySplit.setDividerSize(2);
        movesInventorySplit.setDividerLocation(inventorySubDivision);
        whiteLineDivider(movesInventorySplit);
        
        // Pannello basso sinistra (singolo)
        JPanel legendPanel = createTestPanel();  // Changed to test panel
        
        // Split verticale per la parte sinistra
        JSplitPane downLeftSplit = new JSplitPane(
            JSplitPane.VERTICAL_SPLIT,
            movesInventorySplit,
            legendPanel
        );
        downLeftSplit.setResizeWeight(inventoriesVerticalProportion);
        downLeftSplit.setDividerSize(2);
        downLeftSplit.setDividerLocation(inventoriesVerticalProportion);
        
        // Pannello alto destra (mappa)
        mapPanel = new MapPanel();
        
        // Pannello basso destra diviso in modo proporzionale
        interactionPanel = new InteractionPanel();  // Changed to InteractionPanel
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
        
        // Split verticale per la parte destra
        JSplitPane rightSplit = new JSplitPane(
            JSplitPane.VERTICAL_SPLIT,
            mapPanel,
            downRightSplit
        );
        rightSplit.setResizeWeight(mapHorizontalProportion);
        rightSplit.setDividerSize(2);
        rightSplit.setDividerLocation(mapHorizontalProportion);
        
        // Split mainVerticalProportion principale
        JSplitPane mainVerticalSplit = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            downLeftSplit,
            rightSplit
        );
        mainVerticalSplit.setResizeWeight(mainVerticalProportion);
        mainVerticalSplit.setDividerSize(2);
        mainVerticalSplit.setDividerLocation(mainVerticalProportion);
        
        whiteLineDivider(downLeftSplit);
        whiteLineDivider(rightSplit);
        whiteLineDivider(mainVerticalSplit);
        
        add(mainVerticalSplit);
        
        // Gestione del focus con Tab
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
    
    // Test panel with buttons to trigger dialogs
    private JPanel createTestPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel.setLayout(new BorderLayout());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        
        // Button for simple dialog
        JButton simpleDialogBtn = new JButton("Simple Dialog");
        simpleDialogBtn.addActionListener(e -> {
            List<String> phrases = Arrays.asList(
                "Hello adventurer!",
                "Welcome to my humble shop.",
                "I have many fine wares to sell.",
                "Take a look around!"
            );
            interactionPanel.showDialog(phrases);
            interactionPanel.requestFocusInWindow();
        });
        
        // Button for choice dialog
        JButton choiceDialogBtn = new JButton("Choice Dialog");
        choiceDialogBtn.addActionListener(e -> {
            List<String> options = Arrays.asList(
                "I want to buy something",
                "I want to sell my items",
                "Tell me about this place",
                "Goodbye"
            );
            
            interactionPanel.showDialogWithChoice(
                "How can I help you today, traveler?", 
                options, 
                (index) -> {
                    System.out.println("User chose option: " + index);
                    // You can trigger another dialog based on choice
                    switch(index) {
                        case 0:
                            interactionPanel.showDialog(Arrays.asList("Here are my wares!", "Take your pick."));
                            break;
                        case 1:
                            interactionPanel.showDialog(Arrays.asList("Let me see what you have.", "Hmm, interesting items..."));
                            break;
                        case 2:
                            interactionPanel.showDialog(Arrays.asList("This is the town of Eldoria.", "A peaceful place for traders."));
                            break;
                        case 3:
                            interactionPanel.showDialog(Arrays.asList("Farewell, friend!", "Come back anytime."));
                            break;
                    }
                    interactionPanel.requestFocusInWindow();
                }
            );
            interactionPanel.requestFocusInWindow();
        });

                // Button to load sprite
        JButton loadSpriteBtn = new JButton("Load Sprite");
        loadSpriteBtn.addActionListener(e -> {
            // Replace with your image path
            spritePanel.loadImage("/home/jacoalf/Scrivania/universita/TerzoAnno/ISS/Codici/Iss-Gruppo-7-ArtAttack/artattack/src/main/java/com/artattack/view/assets/photo_2025-12-14_18-32-01.jpg");
        });
        buttonPanel.add(loadSpriteBtn);
        
        buttonPanel.add(simpleDialogBtn);
        buttonPanel.add(choiceDialogBtn);
        
        JLabel label = new JLabel("Test Dialogs", SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Monospaced", Font.PLAIN, 14));
        
        panel.add(label, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        
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
    
    public void setProportion(double mainVerticalProportion, double inventoryVerticalProportion, double mapHorizontalProportion) {
        this.mainVerticalProportion = mainVerticalProportion;
        this.inventoriesVerticalProportion = inventoryVerticalProportion;
        this.mapHorizontalProportion = mapHorizontalProportion;
    }
    
    public void setInternalProportion(double inventorySubDivision, double dialogueSubDivision) {
        this.inventorySubDivision = inventorySubDivision;
        this.dialogueSubDivision = dialogueSubDivision;
    }
    

    private void loadInitialMap() {

        Maps map = new Maps(
            new Musician(1, '@', "Zappa", new Coordinates(10, 5)),
            new MovieDirector(0, '@', "Lynch", new Coordinates(5, 5)),
            List.of(
                new InteractableElement(0, '$', "Chitarra", new Coordinates(10, 10), List.of(new Talk(interactionPanel, List.of("ciaooo")))),
                new InteractableElement(1, '$', "Batteria", new Coordinates(15, 15), List.of(new Talk(interactionPanel, List.of("ciaoneee"))))
            ),
            List.of(
                new Enemy(0, 'E', "Goblin", new Coordinates(20, 20), 0, 0),
                new Enemy(1, 'E', "Orco", new Coordinates(25, 25),0,0)
            )
        );

        MovieDirector player =
            (MovieDirector) map.getDict().get(new Coordinates(5, 5));

        mapPanel.setMap(map, player);

        mapPanel.revalidate();
        mapPanel.repaint();
            System.out.println("Map loaded! Panel size: " + mapPanel.getWidth() + "x" + mapPanel.getHeight());
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
        GameView2 game = new GameView2();
        game.setVisible(true);
        
        SwingUtilities.invokeLater(() -> {
            game.loadInitialMap();
        });
    });
}
}