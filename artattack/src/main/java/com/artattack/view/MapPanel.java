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

        int cellSize = GameSettings.getInstance().getZoomLevel();
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.PLAIN, cellSize));
        
        char[][] matrix = map.getMapMatrix();

        char p1Symbol = (map.getPlayerOne() != null) ? map.getPlayerOne().getMapSymbol() : '\0';
        char p2Symbol = (map.getPlayerTwo() != null) ? map.getPlayerTwo().getMapSymbol() : '\0';

        int mapPixelWidth = map.getWidth() * cellSize;
        int mapPixelHeight = map.getHeight() * cellSize;
        int startX = (getWidth() - mapPixelWidth) / 2;
        int startY = (getHeight() - mapPixelHeight) / 2;

        // --- 1. SFONDO (Caratteri) ---
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                char c = matrix[x][y]; 
                int px = startX + (x * cellSize);
                int py = startY + (y * cellSize);

               if (c == '@' || c == p1Symbol || c == p2Symbol) {
                    continue; 
                }

                switch (c) {
                    case '#' -> g.setColor(Color.GRAY);
                    case '.' -> /*{continue;}*/g.setColor(new Color(50, 50, 50));
                    case 'E' -> g.setColor(Color.RED);
                    case 'I' -> g.setColor(Color.YELLOW);
                    default -> g.setColor(Color.WHITE);
                } 
                g.drawString(String.valueOf(c), px, py + (int)(cellSize * 0.85));
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
                            drawCellRect(g, coord.getX(), coord.getY(), startX, startY, true, cellSize);
                        }
                    }
                }
            }
        }

        // --- 3. AREA MOVIMENTO GIOCATORE ---
        if (moveArea != null && !moveArea.isEmpty()) {
            // System.out.println("DEBUG PAINT: Drawing " + moveArea.size() + " move tiles.");
            
            for (Coordinates coord : moveArea) {
                // Rimuovi il check del muro per debug se necessario, ma di solito è meglio averlo
                if(isValidAndNotWall(coord, map, matrix)) {
                    // Riempimento
                    g.setColor(new Color(0, 255, 0, 20));
                    drawCellRect(g, coord.getX(), coord.getY(), startX, startY, true, cellSize); 
                    
                    // Bordo evidenziato
                    g.setColor(new Color(0, 255, 0, 40));
                    drawCellRect(g, coord.getX(), coord.getY(), startX, startY, false, cellSize); 
                }
            }
        }
        
        // --- 4. AREA ATTACCO ---
        if (attackArea != null) {
            g.setColor(new Color(255, 0, 0, 80)); 
            for (Coordinates coord : attackArea) {
                if(isValidAndNotWall(coord, map, matrix)) {
                    drawCellRect(g, coord.getX(), coord.getY(), startX, startY, true, cellSize);
                    g.setColor(Color.RED);
                    drawCellRect(g, coord.getX(), coord.getY(), startX, startY, false, cellSize);
                    g.setColor(new Color(255, 0, 0, 80)); 
                }
            }
        }

        // --- 5. DISEGNO GIOCATORI CON SIMBOLI PERSONALIZZATI ---
        drawPlayerSymbol(g, map.getPlayerOne(), startX, startY, cellSize);
        drawPlayerSymbol(g, map.getPlayerTwo(), startX, startY, cellSize);

        // --- 6. CURSORE ---
        if (movementCursor != null && cursorVisible) {
            g.setColor(Color.GREEN);
            drawCellRect(g, movementCursor.getX(), movementCursor.getY(), startX, startY, false, cellSize);
            
            int px = startX + (movementCursor.getX() * cellSize);
            int py = startY + (movementCursor.getY() * cellSize);
            g.drawString("*", px, py + cellSize);
        }
    }

    private void drawPlayerSymbol(Graphics g, Player p, int startX, int startY, int cellSize) {
        if (p != null) {
            int px = startX + (p.getCoordinates().getX() * cellSize);
            int py = startY + (p.getCoordinates().getY() * cellSize);
            
            g.setColor(Color.CYAN);
            // Qui prendiamo il simbolo (es. '♫' o '◉') invece di '@'
            g.drawString(String.valueOf(p.getMapSymbol()), px, py + cellSize);
        }
    }

    private boolean isValidAndNotWall(Coordinates c, Maps map, char[][] matrix) {
        if (c.getX() < 0 || c.getX() >= map.getWidth() || 
            c.getY() < 0 || c.getY() >= map.getHeight()) {
            return false;
        }
        return matrix[c.getX()][c.getY()] != '#';
    }

    private void drawCellRect(Graphics g, int gridX, int gridY, int startX, int startY, boolean fill, int cellSize) {
        int px = startX + (gridX * cellSize);
        int py = startY + (gridY * cellSize);
        
        if (fill) {
            g.fillRect(px, py, cellSize, cellSize);
        } else {
            g.drawRect(px, py, cellSize, cellSize);
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