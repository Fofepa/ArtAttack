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
    
    private double propOrizzontale = 0.3;
    private double propVerticaleS = 0.8;
    private double propVerticaleD = 0.7;
    
    // Proporzioni per le divisioni interne
    private double propAltoSx = 0.5;  // Divisione pannello alto sinistra
    private double propBassoDx = 0.8; // Divisione pannello basso destra
    
    private MapPanel pannelloMappa;
    
    public GameView() {
        setTitle("Gioco con Mappa");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Pannello alto sinistra diviso in 2 parti uguali
        JPanel panelAltoSxTop = creaPannelloNero("Inventario Top");
        JPanel panelAltoSxBottom = creaPannelloNero("Inventario Bottom");
        
        JSplitPane splitAltoSx = new JSplitPane(
            JSplitPane.VERTICAL_SPLIT,
            panelAltoSxTop,
            panelAltoSxBottom
        );
        splitAltoSx.setResizeWeight(propAltoSx);
        splitAltoSx.setDividerSize(2);
        splitAltoSx.setDividerLocation(propAltoSx);
        personalizzaDivider(splitAltoSx);
        
        // Pannello basso sinistra (singolo)
        JPanel panelBassoSx = creaPannelloNero("Statistiche");
        
        // Split verticale per la parte sinistra
        JSplitPane splitSinistra = new JSplitPane(
            JSplitPane.VERTICAL_SPLIT,
            splitAltoSx,
            panelBassoSx
        );
        splitSinistra.setResizeWeight(propVerticaleS);
        splitSinistra.setDividerSize(2);
        splitSinistra.setDividerLocation(propVerticaleS);
        
        // Pannello alto destra (mappa)
        pannelloMappa = new MapPanel();
        
        // Pannello basso destra diviso in modo proporzionale (ORIZZONTALE)
        JPanel panelBassoDxLeft = creaPannelloNero("Dialogo");
        JPanel panelBassoDxRight = creaPannelloNero("Sprite Personaggio");
        
        JSplitPane splitBassoDx = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            panelBassoDxLeft,
            panelBassoDxRight
        );
        splitBassoDx.setResizeWeight(propBassoDx);
        splitBassoDx.setDividerSize(2);
        splitBassoDx.setDividerLocation(propBassoDx);
        personalizzaDivider(splitBassoDx);
        
        // Split verticale per la parte destra
        JSplitPane splitDestra = new JSplitPane(
            JSplitPane.VERTICAL_SPLIT,
            pannelloMappa,
            splitBassoDx
        );
        splitDestra.setResizeWeight(propVerticaleD);
        splitDestra.setDividerSize(2);
        splitDestra.setDividerLocation(propVerticaleD);
        
        // Split orizzontale principale
        JSplitPane splitPrincipale = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            splitSinistra,
            splitDestra
        );
        splitPrincipale.setResizeWeight(propOrizzontale);
        splitPrincipale.setDividerSize(2);
        splitPrincipale.setDividerLocation(propOrizzontale);
        
        personalizzaDivider(splitSinistra);
        personalizzaDivider(splitDestra);
        personalizzaDivider(splitPrincipale);
        
        add(splitPrincipale);
        
        // Gestione del focus con Tab
        pannelloMappa.setFocusable(true);
        panelAltoSxTop.setFocusable(true);
        panelAltoSxBottom.setFocusable(true);
        panelBassoDxLeft.setFocusable(true);

        
        List<JComponent> focusOrder = Arrays.asList(
            pannelloMappa, 
            panelAltoSxTop, 
            panelAltoSxBottom, 
            panelBassoDxLeft);

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

    
    
    private JPanel creaPannelloNero(String etichetta) {
    JPanel panel = new JPanel();
    panel.setBackground(Color.BLACK);
    panel.setLayout(new BorderLayout());
    
    JLabel label = new JLabel(etichetta, SwingConstants.CENTER);
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
    
    private void personalizzaDivider(JSplitPane splitPane) {
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
    
    public void setProporzioni(double orizzontale, double verticaleSx, double verticaleDx) {
        this.propOrizzontale = orizzontale;
        this.propVerticaleS = verticaleSx;
        this.propVerticaleD = verticaleDx;
    }
    
    public void setProporzioniInterne(double altoSx, double bassoDx) {
        this.propAltoSx = altoSx;
        this.propBassoDx = bassoDx;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
        GameView gioco = new GameView();
        gioco.setVisible(true);

        // focus iniziale sulla mappa
        SwingUtilities.invokeLater(() -> {
            gioco.pannelloMappa.requestFocusInWindow();
        });
    });
    }
}

