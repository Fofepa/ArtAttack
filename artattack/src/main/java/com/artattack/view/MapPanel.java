package com.artattack.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import com.artattack.inputcontroller.CombatStrategy;
import com.artattack.level.Coordinates;
import com.artattack.level.Maps;
import com.artattack.mapelements.Player;
import com.artattack.moves.Move;
import com.artattack.moves.Weapon;

/**
 * MapPanel - Displays the game map with all elements
 */
public class MapPanel extends JPanel {
    private Maps map;
    private Coordinates movementCursor;
    private List<Coordinates> moveArea;
    private List<Coordinates> attackArea;

    private static final int CELL_SIZE = 12;

    // Blinking cursor properties
    private boolean cursorVisible = true;
    private Timer blinkTimer;
    
    public MapPanel(Maps map) {
        this.map = map;
        setBackground(Color.BLACK);
        setFocusable(true);

        // Initialize the blinking timer
        initializeBlinkTimer();
    }

    /**
     * Initializes the timer for cursor blinking
     */
    private void initializeBlinkTimer() {
        blinkTimer = new Timer(350, e -> {
            cursorVisible = !cursorVisible;
            repaint();
        });
        blinkTimer.start();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (map == null || map.getMapMatrix() == null) {
            return;
        }
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        char[][] matrix = map.getMapMatrix();

        // 1. CALCOLO DELL'OFFSET PER CENTRARE
        // Calcoliamo quanto è grande la mappa in pixel
        int mapPixelWidth = map.getWidth() * CELL_SIZE;
        int mapPixelHeight = map.getHeight() * CELL_SIZE;

        // Calcoliamo il punto di partenza (Top-Left) per centrarla nel pannello
        int startX = (getWidth() - mapPixelWidth) / 2;
        int startY = (getHeight() - mapPixelHeight) / 2;

        // Ciclo di rendering
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                char c = matrix[x][y]; // Nota: verifica se è matrix[y][x] o [x][y] in base alla tua impl.

                // Calcoliamo la posizione esatta in pixel di QUESTA cella
                int px = startX + (x * CELL_SIZE);
                int py = startY + (y * CELL_SIZE);

                // --- CURSORE MOVIMENTO ---
                boolean isCursorPosition = movementCursor != null && 
                                           movementCursor.getX() == x && 
                                           movementCursor.getY() == y;
                
                if (isCursorPosition && cursorVisible) {
                    g.setColor(Color.GREEN);
                    // Disegna il cursore leggermente spostato per centrarlo nel carattere
                    g.drawString("*", px, py + CELL_SIZE); 
                }
                else {
                    // --- COLORI MAPPA ---
                    switch (c) {
                        case '#' -> g.setColor(Color.GRAY);
                        case '@' -> g.setColor(Color.CYAN);
                        case '.' -> g.setColor(new Color(50, 50, 50));
                        case 'E' -> g.setColor(Color.RED);
                        case 'I' -> g.setColor(Color.YELLOW);
                        default -> g.setColor(Color.WHITE);
                    } 
                }
                
                // Disegna il carattere. 
                // Nota: drawString disegna dalla baseline (basso), quindi aggiungiamo CELL_SIZE a Y
                g.drawString(String.valueOf(c), px, py + CELL_SIZE);
            }
        }
        
        // --- RENDERING OVERLAYS (Cursori e Aree) ---
        // Usiamo helper method per evitare codice duplicato nel calcolo pixel
        
        // 1. Cursore quadrato verde (bordi)
        if (movementCursor != null) {
            g.setColor(Color.GREEN);
            drawCellRect(g, movementCursor.getX(), movementCursor.getY(), startX, startY, false);
        }
        
        // 2. Area Movimento (Verde trasparente)
        if (moveArea != null) {
            g.setColor(new Color(0, 255, 0, 50));
            for (Coordinates coord : moveArea) {
                drawCellRect(g, coord.getX(), coord.getY(), startX, startY, true);
            }
        }
        
        // 3. Area Attacco (Rosso trasparente)
        if (attackArea != null) {
            g.setColor(new Color(255, 0, 0, 80)); 
            for (Coordinates coord : attackArea) {
                drawCellRect(g, coord.getX(), coord.getY(), startX, startY, true);
                
                // Bordo rosso opzionale
                g.setColor(Color.RED);
                drawCellRect(g, coord.getX(), coord.getY(), startX, startY, false);
                g.setColor(new Color(255, 0, 0, 80)); // Reset
            }
        }
    }

    /**
     * Helper per disegnare rettangoli sulle celle calcolando l'offset corretto
     */
    private void drawCellRect(Graphics g, int gridX, int gridY, int startX, int startY, boolean fill) {
        int px = startX + (gridX * CELL_SIZE);
        int py = startY + (gridY * CELL_SIZE);
        
        // Aggiustamenti fini per centrare il rettangolo attorno al testo
        // Il testo drawString è a (px, py + CELL_SIZE).
        // Il rettangolo deve partire da (px, py) con larghezza CELL_SIZE.
        
        // Nota: Nel tuo codice originale usavi offset specifici (-2, -10). 
        // Qui normalizziamo: il rettangolo copre l'intera cella logica.
        if (fill) {
            g.fillRect(px, py, CELL_SIZE, CELL_SIZE);
        } else {
            g.drawRect(px, py, CELL_SIZE, CELL_SIZE);
        }
    }
    
    public void updateMovementCursor(Coordinates cursor) {
        this.movementCursor = cursor;
        repaint();
    }
    
    public void clearMovementCursor() {
        this.movementCursor = null;
        repaint();
    }
    
    public void showMoveArea(List<Coordinates> moveArea, Coordinates playerPos) {
        this.moveArea = Coordinates.sum(moveArea, playerPos);
        repaint();
    }
    
    // --- IMPLEMENTAZIONE LOGICA AREA ATTACCO ---
    public void updateAttackArea(CombatStrategy strategy) {
        if (strategy == null || strategy.getPlayer() == null) {
            this.attackArea = null;
            repaint();
            return;
        }

        Player player = strategy.getPlayer();
        
        // 1. Recupera gli indici correnti dalla strategia
        int weaponIdx = strategy.getWeaponIndex();
        int moveIdx = strategy.getMoveIndex();

        // 2. Controlli di sicurezza per evitare IndexOutOfBounds
        List<Weapon> weapons = player.getWeapons();
        if (weaponIdx >= 0 && weaponIdx < weapons.size()) {
            Weapon weapon = weapons.get(weaponIdx);
            List<Move> moves = weapon.getMoves();

            if (moveIdx >= 0 && moveIdx < moves.size()) {
                Move move = moves.get(moveIdx);

                // 3. Recupera l'area relativa dalla mossa
                List<Coordinates> relativeArea = move.getAttackArea();

                if (relativeArea != null && !relativeArea.isEmpty()) {
                    // 4. Trasforma l'area relativa in assoluta (sommando la posizione del player)
                    // Questo usa il metodo statico sum() che hai usato anche in MenuPanel
                    this.attackArea = Coordinates.sum(relativeArea, player.getCoordinates());
                } else {
                    this.attackArea = null;
                }
            } else {
                this.attackArea = null;
            }
        } else {
            this.attackArea = null;
        }

        // 5. Ridisegna la mappa
        repaint();
    }
    
    public void clearAttackArea() {
        this.attackArea = null;
        repaint();
    }
    
    public void refreshAttackArea(CombatStrategy strategy) {
        updateAttackArea(strategy);
    }
    
    public void cleanup() {
        if (blinkTimer != null) {
            blinkTimer.stop();
        }
    }

    public void setMap(Maps map){
        this.map = map;
        repaint();
    }
}