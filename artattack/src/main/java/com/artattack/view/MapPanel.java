package com.artattack.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.artattack.ActiveElement;
import com.artattack.ConcreteTurnHandler;
import com.artattack.ConcreteTurnQueue;
import com.artattack.Enemy;
import com.artattack.InteractionStrategy;
import com.artattack.Maps;
import com.artattack.MovementStrategy;
import com.artattack.MovieDirector;
import com.artattack.Musician;
import com.artattack.Player;

class MapPanel extends JPanel {
    private MovementStrategy movementStrategy;
    private InteractionStrategy interactionStrategy;
    private boolean showCursor = true;
    private Timer timerBlink;
    private ConcreteTurnQueue turnQueue;
    private ConcreteTurnHandler turnHandler;
    private TurnPanel turnPanel;
    
    public MapPanel() {
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyboardInputs();
        addFocusBorder();
        initCursorTimer();
    }

    public void setMap(Maps map, MovieDirector director, Musician musician) {
        this.movementStrategy = new MovementStrategy(map, director);
        this.interactionStrategy = new InteractionStrategy(movementStrategy);
        showCursor = true;
        repaint();
        requestFocusInWindow();
    }
    
    public void setTurnSystem(ConcreteTurnQueue queue, ConcreteTurnHandler handler, 
                             TurnPanel turnPanel) {
        this.turnQueue = queue;
        this.turnHandler = handler;
        this.turnPanel = turnPanel;
        
        System.out.println("=== MapPanel Turn System Set ===");
        System.out.println("Queue: " + (queue != null ? "OK" : "NULL"));
        System.out.println("Handler: " + (handler != null ? "OK" : "NULL"));
        if (handler != null && handler.getIndex() > 0) {
            System.out.println("Current turn: " + handler.current().getName());
        }
    }
    
    private boolean isPlayerTurn() {
        // Se non c'è sistema di turni, permetti sempre
        if (turnHandler == null || turnQueue == null) {
            return true;
        }
        
        try {
            // Ottieni l'elemento del turno corrente
            ActiveElement current = turnHandler.current();
            Player player = movementStrategy.getPlayer();
            
            System.out.println("Checking turn - Current: " + current.getName() + 
                             ", Player: " + player.getName());
            
            // Verifica se il turno corrente è del giocatore
            return current.equals(player);
            
        } catch (Exception e) {
            System.err.println("Error checking turn: " + e.getMessage());
            return true; // In caso di errore, permetti il movimento
        }
    }
    
    private void endTurn() {
    if (turnHandler == null || turnQueue == null) {
        System.out.println("No turn system to end turn");
        return;
    }
    
    System.out.println("=== Ending Turn ===");
    
    // Passa al prossimo turno
    if (turnHandler.hasNext()) {
        ActiveElement next = turnHandler.next();
        System.out.println("Next turn: " + next.getName());
        
        // Aggiorna il TurnPanel
        if (turnPanel != null) {
            turnPanel.updateTurnDisplay();
        }
        
        // Se il prossimo è un nemico, esegui la sua IA
        if (next instanceof Enemy) {
            handleEnemyTurn((Enemy) next);
        } else if (next instanceof Player) {
            // IMPORTANTE: Aggiorna il giocatore attivo nel MovementStrategy
            movementStrategy.setPlayer((Player) next);
            System.out.println("✓ Switched control to: " + next.getName());
            
            // Richiedi il focus per permettere l'input
            requestFocusInWindow();
        }
        
        repaint();
    } else {
        System.out.println("No more turns, resetting");
        turnHandler.resetIndex();
        endTurn(); 
    }
}
    
    private void handleEnemyTurn(Enemy enemy) {
        System.out.println("Enemy turn: " + enemy.getName());
        
        setFocusable(false);
        
       
        Timer enemyTimer = new Timer(1500, e -> {
            System.out.println(enemy.getName() + " finishes turn");
            
            
            setFocusable(true);
            requestFocusInWindow();
            
            
            endTurn();
        });
        enemyTimer.setRepeats(false);
        enemyTimer.start();
    }
    
    private void initCursorTimer() {
        timerBlink = new Timer(500, e -> {
            if (movementStrategy != null && movementStrategy.getSelectedState()) {
                showCursor = !showCursor;
                repaint();
            }
        });
        timerBlink.start();
    }

    private void addFocusBorder() {
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
    }

