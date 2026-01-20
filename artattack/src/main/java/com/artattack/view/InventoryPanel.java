package com.artattack.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

import com.artattack.items.Item;
import com.artattack.mapelements.Player;
/**
 * InventoryPanel - Displays player inventory
 */
public class InventoryPanel extends JPanel {
    private Player player;
    private int selectedIndex = 0;
    
    public InventoryPanel(Player player) {
        this.player = player;
        setBackground(Color.BLACK);
        setFocusable(true);
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
            g.drawString("No player", 10, 20);
            return;
        }
        
        List<Item> inventory = player.getInventory();
        
        if (inventory.isEmpty()) {
            g.setColor(Color.GRAY);
            g.setFont(new Font("Monospaced", Font.PLAIN, 12));
            g.drawString("Empty", 10, 20);
            return;
        }
        
        g.setFont(new Font("Monospaced", Font.PLAIN, 11));
        int y = 20;
        
        for (int i = 0; i < inventory.size(); i++) {
            Item item = inventory.get(i);
            
            if (i == selectedIndex) {
                g.setColor(Color.CYAN);
                g.drawString("> ", 5, y);
            } else {
                g.setColor(Color.WHITE);
            }
            
            g.drawString(item.getName(), 20, y);
            y += 15;
        }
    }

    public void setSelectedIndex(int index) {
        this.selectedIndex = index;
        this.repaint(); 
    }
}