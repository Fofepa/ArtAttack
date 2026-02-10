package com.artattack.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.artattack.items.Item;

/**
 * DetailsPanel - Displays details about selected items/moves
 */
public class DetailsPanel extends JPanel {
    private String detailText = "No item selected";
    private String itemName = "";
    private String itemDesc = "";
    private String itemQty = "";
    
    public DetailsPanel() {
        setBackground(Color.BLACK);
    }
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Simple Border
        g.setColor(Color.DARK_GRAY);
        g.drawRect(0, 0, getWidth()-1, getHeight()-1);

        if (itemName.isEmpty()) {
            g.setColor(Color.GRAY);
            g.setFont(new Font("Monospaced", Font.PLAIN, 12));
            g.drawString("No item selected", 10, 20);
            return;
        }

        // 1. Obj name
        g.setFont(new Font("Monospaced", Font.BOLD, 12));
        g.setColor(Color.WHITE);
        g.drawString(itemName, 10, 20);

        

        // 2. Description
        g.setColor(Color.LIGHT_GRAY);
        g.setFont(new Font("Monospaced", Font.ITALIC, 12));
        
        int y = 45;
        int lineHeight = 15;
        List<String> wrappedDesc = wrapText(itemDesc, 30);
        
        for (String line : wrappedDesc) {
            g.drawString(line, 10, y);
            y += lineHeight;
        }
    }

    private List<String> wrapText(String text, int charLimit) {
        List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            if (currentLine.length() + word.length() + 1 > charLimit) {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder();
            }
            if (currentLine.length() > 0) {
                currentLine.append(" ");
            }
            currentLine.append(word);
        }
        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }
        return lines;
    }
    
    public void showItemDetails(Item item) {
        if (item == null) {
            this.itemName = "";
            this.itemDesc = "";
            this.itemQty = "";
        } else {
            this.itemName = item.getName();
            
            this.itemDesc = (item.getDescription() != null) ? item.getDescription() : "No description available.";
            
        }
        repaint();
    }


}