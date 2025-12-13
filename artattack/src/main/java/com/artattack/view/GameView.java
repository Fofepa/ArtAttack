package com.artattack.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
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
import javax.swing.Timer;

public class GameView extends JFrame {
    
    private double propOrizzontale = 0.3;
    private double propVerticaleS = 0.8;
    private double propVerticaleD = 0.7;
    
    // Proporzioni per le divisioni interne
    private double propAltoSx = 0.5;  // Divisione pannello alto sinistra
    private double propBassoDx = 0.8; // Divisione pannello basso destra
    
    private PannelloMappa pannelloMappa;
    
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
        pannelloMappa = new PannelloMappa();
        
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

// Pannello per visualizzare la mappa
class PannelloMappa extends JPanel {
    private char[][] mappa;
    private int righe = 20;
    private int colonne = 40;
    private int giocatoreX = 5;
    private int giocatoreY = 5;
    private int cursoreX = 5;
    private int cursoreY = 5;
    private boolean modalitaSelezione = false;
    private boolean mostraCursore = true;
    private Timer timerBlink;
    
    public PannelloMappa() {
        setBackground(Color.BLACK);
        setFocusable(true);
        inizializzaMappa();
        aggiungiControlliTastiera();


        addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
            }
        
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                setBorder(null);
            }
        });
        
        timerBlink = new Timer(500, e -> {
            if (modalitaSelezione) {
                mostraCursore = !mostraCursore;
                repaint();
            }
        });
        timerBlink.start();
    }
    
    private void aggiungiControlliTastiera() {
        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                int dx = 0, dy = 0;
                
                switch (e.getKeyCode()) {
                    case java.awt.event.KeyEvent.VK_UP:
                    case java.awt.event.KeyEvent.VK_W:
                        dy = -1;
                        break;
                    case java.awt.event.KeyEvent.VK_DOWN:
                    case java.awt.event.KeyEvent.VK_S:
                        dy = 1;
                        break;
                    case java.awt.event.KeyEvent.VK_LEFT:
                    case java.awt.event.KeyEvent.VK_A:
                        dx = -1;
                        break;
                    case java.awt.event.KeyEvent.VK_RIGHT:
                    case java.awt.event.KeyEvent.VK_D:
                        dx = 1;
                        break;
                    case java.awt.event.KeyEvent.VK_ENTER:
                        if (modalitaSelezione) {
                            confermaMovimento();
                        }
                        return;
                }
                
                if (dx != 0 || dy != 0) {
                    if (!modalitaSelezione) {
                        modalitaSelezione = true;
                        cursoreX = giocatoreX;
                        cursoreY = giocatoreY;
                    }
                    
                    int nuovoCursoreX = cursoreX + dx;
                    int nuovoCursoreY = cursoreY + dy;
                    
                    if (Math.abs(nuovoCursoreX - giocatoreX) <= 1 && 
                        Math.abs(nuovoCursoreY - giocatoreY) <= 1 &&
                        nuovoCursoreX >= 0 && nuovoCursoreX < colonne &&
                        nuovoCursoreY >= 0 && nuovoCursoreY < righe) {
                        
                        cursoreX = nuovoCursoreX;
                        cursoreY = nuovoCursoreY;
                        repaint();
                    }
                }
            }
        });
    }
    
    private void confermaMovimento() {
        if (mappa[cursoreY][cursoreX] != '#') {
            mappa[giocatoreY][giocatoreX] = '.';
            giocatoreX = cursoreX;
            giocatoreY = cursoreY;
            mappa[giocatoreY][giocatoreX] = '@';
        }
        modalitaSelezione = false;
        repaint();
    }
    
    private void inizializzaMappa() {
        mappa = new char[righe][colonne];
        
        for (int i = 0; i < righe; i++) {
            for (int j = 0; j < colonne; j++) {
                mappa[i][j] = '.';
            }
        }
        
        for (int i = 0; i < colonne; i++) {
            mappa[0][i] = '#';
            mappa[righe - 1][i] = '#';
        }
        for (int i = 0; i < righe; i++) {
            mappa[i][0] = '#';
            mappa[i][colonne - 1] = '#';
        }
        
        for (int i = 5; i < 15; i++) {
            mappa[10][i] = '#';
        }
        
        giocatoreX = 5;
        giocatoreY = 5;
        mappa[giocatoreY][giocatoreX] = '@';
        
        mappa[8][15] = 'E';
        mappa[12][20] = 'E';
        
        mappa[3][30] = '$';
        mappa[15][25] = '$';
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Font font = new Font("Monospaced", Font.BOLD, 16);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        
        int charWidth = fm.charWidth('W');
        int charHeight = fm.getHeight();
        
        int mappaLarghezza = colonne * charWidth;
        int mappaAltezza = righe * charHeight;
        
        int offsetX = (getWidth() - mappaLarghezza) / 2;
        int offsetY = (getHeight() - mappaAltezza) / 2;
        
        for (int i = 0; i < righe; i++) {
            for (int j = 0; j < colonne; j++) {
                char c = mappa[i][j];
                
                switch (c) {
                    case '#':
                        g.setColor(Color.GRAY);
                        break;
                    case '@':
                        g.setColor(Color.GREEN);
                        break;
                    case 'E':
                        g.setColor(Color.RED);
                        break;
                    case '$':
                        g.setColor(Color.YELLOW);
                        break;
                    case '.':
                        g.setColor(new Color(50, 50, 50));
                        break;
                    default:
                        g.setColor(Color.WHITE);
                }
                
                g.drawString(String.valueOf(c), 
                    j * charWidth + offsetX, 
                    i * charHeight + offsetY + charHeight);
            }
        }
        
        if (modalitaSelezione && mostraCursore && 
            (cursoreX != giocatoreX || cursoreY != giocatoreY)) {
            g.setColor(Color.CYAN);
            g.drawString("*", 
                cursoreX * charWidth + offsetX, 
                cursoreY * charHeight + offsetY + charHeight);
        }
    }
    
    public void setCell(int riga, int colonna, char carattere) {
        if (riga >= 0 && riga < righe && colonna >= 0 && colonna < colonne) {
            mappa[riga][colonna] = carattere;
            repaint();
        }
    }
    
    public char getCell(int riga, int colonna) {
        if (riga >= 0 && riga < righe && colonna >= 0 && colonna < colonne) {
            return mappa[riga][colonna];
        }
        return ' ';
    }
    
    public char[][] getMappa() {
        return mappa;
    }
    
    public int getRighe() {
        return righe;
    }
    
    public int getColonne() {
        return colonne;
    }
}