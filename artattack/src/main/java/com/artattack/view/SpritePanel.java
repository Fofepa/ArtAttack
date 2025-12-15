package com.artattack.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class SpritePanel extends JPanel {
    private Image spriteImage;
    private boolean keepAspectRatio = true;
    
    public SpritePanel() {
        setBackground(Color.BLACK);
    }
    
    /**
     * Load an image from file path
     * @param imagePath Path to the image file
     * @return true if loaded successfully, false otherwise
     */
    public boolean loadImage(String imagePath) {
        try {
            File imageFile = new File(imagePath);
            spriteImage = ImageIO.read(imageFile);
            repaint();
            return true;
        } catch (IOException e) {
            System.err.println("Failed to load image: " + imagePath);
            e.printStackTrace();
            spriteImage = null;
            repaint();
            return false;
        }
    }
    
    /**
     * Load an image from File object
     * @param imageFile File object of the image
     * @return true if loaded successfully, false otherwise
     */
    public boolean loadImage(File imageFile) {
        try {
            spriteImage = ImageIO.read(imageFile);
            repaint();
            return true;
        } catch (IOException e) {
            System.err.println("Failed to load image: " + imageFile.getAbsolutePath());
            e.printStackTrace();
            spriteImage = null;
            repaint();
            return false;
        }
    }
    
    /**
     * Set the image directly
     * @param image Image to display
     */
    public void setImage(Image image) {
        this.spriteImage = image;
        repaint();
    }
    
    /**
     * Clear the current image
     */
    public void clearImage() {
        spriteImage = null;
        repaint();
    }
    
    /**
     * Set whether to keep aspect ratio when scaling
     * @param keepAspectRatio true to keep aspect ratio, false to stretch
     */
    public void setKeepAspectRatio(boolean keepAspectRatio) {
        this.keepAspectRatio = keepAspectRatio;
        repaint();
    }
    
    /**
     * Get the current image
     * @return Current image or null if no image loaded
     */
    public Image getImage() {
        return spriteImage;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (spriteImage == null) {
            return;
        }
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int imageWidth = spriteImage.getWidth(this);
        int imageHeight = spriteImage.getHeight(this);
        
        if (keepAspectRatio) {
            // Calculate scaling to fit within panel while maintaining aspect ratio
            double scaleX = (double) panelWidth / imageWidth;
            double scaleY = (double) panelHeight / imageHeight;
            double scale = Math.min(scaleX, scaleY);
            
            int scaledWidth = (int) (imageWidth * scale);
            int scaledHeight = (int) (imageHeight * scale);
            
            // Center the image
            int x = (panelWidth - scaledWidth) / 2;
            int y = (panelHeight - scaledHeight) / 2;
            
            g2d.drawImage(spriteImage, x, y, scaledWidth, scaledHeight, this);
        } else {
            // Stretch to fill entire panel
            g2d.drawImage(spriteImage, 0, 0, panelWidth, panelHeight, this);
        }
    }
}