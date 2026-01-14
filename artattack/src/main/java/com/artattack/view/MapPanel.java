package com.artattack.view;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import com.artattack.level.Maps;
import com.artattack.level.Coordinates;
import com.artattack.mapelements.Player;
import com.artattack.items.Item;
import com.artattack.moves.Weapon;
import com.artattack.moves.Move;
import com.artattack.turns.ConcreteTurnHandler;
import com.artattack.inputcontroller.CombatStrategy;

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
        
        // Simple character-based rendering
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        char[][] matrix = map.getMapMatrix();
        int cellSize = 12;
        
        for (int y = 0; y < map.getRows(); y++) {
            for (int x = 0; x < map.getColumns(); x++) {
                char c = matrix[y][x];

                // Check if cursor should be drawn at this position
                boolean isCursorPosition = movementCursor != null && 
                                          movementCursor.getX() == x && 
                                          movementCursor.getY() == y;
                
                // If cursor is at this position and visible, draw cursor instead
                if (isCursorPosition && cursorVisible) {
                    g.setColor(Color.GREEN);
                    g.drawString("*", x * cellSize + 10, y * cellSize + 20);
                }
                else{
                    // Set colors based on character
                    switch (c) {
                        case '#' -> g.setColor(Color.GRAY);
                        case '@' -> g.setColor(Color.CYAN);
                        case '.' -> g.setColor(new Color(50, 50, 50));
                        case 'E' -> g.setColor(Color.RED);
                        case 'I' -> g.setColor(Color.YELLOW);
                        default -> g.setColor(Color.WHITE);
                    } 
                }
                
                g.drawString(String.valueOf(c), x * cellSize + 10, y * cellSize + 20);
            }
        }
        
        // Draw movement cursor if active
        if (movementCursor != null) {
            g.setColor(Color.GREEN);
            int x = movementCursor.getX() * cellSize + 10;
            int y = movementCursor.getY() * cellSize + 20;
            g.drawRect(x - 2, y - 10, cellSize, cellSize);
        }
        
        // Draw move area
        if (moveArea != null) {
            g.setColor(new Color(0, 255, 0, 50));
            for (Coordinates coord : moveArea) {
                int x = coord.getX() * cellSize + 10;
                int y = coord.getY() * cellSize + 20;
                g.fillRect(x - 2, y - 10, cellSize, cellSize);
            }
        }
        
        // Draw attack area
        if (attackArea != null) {
            g.setColor(new Color(255, 0, 0, 50));
            for (Coordinates coord : attackArea) {
                int x = coord.getX() * cellSize + 10;
                int y = coord.getY() * cellSize + 20;
                g.fillRect(x - 2, y - 10, cellSize, cellSize);
            }
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
    
    public void updateAttackArea(CombatStrategy strategy) {
        // Get attack area from selected move
        // This would need to be implemented based on your Move class
        repaint();
    }
    
    public void clearAttackArea() {
        this.attackArea = null;
        repaint();
    }
    
    public void refreshAttackArea(CombatStrategy strategy) {
        updateAttackArea(strategy);
    }
    
    /**
     * Stops the blink timer when the panel is no longer needed
     */
    public void cleanup() {
        if (blinkTimer != null) {
            blinkTimer.stop();
        }
    }
}
