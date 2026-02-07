package com.artattack.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

import com.artattack.mapelements.Player;
import com.artattack.moves.Move;
import com.artattack.moves.Weapon;

/**
 * MovesPanel - Displays moves for the selected weapon
 */
public class MovesPanel extends JPanel {
    private Player player;
    private int selectedWeaponIndex = 0;
    private int selectedMoveIndex = 0;
    private boolean active = false;
    
    public MovesPanel(Player player) {
        this.player = player;
        setBackground(Color.BLACK);
        setFocusable(true);
    }

    public void setActive(boolean active) {
        this.active = active;
        repaint();
    }

    public void setPlayer(Player player) {
        this.player = player;
        selectedWeaponIndex = 0;
        selectedMoveIndex = 0;
        repaint();
    }
    
    public void setSelectedWeaponIndex(int index) {
        this.selectedWeaponIndex = index;
        this.selectedMoveIndex = 0; // Reset move selection when weapon changes
    }
    
    public int getSelectedMoveIndex() {
        return selectedMoveIndex;
    }
    
    public void setSelectedMoveIndex(int index) {
        if (player != null && !player.getWeapons().isEmpty()) {
            Weapon weapon = player.getWeapons().get(selectedWeaponIndex);
            if (!weapon.getMoves().isEmpty()) {
                this.selectedMoveIndex = Math.max(0, Math.min(index, weapon.getMoves().size() - 1));
            }
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!active) {
            return; 
        }
        
        if (player == null) {
            g.setColor(Color.GRAY);
            g.setFont(new Font("Monospaced", Font.PLAIN, 12));
            g.drawString("No player", 10, 20);
            return;
        }
        
        List<Weapon> weapons = player.getWeapons();
        
        if (weapons.isEmpty() || selectedWeaponIndex >= weapons.size()) {
            g.setColor(Color.GRAY);
            g.setFont(new Font("Monospaced", Font.PLAIN, 12));
            g.drawString("No weapon selected", 10, 20);
            return;
        }
        
        Weapon selectedWeapon = weapons.get(selectedWeaponIndex);
        List<Move> moves = selectedWeapon.getMoves();
        
        // Display weapon name
        g.setColor(Color.CYAN);
        g.setFont(new Font("Monospaced", Font.BOLD, 12));
        g.drawString(selectedWeapon.getName(), 10, 20);
        
        int y = 40;
        
        if (moves.isEmpty()) {
            g.setColor(Color.GRAY);
            g.setFont(new Font("Monospaced", Font.PLAIN, 11));
            g.drawString("No moves available", 10, y);
            return;
        }
        
        // Display moves
        g.setFont(new Font("Monospaced", Font.PLAIN, 11));
        for (int i = 0; i < moves.size(); i++) {
            Move move = moves.get(i);
            
            if (i == selectedMoveIndex) {
                g.setColor(Color.YELLOW);
                g.drawString("> ", 5, y);
            } else {
                g.setColor(Color.WHITE);
            }
            
            String moveLabel = move.getName() + " [" + move.getActionPoints() + " AP]";
            g.drawString(moveLabel, 20, y);
            y += 15;
        }
        
        // Instruction
        g.setColor(Color.GRAY);
        g.setFont(new Font("Monospaced", Font.PLAIN, 10));
        g.drawString("Press « to go back", 10, getHeight() - 10);
    }
}
