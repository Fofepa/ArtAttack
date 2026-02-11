package com.artattack.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.artattack.mapelements.Player;
import com.artattack.moves.Move;
import com.artattack.moves.Weapon;

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
        this.selectedMoveIndex = 0; 
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

        if (!active) return; 
        
        if (player == null) {
            drawSimpleText(g, "No player");
            return;
        }
        
        List<Weapon> weapons = player.getWeapons();
        if (weapons.isEmpty() || selectedWeaponIndex >= weapons.size()) {
            drawSimpleText(g, "No weapon selected");
            return;
        }
        
        Weapon selectedWeapon = weapons.get(selectedWeaponIndex);
        List<Move> moves = selectedWeapon.getMoves();
        
        g.setColor(Color.CYAN);
        g.setFont(new Font("Monospaced", Font.BOLD, 12));
        g.drawString(selectedWeapon.getName(), 10, 20);
        
        int y = 40;
        int leftMargin = 20;
        int rightMargin = 10;
        int maxTextWidth = getWidth() - leftMargin - rightMargin;
        
        if (moves.isEmpty()) {
            g.setColor(Color.GRAY);
            g.setFont(new Font("Monospaced", Font.PLAIN, 11));
            g.drawString("No moves available", 10, y);
            return;
        }
        
        g.setFont(new Font("Monospaced", Font.PLAIN, 11));
        FontMetrics fm = g.getFontMetrics();
        int lineHeight = fm.getHeight(); 

        for (int i = 0; i < selectedWeapon.getMoves().size(); i++) { 
            Move move = moves.get(i);
            
            // Colore selezione
            if (i == selectedMoveIndex) {
                g.setColor(Color.YELLOW);
                g.drawString("> ", 5, y); 
            } else {
                g.setColor(Color.WHITE);
            }
            
            String moveLabel = move.getName() + " [" + move.getActionPoints() + " AP]";
            
            List<String> wrappedLines = getWrappedLines(g, moveLabel, maxTextWidth);
            
            for (String line : wrappedLines) {
                g.drawString(line, leftMargin, y);
                y += lineHeight; 
            }
            
            y += 5;
        }
        
        // Instruction
        g.setColor(Color.GRAY);
        g.setFont(new Font("Monospaced", Font.PLAIN, 10));
        g.drawString("Press « to go back", 10, getHeight() - 10);
    }

    private void drawSimpleText(Graphics g, String text) {
        g.setColor(Color.GRAY);
        g.setFont(new Font("Monospaced", Font.PLAIN, 12));
        g.drawString(text, 10, 20);
    }
    
    private List<String> getWrappedLines(Graphics g, String text, int maxWidth) {
        List<String> lines = new ArrayList<>();
        FontMetrics fm = g.getFontMetrics();
        
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();
        
        for (String word : words) {
            String potentialLine = currentLine + word + " ";
            if (fm.stringWidth(potentialLine) > maxWidth) {
                if (currentLine.length() > 0) {
                    lines.add(currentLine.toString());
                }
                currentLine = new StringBuilder(word + " ");
            } else {
                currentLine.append(word).append(" ");
            }
        }
        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }
        
        return lines;
    }
}