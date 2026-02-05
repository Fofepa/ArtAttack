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
import com.artattack.mapelements.Enemy;
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

        initializeBlinkTimer();
    }

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

        int mapPixelWidth = map.getWidth() * CELL_SIZE;
        int mapPixelHeight = map.getHeight() * CELL_SIZE;
        int startX = (getWidth() - mapPixelWidth) / 2;
        int startY = (getHeight() - mapPixelHeight) / 2;

        // --- 1. SFONDO (Caratteri) ---
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                char c = matrix[x][y]; 
                int px = startX + (x * CELL_SIZE);
                int py = startY + (y * CELL_SIZE);

                switch (c) {
                    case '#' -> g.setColor(Color.GRAY);
                    case '@' -> g.setColor(Color.CYAN);
                    case '.' -> g.setColor(new Color(50, 50, 50));
                    case 'E' -> g.setColor(Color.RED);
                    case 'I' -> g.setColor(Color.YELLOW);
                    default -> g.setColor(Color.WHITE);
                } 
                g.drawString(String.valueOf(c), px, py + CELL_SIZE);
            }
        }
        
        // --- 2. VISIONE NEMICI ---
        if (map.getEnemies() != null) {
            g.setColor(new Color(255, 165, 0, 40)); 
            for (Enemy enemy : map.getEnemies()) {
                if ((enemy.getIsActive() || enemy.getCurrHP() > 0) && enemy.getVisionArea() != null) {
                    // VisionArea è solitamente relativa, quindi qui USIAMO la somma
                    List<Coordinates> absVision = Coordinates.sum(enemy.getVisionArea(), enemy.getCoordinates());
                    for (Coordinates coord : absVision) {
                        if (isValidAndNotWall(coord, map, matrix)) {
                            drawCellRect(g, coord.getX(), coord.getY(), startX, startY, true);
                        }
                    }
                }
            }
        }

        // --- 3. AREA MOVIMENTO GIOCATORE ---
        if (moveArea != null && !moveArea.isEmpty()) {
            // System.out.println("DEBUG PAINT: Drawing " + moveArea.size() + " move tiles.");
            
            g.setColor(new Color(0, 70, 0, 10)); // Verde semi-trasparente
            
            for (Coordinates coord : moveArea) {
                // Rimuovi il check del muro per debug se necessario, ma di solito è meglio averlo
                if(isValidAndNotWall(coord, map, matrix)) {
                    // Riempimento
                    g.setColor(new Color(0, 255, 0, 80));
                    drawCellRect(g, coord.getX(), coord.getY(), startX, startY, true); 
                    
                    // Bordo evidenziato
                    g.setColor(Color.GREEN);
                    drawCellRect(g, coord.getX(), coord.getY(), startX, startY, false); 
                }
            }
        }
        
        // --- 4. AREA ATTACCO ---
        if (attackArea != null) {
            g.setColor(new Color(255, 0, 0, 80)); 
            for (Coordinates coord : attackArea) {
                if(isValidAndNotWall(coord, map, matrix)) {
                    drawCellRect(g, coord.getX(), coord.getY(), startX, startY, true);
                    g.setColor(Color.RED);
                    drawCellRect(g, coord.getX(), coord.getY(), startX, startY, false);
                    g.setColor(new Color(255, 0, 0, 80)); 
                }
            }
        }

        // --- 5. CURSORE ---
        if (movementCursor != null && cursorVisible) {
            g.setColor(Color.GREEN);
            drawCellRect(g, movementCursor.getX(), movementCursor.getY(), startX, startY, false);
            
            int px = startX + (movementCursor.getX() * CELL_SIZE);
            int py = startY + (movementCursor.getY() * CELL_SIZE);
            g.drawString("*", px, py + CELL_SIZE);
        }
    }

    private boolean isValidAndNotWall(Coordinates c, Maps map, char[][] matrix) {
        if (c.getX() < 0 || c.getX() >= map.getWidth() || 
            c.getY() < 0 || c.getY() >= map.getHeight()) {
            return false;
        }
        return matrix[c.getX()][c.getY()] != '#';
    }

    private void drawCellRect(Graphics g, int gridX, int gridY, int startX, int startY, boolean fill) {
        int px = startX + (gridX * CELL_SIZE);
        int py = startY + (gridY * CELL_SIZE);
        
        if (fill) {
            g.fillRect(px, py, CELL_SIZE, CELL_SIZE);
        } else {
            g.drawRect(px, py, CELL_SIZE, CELL_SIZE);
        }
    }
    
    public void showMoveArea(List<Coordinates> moveArea, Coordinates playerPos) {
        // System.out.println("DEBUG: showMoveArea called with " + (moveArea != null ? moveArea.size() : "null") + " tiles.");
        
        if (moveArea == null || moveArea.isEmpty()) {
            this.moveArea = null;
        } else {
            
            this.moveArea = moveArea;
            
            
        }
        repaint();
    }
    
    public void updateMovementCursor(Coordinates cursor) {
        this.movementCursor = cursor;
        repaint();
    }
    
    public void clearMovementCursor() {
        this.movementCursor = null;
        repaint();
    }

    public void updateAttackArea(CombatStrategy strategy) {
        if (strategy == null || strategy.getPlayer() == null) {
            this.attackArea = null;
            repaint();
            return;
        }

        Player player = strategy.getPlayer();
        int weaponIdx = strategy.getWeaponIndex();
        int moveIdx = strategy.getMoveIndex();

        List<Weapon> weapons = player.getWeapons();
        if (weaponIdx >= 0 && weaponIdx < weapons.size()) {
            Weapon weapon = weapons.get(weaponIdx);
            List<Move> moves = weapon.getMoves();

            if (moveIdx >= 0 && moveIdx < moves.size()) {
                Move move = moves.get(moveIdx);
                List<Coordinates> relativeArea = move.getAttackArea();

                if (relativeArea != null && !relativeArea.isEmpty()) {
                    
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
        if (blinkTimer != null) blinkTimer.stop();
    }

    public void setMap(Maps map){
        this.map = map;
        repaint();
    }
}