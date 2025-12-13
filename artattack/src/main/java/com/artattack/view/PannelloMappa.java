package com.artattack.view;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.*;

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