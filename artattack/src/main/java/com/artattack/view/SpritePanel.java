package com.artattack.view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * SpritePanel - Displays character/NPC sprites during interactions
 */
public class SpritePanel extends JPanel {
    private BufferedImage currentSprite;
    private String currentSpritePath;
    
    public SpritePanel() {
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(200, 200));
        setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
    }
    
    /**
     * Loads and displays a sprite image
     * @param spritePath Path to the sprite image file
     */
    public void loadImage(String spritePath) {
        try {
            if (spritePath != null && !spritePath.isEmpty()) {
                File imageFile = new File(spritePath);
                if (imageFile.exists()) {
                    currentSprite = ImageIO.read(imageFile);
                    currentSpritePath = spritePath;
                    repaint();
                } else {
                    System.err.println("Sprite file not found: " + spritePath);
                    currentSprite = null;
                    repaint();
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading sprite: " + spritePath);
            e.printStackTrace();
            currentSprite = null;
            repaint();
        }
    }
    
    /**
     * Clears the currently displayed sprite
     */
    public void clearSprite() {
        currentSprite = null;
        currentSpritePath = null;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (currentSprite != null) {
            // Calculate scaling to fit the panel while maintaining aspect ratio
            int panelWidth = getWidth();
            int panelHeight = getHeight();
            int imgWidth = currentSprite.getWidth();
            int imgHeight = currentSprite.getHeight();
            
            double scaleX = (double) panelWidth / imgWidth;
            double scaleY = (double) panelHeight / imgHeight;
            double scale = Math.min(scaleX, scaleY) * 0.9; // 90% to leave some padding
            
            int scaledWidth = (int) (imgWidth * scale);
            int scaledHeight = (int) (imgHeight * scale);
            
            // Center the image
            int x = (panelWidth - scaledWidth) / 2;
            int y = (panelHeight - scaledHeight) / 2;
            
            g.drawImage(currentSprite, x, y, scaledWidth, scaledHeight, this);
        } else {
            // Draw placeholder text when no sprite is loaded
            g.setColor(Color.GRAY);
            g.setFont(new Font("Monospaced", Font.PLAIN, 12));
            String text = "No sprite";
            FontMetrics fm = g.getFontMetrics();
            int textX = (getWidth() - fm.stringWidth(text)) / 2;
            int textY = (getHeight() + fm.getAscent()) / 2;
            g.drawString(text, textX, textY);
        }
    }
    
    public String getCurrentSpritePath() {
        return currentSpritePath;
    }
    
    public boolean hasSprite() {
        return currentSprite != null;
    }
}