package com.artattack.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;
import javax.swing.Timer;

import com.artattack.Player;

public class StatsPanel extends JPanel {
    private Player currentPlayer;
    
    private final Font TITLE_FONT = new Font("Monospaced", Font.BOLD, 16);
    private final Font LABEL_FONT = new Font("Monospaced", Font.PLAIN, 12);
    private final Font VALUE_FONT = new Font("Monospaced", Font.BOLD, 12);
    
    private final int MARGIN = 10;
    private final int LINE_HEIGHT = 18;
    private final int BAR_HEIGHT = 12;
    private final int BAR_WIDTH_RATIO = 70; // percentuale della larghezza

    private boolean lowHealthBlink = false;
    private Timer blinkTimer;
    
    public StatsPanel() {
        setBackground(Color.BLACK);

        blinkTimer = new Timer(500, e -> {
            if (currentPlayer != null && 
                currentPlayer.getCurrHP() > 0 && 
                (double) currentPlayer.getCurrHP() / currentPlayer.getMaxHP() < 0.25) {
                lowHealthBlink = !lowHealthBlink;
                repaint();
            }
        });
        blinkTimer.start();
    
    }
    
    /**
     * Imposta il giocatore corrente e aggiorna le statistiche
     */
    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
        repaint();
        
        if (player != null) {
            System.out.println("StatsPanel: Updated to player " + player.getName());
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
                             RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        
        // Titolo
        g2d.setFont(TITLE_FONT);
        g2d.setColor(Color.WHITE);
        String title = "STATS";
        FontMetrics fm = g2d.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        g2d.drawString(title, (getWidth() - titleWidth) / 2, MARGIN + fm.getAscent());
        
        // Linea separatore
        int y = MARGIN + fm.getHeight() + 5;
        g2d.drawLine(MARGIN, y, getWidth() - MARGIN, y);
        
        y += 15;
        
        if (currentPlayer == null) {
            // Nessun giocatore selezionato
            g2d.setFont(LABEL_FONT);
            g2d.setColor(Color.GRAY);
            String msg = "No player selected";
            int width = g2d.getFontMetrics().stringWidth(msg);
            g2d.drawString(msg, (getWidth() - width) / 2, getHeight() / 2);
            return;
        }
        
        // ========== NOME DEL PERSONAGGIO ==========
        g2d.setFont(new Font("Monospaced", Font.BOLD, 14));
        g2d.setColor(Color.CYAN);
        g2d.drawString(currentPlayer.getName(), MARGIN, y);
        y += LINE_HEIGHT + 5;
        
        // ========== HP (HEALTH POINTS) ==========
        y = drawStatBar(g2d, "HP", 
                       currentPlayer.getCurrHP(), 
                       currentPlayer.getMaxHP(), 
                       new Color(220, 20, 60),  // Rosso
                       new Color(139, 0, 0),     // Rosso scuro
                       y);
        
        // ========== AP (ACTION POINTS) ==========
        y = drawStatBar(g2d, "AP", 
                       currentPlayer.getActionPoints(), 
                       currentPlayer.getMaxActionPoints(), 
                       new Color(65, 105, 225),  // Blu
                       new Color(25, 25, 112),    // Blu scuro
                       y);
        
        // ========== XP (EXPERIENCE) ==========
        y = drawStatBar(g2d, "XP", 
                       currentPlayer.getCurrXP(), 
                       currentPlayer.getMaxXP(), 
                       new Color(255, 215, 0),   // Oro
                       new Color(184, 134, 11),  // Oro scuro
                       y);
        
        y += 5; // Spazio extra
        
        // ========== STATISTICHE BASE ==========
        y = drawStatLine(g2d, "LVL", String.valueOf(currentPlayer.getLevel()), Color.CYAN, y);
        y = drawStatLine(g2d, "SPD", String.valueOf(currentPlayer.getSpeed()), Color.GREEN, y);
        
        // Indicatore Level Up
        if (currentPlayer.getLeveledUp()) {
            g2d.setFont(new Font("Monospaced", Font.BOLD, 12));
            g2d.setColor(Color.YELLOW);
            g2d.drawString("★ LEVEL UP! ★", MARGIN, y);
            y += LINE_HEIGHT;
        }
        
        // ========== INVENTARIO ==========
        y += 5;
        g2d.setFont(LABEL_FONT);
        g2d.setColor(Color.GRAY);
        g2d.drawString("─────────────────", MARGIN, y);
        y += LINE_HEIGHT;
        
        // Armi
        g2d.setColor(Color.YELLOW);
        g2d.drawString("Weapons:", MARGIN, y);
        y += LINE_HEIGHT;
        
        if (currentPlayer.getWeapons() != null && !currentPlayer.getWeapons().isEmpty()) {
            g2d.setFont(LABEL_FONT);
            g2d.setColor(Color.WHITE);
            
            int maxWeaponsToShow = Math.min(2, currentPlayer.getWeapons().size());
            
            for (int i = 0; i < maxWeaponsToShow; i++) {
                String weaponName = currentPlayer.getWeapons().get(i).getName();
                if (weaponName.length() > 12) {
                    weaponName = weaponName.substring(0, 9) + "...";
                }
                g2d.drawString("• " + weaponName, MARGIN + 5, y);
                y += LINE_HEIGHT - 3;
            }
            
            if (currentPlayer.getWeapons().size() > 2) {
                g2d.setColor(Color.GRAY);
                g2d.drawString("  +" + (currentPlayer.getWeapons().size() - 2) + " more", 
                              MARGIN + 5, y);
                y += LINE_HEIGHT - 3;
            }
            
            // Slots armi
            g2d.setColor(Color.GRAY);
            g2d.setFont(new Font("Monospaced", Font.PLAIN, 10));
            g2d.drawString("Slots: " + currentPlayer.getWeapons().size() + "/" + 
                          currentPlayer.getMaxWeapons(), MARGIN + 5, y);
            y += LINE_HEIGHT;
        } else {
            g2d.setColor(Color.GRAY);
            g2d.drawString("  No weapons", MARGIN + 5, y);
            y += LINE_HEIGHT;
        }
        
        // Items
        g2d.setFont(LABEL_FONT);
        g2d.setColor(Color.YELLOW);
        g2d.drawString("Items:", MARGIN, y);
        y += LINE_HEIGHT;
        
        if (currentPlayer.getInventory() != null && !currentPlayer.getInventory().isEmpty()) {
            g2d.setColor(Color.WHITE);
            g2d.drawString("  " + currentPlayer.getInventory().size() + " items", MARGIN + 5, y);
            y += LINE_HEIGHT;
        } else {
            g2d.setColor(Color.GRAY);
            g2d.drawString("  Empty", MARGIN + 5, y);
            y += LINE_HEIGHT;
        }
        
        // Keys
        g2d.setFont(LABEL_FONT);
        g2d.setColor(Color.YELLOW);
        g2d.drawString("Keys:", MARGIN, y);
        y += LINE_HEIGHT;
        
        if (currentPlayer.getKeys() != null && !currentPlayer.getKeys().isEmpty()) {
            g2d.setColor(Color.WHITE);
            g2d.drawString("  " + currentPlayer.getKeys().size() + " keys", MARGIN + 5, y);
        } else {
            g2d.setColor(Color.GRAY);
            g2d.drawString("  No keys", MARGIN + 5, y);
        }
    }
    
