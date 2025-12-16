package com.artattack.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.artattack.Coordinates;
import com.artattack.InteractableElement;
import com.artattack.Maps;
import com.artattack.MovementStrategy;
import com.artattack.MovieDirector;
import com.artattack.Musician;

class MapPanel extends JPanel {
    private MovementStrategy movementStrategy;
    /* private boolean isSelected = false; */
    private boolean showCursor = true;
    private Timer timerBlink;
    
    public MapPanel() {
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyboardInputs(); //è execute di MovementStrategy
        Maps t = new Maps(new Musician(1, '@', "Zappa", new Coordinates(0, 1)),
         new MovieDirector(0, '@', "Lynch", new Coordinates(5, 5)), List.of(
            new InteractableElement(0, '$', "Chitarra", new Coordinates(10, 10),null),
            new InteractableElement(1, '$', "Batteria", new Coordinates(15, 15),null)
         )/*, List.of(
            new Enemy(0, 'E', "Goblin", new Coordinates(20, 20)),
            new Enemy(1, 'E', "Orco", new Coordinates(25, 25))
         )*/);
        movementStrategy = new MovementStrategy(t,(MovieDirector)t.getDict().get(new Coordinates(5,5)));

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
            if (movementStrategy.getSelectedState()) {
                showCursor = !showCursor;
                repaint();
            }
        });
        timerBlink.start();
    }
    
    private void addKeyboardInputs() {
        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                int dx = 0, dy = 0;
                
                switch (e.getKeyCode()) {
                    case java.awt.event.KeyEvent.VK_UP, java.awt.event.KeyEvent.VK_W -> dy = -1;
                    case java.awt.event.KeyEvent.VK_DOWN, java.awt.event.KeyEvent.VK_S -> dy = 1;
                    case java.awt.event.KeyEvent.VK_LEFT, java.awt.event.KeyEvent.VK_A -> dx = -1;
                    case java.awt.event.KeyEvent.VK_RIGHT, java.awt.event.KeyEvent.VK_D -> dx = 1;
                    case java.awt.event.KeyEvent.VK_ENTER -> {
                        if (movementStrategy.getSelectedState()) {
                            movementStrategy.acceptMovement();
                            repaint();
                        }
                        return;
                    }
                }
                
                movementStrategy.execute(dx, dy);
                repaint();
            }
        });
    }
    
    
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    
        Font font = new Font("Monospaced", Font.BOLD, 16);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        
        int charWidth = fm.charWidth('W');
        int charHeight = fm.getHeight();
        
        int mapLength = this.movementStrategy.getMap().getColumns() * charWidth;
        int mapHeight = this.movementStrategy.getMap().getRows() * charHeight;
        
        int offsetX = (getWidth() - mapLength) / 2;
        int offsetY = (getHeight() - mapHeight) / 2;
        
        for (int i = 0; i < this.movementStrategy.getMap().getRows(); i++) {
            for (int j = 0; j < this.movementStrategy.getMap().getColumns(); j++) {
                char c = this.movementStrategy.getMap().getMapMatrix()[i][j];
                
                switch (c) {
                    case '#' -> g.setColor(Color.GRAY);
                    case '@' -> g.setColor(Color.GREEN);
                    case 'E' -> g.setColor(Color.RED);
                    case '$' -> g.setColor(Color.YELLOW);
                    case '.' -> g.setColor(new Color(50, 50, 50));
                    default -> g.setColor(Color.WHITE);
                }
                
                g.drawString(String.valueOf(c), 
                    j * charWidth + offsetX, 
                    i * charHeight + offsetY + charHeight);
            }
        }

        int cursorX = movementStrategy.getCursor().getX();
        int cursorY = movementStrategy.getCursor().getY();
        int playerX = movementStrategy.getPlayer().getCoordinates().getX();
        int playerY = movementStrategy.getPlayer().getCoordinates().getY();
        
        if (movementStrategy.getSelectedState() && showCursor && 
            (cursorX != playerX || cursorY != playerY)) {
            g.setColor(Color.CYAN);
            g.drawString("*", 
                cursorX * charWidth + offsetX, 
                cursorY * charHeight + offsetY + charHeight);
        }
    }

}