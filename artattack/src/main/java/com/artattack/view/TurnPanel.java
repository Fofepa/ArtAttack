package com.artattack.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import com.artattack.mapelements.ActiveElement;
import com.artattack.turns.ConcreteTurnHandler;
import com.artattack.turns.ConcreteTurnQueue;

class TurnPanel extends JPanel {
    private ConcreteTurnQueue turnQueue;
    private ConcreteTurnHandler turnHandler;
    private final Font FONT = new Font("Monospaced", Font.PLAIN, 12);
    private final Font TITLE_FONT = new Font("Monospaced", Font.BOLD, 14);
    private final int LINE_HEIGHT = 18;
    private final int MARGIN = 10;
    
    public TurnPanel(){
        setBackground(Color.BLACK);
    }
    
    public void setTurnSystem(ConcreteTurnQueue queue, ConcreteTurnHandler handler) {
        this.turnQueue = queue;
        this.turnHandler = handler;
        System.out.println("TurnPanel initialized with " + 
            (queue != null ? queue.getTurnQueue().size() : 0) + " elements");
        repaint();
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
        String title = "TURN ORDER";
        FontMetrics fm = g2d.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        g2d.drawString(title, (getWidth() - titleWidth) / 2, MARGIN + fm.getAscent());
        
        // Linea separatore
        int y = MARGIN + fm.getHeight() + 5;
        g2d.setColor(Color.WHITE);
        g2d.drawLine(MARGIN, y, getWidth() - MARGIN, y);
        
        y += 15;
        
        if (turnQueue == null || turnHandler == null) {
            g2d.setFont(FONT);
            g2d.setColor(Color.GRAY);
            String msg = "No turn system";
            int width = g2d.getFontMetrics().stringWidth(msg);
            g2d.drawString(msg, (getWidth() - width) / 2, y);
            return;
        }
        
        if (turnQueue.getTurnQueue().isEmpty()) {
            g2d.setFont(FONT);
            g2d.setColor(Color.GRAY);
            String msg = "No active turns";
            int width = g2d.getFontMetrics().stringWidth(msg);
            g2d.drawString(msg, (getWidth() - width) / 2, y);
            return;
        }
        
        // Disegna la lista dei turni
        g2d.setFont(FONT);
        int currentIndex = Math.max(0, turnHandler.getIndex() - 1);
        
        for (int i = 0; i < turnQueue.getTurnQueue().size(); i++) {
            try {
                ActiveElement element = turnQueue.getTurnQueue().get(i);
                boolean isCurrent = (i == currentIndex);
                
                drawTurnEntry(g2d, element, y, isCurrent, i + 1);
                y += LINE_HEIGHT;
            } catch (Exception e) {
                System.err.println("Error drawing turn entry " + i + ": " + e.getMessage());
            }
        }
    }
    
    private void drawTurnEntry(Graphics2D g2d, ActiveElement element, int y, 
                               boolean isCurrent, int position) {
        FontMetrics fm = g2d.getFontMetrics();
        
        if (isCurrent) {
            // Evidenzia il turno corrente
            g2d.setColor(new Color(0, 139, 139));
            g2d.fillRect(0, y - fm.getAscent() + 2, getWidth(), LINE_HEIGHT);
            
            // Freccia
            g2d.setColor(Color.CYAN);
            g2d.drawString(">", MARGIN, y);
        }
        
        // Testo
        g2d.setColor(isCurrent ? Color.CYAN : Color.WHITE);
        
        try {
            String line = String.format("%d. %c %-12s SPD:%d", 
                position,
                element.getMapSymbol(),
                element.getName(),
                element.getSpeed()
            );
            
            g2d.drawString(line, MARGIN + 15, y);
        } catch (Exception e) {
            // Fallback se i metodi non esistono
            g2d.drawString(position + ". " + element.toString(), MARGIN + 15, y);
        }
    }
    
    public void updateTurnDisplay() {
        repaint();
    }
    
    public ConcreteTurnQueue getTurnQueue() {
        return turnQueue;
    }
    
    public ConcreteTurnHandler getTurnHandler() {
        return turnHandler;
    }
}