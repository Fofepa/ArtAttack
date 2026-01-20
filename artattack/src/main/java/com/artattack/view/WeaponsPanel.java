package com.artattack.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

import com.artattack.mapelements.Player;
import com.artattack.moves.Weapon;

/**
 * WeaponsPanel - Displays available weapons with selection
 */
public class WeaponsPanel extends JPanel {
    private Player player;
    private int selectedWeaponIndex = 0;
    
    public WeaponsPanel(Player player) {
        this.player = player;
        setBackground(Color.BLACK);
        setFocusable(true);
    }


    public void setPlayer(Player player) {
        this.player = player;
        this.selectedWeaponIndex = 0;
        repaint();
    }
    
    public int getSelectedWeaponIndex() {
        return selectedWeaponIndex;
    }
    
    public void setSelectedWeaponIndex(int index) {
        if (player != null && !player.getWeapons().isEmpty()) {
            this.selectedWeaponIndex = Math.max(0, Math.min(index, player.getWeapons().size() - 1));
        }
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
        
        g.setFont(new Font("Monospaced", Font.PLAIN, 11));
        int y = 20;
        
        for (int i = 0; i < weapons.size(); i++) {
            Weapon weapon = weapons.get(i);
            
            if (i == selectedWeaponIndex) {
                g.setColor(Color.CYAN);
                g.drawString("> ", 5, y);
            } else {
                g.setColor(Color.WHITE);
            }
            
            g.drawString(weapon.getName(), 20, y);
            y += 15;
        }
        
        // Instruction
        g.setColor(Color.GRAY);
        g.setFont(new Font("Monospaced", Font.PLAIN, 10));
        g.drawString("Press » to view moves", 10, getHeight() - 10);
    }
}