    private void addKeyboardInputs() {
        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (movementStrategy == null) {
                    System.out.println("MovementStrategy is null!");
                    return;
                }
                
                
                if (!isPlayerTurn()) {
                    System.out.println("⚠️ Not your turn! Wait...");
                    return;
                }

                int dx = 0, dy = 0;

                switch (e.getKeyCode()) {
                    case java.awt.event.KeyEvent.VK_UP, java.awt.event.KeyEvent.VK_W -> {
                        dy = -1;
                        System.out.println("UP pressed");
                    }
                    case java.awt.event.KeyEvent.VK_DOWN, java.awt.event.KeyEvent.VK_S -> {
                        dy = 1;
                        System.out.println("DOWN pressed");
                    }
                    case java.awt.event.KeyEvent.VK_LEFT, java.awt.event.KeyEvent.VK_A -> {
                        dx = -1;
                        System.out.println("LEFT pressed");
                    }
                    case java.awt.event.KeyEvent.VK_RIGHT, java.awt.event.KeyEvent.VK_D -> {
                        dx = 1;
                        System.out.println("RIGHT pressed");
                    }

                    case java.awt.event.KeyEvent.VK_ENTER -> {
                        System.out.println("ENTER pressed - Confirming movement");
                        if (movementStrategy.getSelectedState()) {
                            movementStrategy.acceptMovement();
                            endTurn(); 
                            repaint();
                        }
                        return;
                    }

                    case java.awt.event.KeyEvent.VK_E -> {
                        System.out.println("E pressed - Interaction");
                        if (movementStrategy.getSelectedState()) {
                            interactionStrategy.acceptInteraction();
                        }
                        return;
                    }
                    
                    case java.awt.event.KeyEvent.VK_SPACE -> {
                        System.out.println("SPACE pressed - Skip turn");
                        endTurn(); 
                        return;
                    }
                }

                if (dx != 0 || dy != 0) {
                    System.out.println("Moving cursor: dx=" + dx + ", dy=" + dy);
                    movementStrategy.execute(dx, dy);
                    repaint();
                }
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (movementStrategy == null) {
            return;
        }

        final int BASE_FONT_SIZE = 16;
        Font baseFont = new Font("Monospaced", Font.BOLD, BASE_FONT_SIZE);
        g.setFont(baseFont);
        FontMetrics fmBase = g.getFontMetrics();
        
        int baseCharWidth = fmBase.charWidth('W');
        int baseCharHeight = fmBase.getHeight();
        
        int mapRequiredWidth = this.movementStrategy.getMap().getColumns() * baseCharWidth;
        int mapRequiredHeight = this.movementStrategy.getMap().getRows() * baseCharHeight;
        
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        
        double scaleX = (double) panelWidth / mapRequiredWidth;
        double scaleY = (double) panelHeight / mapRequiredHeight;
        double scaleFactor = Math.min(scaleX, scaleY);

        int scaledFontSize = (int) (BASE_FONT_SIZE * scaleFactor);
        if (scaledFontSize < 1) scaledFontSize = 1;

        Font scaledFont = new Font("Monospaced", Font.BOLD, scaledFontSize);
        g.setFont(scaledFont);
        FontMetrics fmScaled = g.getFontMetrics();

        int charWidth = fmScaled.charWidth('W');
        int charHeight = fmScaled.getHeight();
        
        int mapLength = this.movementStrategy.getMap().getColumns() * charWidth;
        int mapHeight = this.movementStrategy.getMap().getRows() * charHeight;
        
        int offsetX = (panelWidth - mapLength) / 2;
        int offsetY = (panelHeight - mapHeight) / 2;
        
        // Disegno della mappa
        for (int i = 0; i < this.movementStrategy.getMap().getRows(); i++) {
            for (int j = 0; j < this.movementStrategy.getMap().getColumns(); j++) {
                char c = this.movementStrategy.getMap().getMapMatrix()[i][j];

                switch (c) {
                    case '#' -> g.setColor(Color.GRAY);
                    case '@' -> g.setColor(Color.GREEN);
                    case 'E' -> g.setColor(Color.RED);
                    case '$' -> g.setColor(Color.YELLOW);
                    case '.' -> g.setColor(new Color(50, 50, 50));
                    case 'G' -> g.setColor(Color.ORANGE);
                    default -> g.setColor(Color.WHITE);
                }

                g.drawString(String.valueOf(c), 
                    j * charWidth + offsetX, 
                    i * charHeight + offsetY + charHeight);
            }
        }

        // Disegno del cursore
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

    public void setCurrentPlayer(Player player) {
        if (movementStrategy != null) {
            movementStrategy.setPlayer(player);
            System.out.println("Current player changed to: " + player.getName());
        }
    }
}