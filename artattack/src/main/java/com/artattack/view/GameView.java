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
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class GameView extends JFrame {
    
    private double mainVerticalProportion = 0.3;
    private double inventorysVerticalProportion = 0.8;
    private double mapHorizontalProportion = 0.7;
    
    // Proporzioni per le divisioni interne
    private double inventorySubDivision = 0.5;  // Divisione pannello alto sinistra
    private double dialogueSubDivision = 0.8; // Divisione pannello basso destra
    
    private MapPanel mapPanel;
    
    public GameView() {
        setTitle("Gioco con Mappa");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Pannello alto sinistra diviso in 2 parti uguali
        JPanel inventoryPanel = createBlackPanel("Inventary");
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
        JPanel legendPanel = createBlackPanel("Legend");
        
        // Split verticale per la parte sinistra
        JSplitPane downLeftSplit = new JSplitPane(
            JSplitPane.VERTICAL_SPLIT,
            movesInventorySplit,
            legendPanel
        );
        downLeftSplit.setResizeWeight(inventorysVerticalProportion);
        downLeftSplit.setDividerSize(2);
        downLeftSplit.setDividerLocation(inventorysVerticalProportion);
        
        // Pannello alto destra (mappa)
        mapPanel = new MapPanel();
        
        // Pannello basso destra diviso in modo proporzionale (mainVerticalProportion)
        JPanel dialoguePanel = createBlackPanel("Dialogue");
        JPanel spritePanel = createBlackPanel("Character Sprite");
        
        JSplitPane downRightSplit = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            dialoguePanel,
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
        dialoguePanel.setFocusable(true);

        
        List<JComponent> focusOrder = Arrays.asList(
            mapPanel, 
            inventoryPanel, 
            movesPanel, 
            dialoguePanel);

        for (JComponent c : focusOrder) {
            installTabLoop(c, focusOrder);
        }        
        
        
    }


    private void nextFocus(List<JComponent> order) {
        KeyboardFocusManager kfm =
        KeyboardFocusManager.getCurrentKeyboardFocusManager();

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
        c.getInputMap(JComponent.WHEN_FOCUSED)
            .put(KeyStroke.getKeyStroke("TAB"), "nextFocus");

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
    
    // Aggiungi listener per mostrare/nascondere il focus
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
        this.inventorysVerticalProportion = inventoryVerticalProportion;
        this.mapHorizontalProportion = mapHorizontalProportion;
    }
    
    public void setInternalProportion(double inventorySubDivision, double dialogueSubDivision) {
        this.inventorySubDivision = inventorySubDivision;
        this.dialogueSubDivision = dialogueSubDivision;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
        GameView gioco = new GameView();
        gioco.setVisible(true);

        // focus iniziale sulla mappa
        SwingUtilities.invokeLater(() -> {
            gioco.mapPanel.requestFocusInWindow();
        });
    });
    }
}

