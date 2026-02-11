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
    
    private static final int BAR_WIDTH = 150;
    private static final int BAR_HEIGHT = 10;
    private static final int START_X = 10;
    private static final int START_Y = 20;
    private static final int GAP_Y = 35; 

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
        
        if (player == null) {
            g.setColor(Color.GRAY);
            g.setFont(new Font("Monospaced", Font.PLAIN, 12));
            g.drawString("No player selected", 10, 20);
            return;
        }
        
        g.setFont(new Font("Monospaced", Font.BOLD, 12));
        
        int currentY = START_Y;

        g.setColor(Color.WHITE);
        String nameText = player.getName() + " (Lvl: " + player.getLevel() + ")";
        g.drawString(nameText, START_X, currentY);
        
        currentY += 20; 

        drawBar(g, "HP", player.getCurrHP(), player.getMaxHP(), Color.RED, START_X, currentY);
        
        currentY += GAP_Y; 

        
        int maxAP = player.getMaxActionPoints(); 
        drawBar(g, "AP", player.getActionPoints(), maxAP, Color.CYAN, START_X, currentY);

        currentY += GAP_Y;

        int currentExp = player.getCurrXP();
        int maxExp = player.getMaxXP();

        drawBar(g, "XP", currentExp, maxExp, Color.YELLOW, START_X, currentY);
    }

    
    private void drawBar(Graphics g, String label, int current, int max, Color barColor, int x, int y) {
        if (max <= 0) max = 1; 
        
        int fillWidth = (int) (((double) current / max) * BAR_WIDTH);
        
        if (fillWidth > BAR_WIDTH) fillWidth = BAR_WIDTH;
        if (fillWidth < 0) fillWidth = 0;

        g.setColor(Color.WHITE);
        g.drawString(label + ": " + current + "/" + max, x, y - 5);

        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, BAR_WIDTH, BAR_HEIGHT);

        g.setColor(barColor);
        g.fillRect(x, y, fillWidth, BAR_HEIGHT);

        g.setColor(Color.LIGHT_GRAY);
        g.drawRect(x, y, BAR_WIDTH, BAR_HEIGHT);
    }
}