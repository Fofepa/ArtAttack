package com.artattack.view;

import java.awt.*;
import java.util.List;

import javax.swing.*;

import com.artattack.mapelements.Player;
import com.artattack.moves.*;

/**
 * WeaponsPanel - Displays available weapons
 */
public class WeaponsPanel extends JPanel {
    private Player player;
    
    public WeaponsPanel(Player player) {
        this.player = player;
        setBackground(Color.BLACK);
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
        
        List<Weapon> weapons = player.getWeapons();
        
        if (weapons.isEmpty()) {
            g.setColor(Color.GRAY);
            g.setFont(new Font("Monospaced", Font.PLAIN, 12));
            g.drawString("No weapons", 10, 20);
            return;
        }
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.PLAIN, 11));
        
        int y = 20;
        for (Weapon weapon : weapons) {
            g.drawString("• " + weapon.getName(), 10, y);
            y += 15;
        }
    }
}
