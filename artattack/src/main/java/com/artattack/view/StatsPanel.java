package com.artattack.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

import com.artattack.mapelements.Player;

/**
 * StatsPanel - Displays player statistics with visual bars
 */
public class StatsPanel extends JPanel {
    private Player player;
    
    // Layout constants
    private static final int BAR_WIDTH = 150;
    private static final int BAR_HEIGHT = 10;
    private static final int START_X = 10;
    private static final int START_Y = 20;
    private static final int GAP_Y = 35; // Vertical space between bars
    
    public StatsPanel(Player player) {
        this.player = player;
        setBackground(Color.BLACK);
    }

    public void setPlayer(Player player){
        this.player = player;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // 1. Handle null player case
        if (player == null) {
            g.setColor(Color.GRAY);
            g.setFont(new Font("Monospaced", Font.PLAIN, 12));
            g.drawString("No player selected", 10, 20);
            return;
        }
        
        g.setFont(new Font("Monospaced", Font.BOLD, 12));
        
        int currentY = START_Y;

        // 2. Draw Player Name
        g.setColor(Color.WHITE);
        String nameText = player.getName() + " (Lvl: " + player.getLevel() + ")";
        g.drawString(nameText, START_X, currentY);
        
        currentY += 20; // Move down for the first bar

        // 3. Draw HP Bar (Red)
        drawBar(g, "HP", player.getCurrHP(), player.getMaxHP(), Color.RED, START_X, currentY);
        
        currentY += GAP_Y; // Move down for the second bar

        // 4. Draw AP Bar (Cyan)
        
        int maxAP = player.getMaxActionPoints(); 
        drawBar(g, "AP", player.getActionPoints(), maxAP, Color.CYAN, START_X, currentY);

        currentY += GAP_Y;

        // 5. Draw XP Bar (Yellow)
        int currentExp = player.getCurrXP();
        int maxExp = player.getMaxHP();

        drawBar(g, "XP", currentExp, maxExp, Color.YELLOW, START_X, currentY);
    }

    /**
     * Helper method to draw a generic statistic bar.
     * * @param g The graphics context
     * @param label The text label (e.g., "HP")
     * @param current The current value
     * @param max The maximum value
     * @param barColor The color of the filled portion
     * @param x X position
     * @param y Y position
     */
    private void drawBar(Graphics g, String label, int current, int max, Color barColor, int x, int y) {
        // Avoid division by zero
        if (max <= 0) max = 1; 
        
        // Calculate the width of the filled portion based on percentage
        // Cast to double for decimal division, then back to int
        int fillWidth = (int) (((double) current / max) * BAR_WIDTH);
        
        // Clamp the width: ensure it doesn't exceed the max width or go below 0
        if (fillWidth > BAR_WIDTH) fillWidth = BAR_WIDTH;
        if (fillWidth < 0) fillWidth = 0;

        // 1. Draw Label above the bar (e.g., "HP: 5/10")
        g.setColor(Color.WHITE);
        g.drawString(label + ": " + current + "/" + max, x, y - 5);

        // 2. Draw Bar Background (Dark Gray - empty portion)
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, BAR_WIDTH, BAR_HEIGHT);

        // 3. Draw Bar Fill (Color param - filled portion)
        g.setColor(barColor);
        g.fillRect(x, y, fillWidth, BAR_HEIGHT);

        // 4. Draw Bar Border (Light Gray)
        g.setColor(Color.LIGHT_GRAY);
        g.drawRect(x, y, BAR_WIDTH, BAR_HEIGHT);
    }
}