    /**
     * Disegna una barra di statistiche con valore/massimo
     */
    private int drawStatBar(Graphics2D g2d, String label, int current, int max, 
                           Color fillColor, Color bgColor, int y) {
        // Label
        g2d.setFont(LABEL_FONT);
        g2d.setColor(Color.GRAY);
        g2d.drawString(label + ":", MARGIN, y);
        
        // Valori numerici
        g2d.setFont(VALUE_FONT);
        g2d.setColor(Color.WHITE);
        String valueText = current + "/" + max;
        int valueX = getWidth() - MARGIN - g2d.getFontMetrics().stringWidth(valueText);
        g2d.drawString(valueText, valueX, y);
        
        y += LINE_HEIGHT - 2;
        
        // Barra di progresso
        int barWidth = (getWidth() - 2 * MARGIN) * BAR_WIDTH_RATIO / 100;
        int barX = MARGIN;
        
        // Background della barra
        g2d.setColor(bgColor);
        g2d.fillRect(barX, y - BAR_HEIGHT + 2, barWidth, BAR_HEIGHT);
        
        // Bordo
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawRect(barX, y - BAR_HEIGHT + 2, barWidth, BAR_HEIGHT);
        
        // Riempimento proporzionale
        if (max > 0) {
            int fillWidth = (int) ((double) current / max * barWidth);
            fillWidth = Math.max(0, Math.min(fillWidth, barWidth)); // Clamp
            
            g2d.setColor(fillColor);
            g2d.fillRect(barX + 1, y - BAR_HEIGHT + 3, fillWidth - 1, BAR_HEIGHT - 1);
            
            // Effetto "low health" - lampeggia quando HP < 25%
            if (label.equals("HP") && current > 0 && (double) current / max < 0.25 && lowHealthBlink) {
                g2d.setColor(new Color(255, 255, 0, 150)); // Giallo lampeggiante
                g2d.fillRect(barX + 1, y - BAR_HEIGHT + 3, fillWidth - 1, BAR_HEIGHT - 1);
            }
        }
        
        y += LINE_HEIGHT - 5;
        return y;
    }
    
    /**
     * Disegna una linea di statistica semplice (label: valore)
     */
    private int drawStatLine(Graphics2D g2d, String label, String value, Color valueColor, int y) {
        g2d.setFont(LABEL_FONT);
        g2d.setColor(Color.GRAY);
        g2d.drawString(label + ":", MARGIN, y);
        
        g2d.setFont(VALUE_FONT);
        g2d.setColor(valueColor);
        int valueX = getWidth() - MARGIN - g2d.getFontMetrics().stringWidth(value);
        g2d.drawString(value, valueX, y);
        
        y += LINE_HEIGHT;
        return y;
    }
    
    /**
     * Aggiorna il pannello (forza repaint)
     */
    public void updateStats() {
        repaint();
    }
    
    /**
     * Ottieni il giocatore corrente
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}