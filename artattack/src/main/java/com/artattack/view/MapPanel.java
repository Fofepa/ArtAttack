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
import com.artattack.mapelements.ActiveElement;
import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.Player;
import com.artattack.moves.Move;
import com.artattack.moves.Weapon;

public class MapPanel extends JPanel {
    private Maps map;
    private Coordinates movementCursor;
    private List<Coordinates> moveArea;
    private List<Coordinates> attackArea;
    boolean isHeal = false;

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

        int mapPixelWidth = map.getWidth() * cellSize;
        int mapPixelHeight = map.getHeight() * cellSize;
        int startX = (getWidth() - mapPixelWidth) / 2;
        int startY = (getHeight() - mapPixelHeight) / 2;

        // BackGround
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                char c = matrix[x][y]; 
                int px = startX + (x * cellSize);
                int py = startY + (y * cellSize);


                boolean isP1Here = (map.getPlayerOne() != null && 
                            map.getPlayerOne().getCoordinates().getX() == x && 
                            map.getPlayerOne().getCoordinates().getY() == y);
                            
                boolean isP2Here = (map.getPlayerTwo() != null && 
                            map.getPlayerTwo().getCoordinates().getX() == x && 
                            map.getPlayerTwo().getCoordinates().getY() == y);

                if (isP1Here || isP2Here) {
                    continue; 
                }



                switch (c) {
                    case '\u2588' -> g.setColor(Color.GRAY);
                    case '\u25EA' -> g.setColor(new Color(50, 50, 50));
                    // enemy cases
                    case 'E' -> g.setColor(Color.RED);
                    case 'G' -> g.setColor(Color.RED);
                    case 'M' -> g.setColor(Color.RED);
                    case 'B' -> g.setColor(Color.RED);
                    case 'R' -> g.setColor(Color.RED);
                    case 'T' -> g.setColor(Color.RED);
                    case 'S' -> g.setColor(Color.RED);
                    case 'Y' -> g.setColor(Color.PINK);
                    // NPCs and mapElements Cases
                    case 'i' -> g.setColor(Color.GREEN);    // Melies
                    case 'A' -> g.setColor(new Color(119, 3, 252)); // Aretha
                    case '\u3020' -> g.setColor(Color.YELLOW);  // RENE
                    case 'I' -> g.setColor(Color.YELLOW);   
                    case '\u2583' -> g.setColor(new Color(150, 100, 60));
                    case '\u2318' -> g.setColor(new Color(131, 244, 74)); 
                    case '$' -> g.setColor(new Color(180,180,0));
                    case '\u2339' -> g.setColor(new Color(196, 136, 62));
                    case '\u20AC' -> g.setColor(new Color(133, 187, 101));
                    case '\u271A' -> g.setColor(new Color(255, 0, 0));
                    case '\u25A3' -> g.setColor(Color.GRAY);
                    case '\u2380' -> g.setColor(new Color(255, 105, 180));
                    default -> g.setColor(Color.WHITE);
                } 
                g.drawString(String.valueOf(c), px, py + (int)(cellSize * 0.85));
            }
        }
        
        if (map.getEnemies() != null) {
            g.setColor(new Color(255, 165, 0, 40)); 

            
            List<ActiveElement> activeQueue = map.getConcreteTurnHandler().getConcreteTurnQueue().getTurnQueue();
        
            for (Enemy enemy : map.getEnemies()) {
                
                boolean isAlreadyInQueue = activeQueue.contains(enemy);
            
                if ((enemy.getIsActive() || enemy.getCurrHP() > 0) && 
                    !isAlreadyInQueue && 
                    enemy.getVisionArea() != null) {
                    
                    List<Coordinates> absVision = Coordinates.sum(enemy.getVisionArea(), enemy.getCoordinates());
                    for (Coordinates coord : absVision) {
                        if (isValidAndNotWall(coord, map, matrix)) {
                            drawCellRect(g, coord.getX(), coord.getY(), startX, startY, true, cellSize);
                        }
                    }
                }
            }
        }

        if (moveArea != null && !moveArea.isEmpty()) {
            // System.out.println("DEBUG PAINT: Drawing " + moveArea.size() + " move tiles.");
            
            for (Coordinates coord : moveArea) {
                if(isValidAndNotWall(coord, map, matrix)) {
                    g.setColor(new Color(0, 255, 0, 20));
                    drawCellRect(g, coord.getX(), coord.getY(), startX, startY, true, cellSize); 
                    
                    g.setColor(new Color(0, 255, 0, 40));
                    drawCellRect(g, coord.getX(), coord.getY(), startX, startY, false, cellSize); 
                }
            }
        }
        
        if (attackArea != null) {

            if (isHeal) {
                g.setColor(new Color(255, 255, 0, 80));
                for (Coordinates coord : attackArea) {
                    if(isValidAndNotWall(coord, map, matrix)) {
                        drawCellRect(g, coord.getX(), coord.getY(), startX, startY, true, cellSize);
                        g.setColor(Color.YELLOW);
                        drawCellRect(g, coord.getX(), coord.getY(), startX, startY, false, cellSize);
                        g.setColor(new Color(255, 255, 0, 80)); 
                    }
                }
            }else{
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
        }

        
        drawPlayerSymbol(g, map.getPlayerOne(), startX, startY, cellSize);
        drawPlayerSymbol(g, map.getPlayerTwo(), startX, startY, cellSize);

        
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
            
            if (p.isAlive()) {
                g.setColor(Color.CYAN);
            }else{
                g.setColor(Color.GRAY);
            }

            g.drawString(String.valueOf(p.getMapSymbol()), px, py + cellSize);
        }
    }


    private boolean isValidAndNotWall(Coordinates c, Maps map, char[][] matrix) {
        if (c.getX() < 0 || c.getX() >= map.getWidth() || 
            c.getY() < 0 || c.getY() >= map.getHeight()) {
            return false;
        }
        return matrix[c.getX()][c.getY()] != '\u2588';
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
        this.isHeal = false;
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

                if(relativeArea== null || relativeArea.isEmpty()){
                    relativeArea = move.getHealArea();
                    isHeal= true;

                    if (relativeArea != null && !relativeArea.isEmpty()) {
                        this.isHeal = true;
                    }
                }

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