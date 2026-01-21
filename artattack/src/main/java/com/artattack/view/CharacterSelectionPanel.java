package com.artattack.view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

public class CharacterSelectionPanel extends JPanel {
    private MainGUIFacade mainFacade;
    private CharacterType[] types = CharacterType.values();
    
    // Stato della selezione
    private int hoverIndex = 0;
    private CharacterType player1Choice = null;
    private boolean isPlayer1Turn = true;
    
    public CharacterSelectionPanel(MainGUIFacade mainFacade) {
        this.mainFacade = mainFacade;
        initializePanel();
        setupInput();
    }

    private void initializePanel() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        setFocusable(true);
    }

    private void setupInput() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT, KeyEvent.VK_A -> {
                        moveSelection(-1);
                    }
                    case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> {
                        moveSelection(1);
                    }
                    case KeyEvent.VK_ENTER, KeyEvent.VK_SPACE -> {
                        confirmSelection();
                    }
                    case KeyEvent.VK_ESCAPE -> {
                        if (!isPlayer1Turn) {
                            // Torna alla selezione P1
                            isPlayer1Turn = true;
                            player1Choice = null;
                            repaint();
                        } else {
                            // Torna al menu principale
                            mainFacade.showMenu();
                        }
                    }
                }
            }
        });
    }

    private void moveSelection(int delta) {
        hoverIndex = (hoverIndex + delta + types.length) % types.length;
        repaint();
    }

    private void confirmSelection() {
        CharacterType selected = types[hoverIndex];

        // Se è il turno del P2 e sta provando a scegliere il personaggio di P1
        if (!isPlayer1Turn && selected == player1Choice) {
            // Feedback visivo (es. suono errore)
            System.out.println("Character already taken!");
            return;
        }

        if (isPlayer1Turn) {
            player1Choice = selected;
            isPlayer1Turn = false;
            // Reset cursore o spostalo automaticamente sul primo disponibile
            if (types[0] == player1Choice && types.length > 1) hoverIndex = 1;
            else hoverIndex = 0;
            
            System.out.println("P1 selected: " + player1Choice);
            repaint();
        } else {
            // Selezione finita, avvia il gioco
            System.out.println("P2 selected: " + selected);
            mainFacade.finalizeGameSetup(player1Choice, selected);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // 1. Disegna Titolo
        String title = isPlayer1Turn ? "PLAYER 1: CHOOSE CHARACTER" : "PLAYER 2: CHOOSE CHARACTER";
        g2.setFont(new Font("Monospaced", Font.BOLD, 40));
        g2.setColor(isPlayer1Turn ? Color.GREEN : Color.MAGENTA);
        drawCenteredString(g2, title, width / 2, 80);

        // 2. Disegna le carte dei personaggi
        int cardWidth = 250;
        int cardHeight = 350;
        int gap = 50;
        int totalWidth = (types.length * cardWidth) + ((types.length - 1) * gap);
        int startX = (width - totalWidth) / 2;
        int startY = (height - cardHeight) / 2;

        for (int i = 0; i < types.length; i++) {
            CharacterType type = types[i];
            int x = startX + (i * (cardWidth + gap));
            
            drawCharacterCard(g2, type, x, startY, cardWidth, cardHeight, i == hoverIndex);
        }
        
        // 3. Istruzioni
        g2.setFont(new Font("Monospaced", Font.ITALIC, 16));
        g2.setColor(Color.GRAY);
        String instruction = isPlayer1Turn ? "Press ENTER to confirm" : "Cannot select P1's character";
        drawCenteredString(g2, instruction, width / 2, height - 100);
    }

    private void drawCharacterCard(Graphics2D g2, CharacterType type, int x, int y, int w, int h, boolean isHovered) {
        boolean isTaken = (type == player1Choice);

        // --- 1. SFONDO E BORDO ---
        if (isTaken) {
            g2.setColor(new Color(20, 20, 20)); 
        } else if (isHovered) {
            g2.setColor(new Color(45, 45, 45)); 
        } else {
            g2.setColor(new Color(30, 30, 30)); 
        }
        g2.fillRoundRect(x, y, w, h, 15, 15);

        if (isHovered) {
            g2.setColor(isPlayer1Turn ? Color.CYAN : Color.MAGENTA);
            g2.setStroke(new BasicStroke(3));
        } else if (isTaken) {
            g2.setColor(Color.DARK_GRAY);
            g2.setStroke(new BasicStroke(2));
        } else {
            g2.setColor(Color.GRAY);
            g2.setStroke(new BasicStroke(1));
        }
        g2.drawRoundRect(x, y, w, h, 15, 15);

        // --- 2. INTESTAZIONE (Nome e Descrizione) ---
        int currentY = y + 40;
        
        // Nome
        g2.setColor(isTaken ? Color.GRAY : Color.WHITE);
        g2.setFont(new Font("Monospaced", Font.BOLD, 22));
        drawCenteredString(g2, type.getName(), x + w / 2, currentY);
        
        currentY += 25;
        
        // Descrizione
        g2.setFont(new Font("Monospaced", Font.ITALIC, 12));
        g2.setColor(Color.LIGHT_GRAY);
        drawCenteredString(g2, type.getDescription(), x + w / 2, currentY);

        // Linea divisoria
        currentY += 15;
        g2.setColor(Color.DARK_GRAY);
        g2.drawLine(x + 20, currentY, x + w - 20, currentY);

        // --- 3. STATISTICHE ---
        currentY += 25;
        int barX = x + 30;
        int barWidth = w - 60; // Larghezza barre

        // Arma
        g2.setFont(new Font("Monospaced", Font.PLAIN, 14));
        g2.setColor(Color.WHITE);
        g2.drawString("Weapon: " + type.getWeaponName(), barX, currentY);
        
        currentY += 25;

        // Barra HP (Immaginando che 30 sia il massimo assoluto nel gioco per scalare la barra)
        drawStatBar(g2, "HP", type.getMaxHP(), 30, Color.RED, barX, currentY, barWidth);
        
        currentY += 30;

        // Barra Velocità (Immaginando che 10 sia il massimo assoluto)
        drawStatBar(g2, "Speed", type.getSpeed(), 10, Color.YELLOW, barX, currentY, barWidth);

        // --- 4. STATO (TAKEN / P1 / P2) ---
        if (isTaken) {
            g2.setColor(new Color(255, 0, 0, 100)); // Rosso semitrasparente
            g2.setFont(new Font("Monospaced", Font.BOLD, 40));
            AffineTransform orig = g2.getTransform();
            
            // Ruota la scritta TAKEN
            g2.rotate(Math.toRadians(-20), x + w/2, y + h/2);
            drawCenteredString(g2, "TAKEN", x + w / 2, y + h / 2);
            g2.setTransform(orig);
        }
        
        if (isHovered && !isTaken) {
            g2.setColor(isPlayer1Turn ? Color.CYAN : Color.MAGENTA);
            String label = isPlayer1Turn ? "P1 Select" : "P2 Select";
            g2.setFont(new Font("Monospaced", Font.BOLD, 14));
            drawCenteredString(g2, label, x + w/2, y + h - 20);
        }
    }

    private void drawStatBar(Graphics2D g2, String label, int value, int max, Color color, int x, int y, int width) {
        int height = 10;
        
        // Etichetta (es. "HP: 20")
        g2.setColor(Color.LIGHT_GRAY);
        g2.setFont(new Font("Monospaced", Font.PLAIN, 10));
        g2.drawString(label + " " + value, x, y - 3);

        // Sfondo barra (grigio scuro)
        g2.setColor(new Color(20, 20, 20));
        g2.fillRect(x, y, width, height);

        // Parte piena
        int fillWidth = (int) (((double) value / max) * width);
        if (fillWidth > width) fillWidth = width;
        
        g2.setColor(color);
        g2.fillRect(x, y, fillWidth, height);
        
        // Bordo sottile
        g2.setColor(new Color(60, 60, 60));
        g2.drawRect(x, y, width, height);
    }

    private void drawCenteredString(Graphics g, String text, int x, int y) {
        FontMetrics metrics = g.getFontMetrics();
        int dX = x - (metrics.stringWidth(text) / 2);
        int dY = y + (metrics.getAscent() - metrics.getDescent()) / 2; // Vertical center approx
        g.drawString(text, dX, dY);
    }
}