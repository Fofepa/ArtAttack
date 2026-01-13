package com.artattack.view;

import java.awt.*;
import java.util.List;

import javax.swing.*;

import com.artattack.mapelements.Player;
import com.artattack.moves.*;

/**
 * MovesPanel - Displays weapons and their moves
 */
public class MovesPanel extends JPanel {
    private Player player;
    private int selectedWeaponIndex = 0;
    private int selectedMoveIndex = 0;
    private boolean isInMoveSelection = false;
    
    public MovesPanel(Player player) {
        this.player = player;
        setBackground(Color.BLACK);
        setFocusable(true);
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
        
        g.setFont(new Font("Monospaced", Font.PLAIN, 11));
        int y = 20;
        
        List<Weapon> weapons = player.getWeapons();
        
        if (weapons.isEmpty()) {
            g.setColor(Color.GRAY);
            g.drawString("Press » to select", 10, y);
            return;
        }
        
        // Display weapons
        for (int i = 0; i < weapons.size(); i++) {
            Weapon weapon = weapons.get(i);
            
            if (i == selectedWeaponIndex && !isInMoveSelection) {
                g.setColor(Color.CYAN);
                g.drawString("> ", 5, y);
            } else {
                g.setColor(Color.WHITE);
            }
            
            g.drawString(weapon.getName(), 20, y);
            y += 15;
            
            // Display moves if this weapon is selected
            if (i == selectedWeaponIndex && isInMoveSelection) {
                List<Move> moves = weapon.getMoves();
                for (int j = 0; j < moves.size(); j++) {
                    Move move = moves.get(j);
                    
                    if (j == selectedMoveIndex) {
                        g.setColor(Color.YELLOW);
                        g.drawString("  > ", 25, y);
                    } else {
                        g.setColor(Color.LIGHT_GRAY);
                    }
                    
                    g.drawString(move.getName(), 45, y);
                    y += 15;
                }
            }
        }
    }
}