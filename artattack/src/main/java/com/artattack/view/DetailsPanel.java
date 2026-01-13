package com.artattack.view;

import java.awt.*;
import java.util.List;

import javax.swing.*;

import com.artattack.mapelements.Player;
import com.artattack.moves.*;

/**
 * DetailsPanel - Displays details about selected items/moves
 */
public class DetailsPanel extends JPanel {
    private String detailText = "No item selected";
    
    public DetailsPanel() {
        setBackground(Color.BLACK);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.setColor(Color.GRAY);
        g.setFont(new Font("Monospaced", Font.PLAIN, 12));
        g.drawString(detailText, 10, 20);
    }
    
    public void setDetailText(String text) {
        this.detailText = text;
        repaint();
    }
}