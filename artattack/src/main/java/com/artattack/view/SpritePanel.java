package com.artattack.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * SpritePanel - Displays character/NPC sprites using Pixel Perfect rendering
 */
public class SpritePanel extends JPanel {
    private BufferedImage currentSprite;
    // Cache used to buffer the images instead of reloading it every time
    private static final Map<String, BufferedImage> imageCache = new HashMap<>();
    
    public SpritePanel() {
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(200, 200));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 1),
            BorderFactory.createLineBorder(Color.BLACK, 4)
        ));
    }
    
    //Loads and displays a sprite image from resources
    //@param spriteName The name of the file in the resources folder (e.g., "zappa_portrait.png")
    public void loadImage(String spriteName) {
        if (spriteName == null || spriteName.isEmpty()) {
            clearSprite();
            return;
        }

        // Chache check
        if (imageCache.containsKey(spriteName)) {
            currentSprite = imageCache.get(spriteName);
            repaint();
            return;
        }

        // Loading of the resources
        try {
            // the sprites must be in resources
            URL imgUrl = getClass().getResource("/images/" + spriteName);
            if (imgUrl == null) {
                imgUrl = getClass().getResource("/" + spriteName);
            }
            
            if (imgUrl != null) {
                BufferedImage img = ImageIO.read(imgUrl);
                imageCache.put(spriteName, img); // Store in chache
                currentSprite = img;
                repaint();
            } else {
                System.err.println("Sprite resource not found: " + spriteName);
                clearSprite();
            }
        } catch (IOException e) {
            System.err.println("Error loading sprite: " + spriteName);
            e.printStackTrace();
            clearSprite();
        }
    }
    
    public void clearSprite() {
        try {
            currentSprite = ImageIO.read(getClass().getResource("/image/thevoid.jpg"));
            repaint();
        } catch (IOException|IllegalArgumentException e) {
            System.err.println("Error loading the image: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (currentSprite != null) {
            Graphics2D g2d = (Graphics2D) g;
            // pixel manipulation to make them look great
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

            int panelWidth = getWidth();
            int panelHeight = getHeight();
            int imgWidth = currentSprite.getWidth();
            int imgHeight = currentSprite.getHeight();
            
            // scale with the correct ratio
            double scaleX = (double) (panelWidth - 20) / imgWidth; // -20 per padding
            double scaleY = (double) (panelHeight - 20) / imgHeight;
            double scale = Math.min(scaleX, scaleY);
            
            int scaledWidth = (int) (imgWidth * scale);
            int scaledHeight = (int) (imgHeight * scale);
            
            int x = (panelWidth - scaledWidth) / 2;
            int y = (panelHeight - scaledHeight) / 2;
            
            g2d.drawImage(currentSprite, x, y, scaledWidth, scaledHeight, this);
        } else {
            // if no image is present at all use this placeholder
            g.setColor(new Color(30, 30, 30));
            g.fillRect(10, 10, getWidth()-20, getHeight()-20);
            g.setColor(Color.DARK_GRAY);
            g.setFont(new Font("Monospaced", Font.BOLD, 14));
            String text = "NO SIGNAL";
            FontMetrics fm = g.getFontMetrics();
            g.drawString(text, (getWidth() - fm.stringWidth(text))/2, (getHeight()/2) + 5);
        }
    }
}