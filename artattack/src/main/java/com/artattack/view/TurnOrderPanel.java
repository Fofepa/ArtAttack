package com.artattack.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

import com.artattack.mapelements.ActiveElement;
import com.artattack.mapelements.Enemy;
import com.artattack.mapelements.Player;
import com.artattack.turns.ConcreteTurnHandler;

/**
 * TurnOrderPanel - Displays turn order queue
 */
public class TurnOrderPanel extends JPanel {
    private ConcreteTurnHandler turnHandler;
    
    public TurnOrderPanel(ConcreteTurnHandler turnHandler) {
        this.turnHandler = turnHandler;
        setBackground(Color.BLACK);
    }

    public void setTurnHandler(ConcreteTurnHandler turnHandler){
        this.turnHandler = turnHandler;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (turnHandler == null) {
            g.setColor(Color.GRAY);
            g.setFont(new Font("Monospaced", Font.PLAIN, 12));
            g.drawString("No turn system", 10, 20);
            return;
        }
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.BOLD, 11));
        g.drawString("Turn Order:", 10, 20);
        
        // Get the turn queue from the ConcreteTurnQueue inside the handler
        try {
            List<ActiveElement> turnQueue = turnHandler.getConcreteTurnQueue().getTurnQueue();
            
            if (turnQueue == null || turnQueue.isEmpty()) {
                g.setColor(Color.GRAY);
                g.setFont(new Font("Monospaced", Font.PLAIN, 11));
                g.drawString("No turns in queue", 10, 40);
                return;
            }
            
            // Display turn order
            g.setFont(new Font("Monospaced", Font.PLAIN, 11));
            int y = 40;
            int position = 1;
            
            // Get current turn element and index
            ActiveElement currentTurn = turnHandler.current();
            int currentIndex = turnHandler.getIndex();
            
            for (int i = 0; i < turnQueue.size(); i++) {
                ActiveElement element = turnQueue.get(i);
                
                if (element != null) {
                    // Highlight current turn
                    if (i == currentIndex) {
                        g.setColor(Color.CYAN);
                        g.drawString("> ", 5, y);
                        g.setFont(new Font("Monospaced", Font.BOLD, 11));
                    } else {
                        g.setColor(Color.WHITE);
                        g.setFont(new Font("Monospaced", Font.PLAIN, 11));
                    }
                    
                    // Display: position. name (HP: curr/max, AP: curr)
                    String turnInfo = position + ". " + element.getName();
                    
                    // Add HP and AP info
                    if (element instanceof Player) {
                        Player player = (Player) element;
                        turnInfo += " (HP:" + player.getCurrHP() + "/" + player.getMaxHP() + 
                                   " AP:" + player.getActionPoints() + ")";
                    } else if (element instanceof Enemy) {
                        Enemy enemy = (Enemy) element;
                        turnInfo += " (HP:" + enemy.getCurrHP() + "/" + enemy.getMaxHP() + 
                                   " AP:" + enemy.getActionPoints() + ")";
                    }
                    
                    g.drawString(turnInfo, 20, y);
                    y += 15;
                    position++;
                    
                    // Don't overflow the panel
                    if (y > getHeight() - 10) {
                        g.setColor(Color.GRAY);
                        g.setFont(new Font("Monospaced", Font.ITALIC, 10));
                        g.drawString("...", 20, y);
                        break;
                    }
                }
            }
            
        } catch (Exception e) {
            g.setColor(Color.RED);
            g.setFont(new Font("Monospaced", Font.PLAIN, 10));
            g.drawString("Error: " + e.getMessage(), 10, 40);
            System.err.println("TurnOrderPanel error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}