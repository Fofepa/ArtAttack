package com.artattack.view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class CharacterSelectionPanel extends JPanel {
    private final MainGUIFacade mainFacade;
    private final CharacterType[] types = CharacterType.values();
    private Map<CharacterType, Image> characterImages;
    
    private int hoverIndex = 0;
    private CharacterType player1Choice = null;
    private boolean isPlayer1Turn = true;
    
    private final int CARD_WIDTH = 280;
    private final int CARD_HEIGHT = 400; 
    private final int IMAGE_HEIGHT = 180; 

    public CharacterSelectionPanel(MainGUIFacade mainFacade) {
        this.mainFacade = mainFacade;
        loadCharacterImages();
        initializePanel();
        setupInput();
    }

    private void loadCharacterImages() {
        characterImages = new HashMap<>();
        for (CharacterType type : types) {
            String path = switch (type) {
                case MUSICIAN -> "/images/frank-zappa-fotor-20260206135640.jpg";
                case DIRECTOR -> "/images/ozxg45isal6ve56l7tl6-fotor-20260206135846.jpg";
                default -> "";
            };
            
            if (!path.isEmpty()) {
                try {
                    URL imgUrl = getClass().getResource(path);
                    if (imgUrl != null) {
                        characterImages.put(type, ImageIO.read(imgUrl));
                    }
                } catch (IOException e) {
                    System.err.println("Failed to load image for: " + type);
                }
            }
        }
    }

    private void initializePanel() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        setFocusable(true);
    }

    private void setupInput() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT, KeyEvent.VK_A -> moveSelection(-1);
                    case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> moveSelection(1);
                    case KeyEvent.VK_ENTER, KeyEvent.VK_SPACE -> confirmSelection();
                    case KeyEvent.VK_ESCAPE -> handleBackNavigation();
                }
            }
        });
    }

    private void handleBackNavigation() {
        if (!isPlayer1Turn) {
            isPlayer1Turn = true;
            player1Choice = null;
            repaint();
        } else {
            mainFacade.showMenu();
        }
    }

    private void moveSelection(int delta) {
        hoverIndex = (hoverIndex + delta + types.length) % types.length;
        repaint();
    }

    private void confirmSelection() {
        CharacterType selected = types[hoverIndex];

        // Prevent P2 from picking P1's character
        if (!isPlayer1Turn && selected == player1Choice) {
            return;
        }

        if (isPlayer1Turn) {
            player1Choice = selected;
            isPlayer1Turn = false;
            // Move hover to next available
            hoverIndex = (hoverIndex + 1) % types.length;
            repaint();
        } else {
            mainFacade.finalizeGameSetup(player1Choice, selected);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        // High quality rendering settings
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int width = getWidth();
        int height = getHeight();

        // Title rendering
        String title = isPlayer1Turn ? "PLAYER 1: CHOOSE CHARACTER" : "PLAYER 2: CHOOSE CHARACTER";
        g2.setFont(new Font("Monospaced", Font.BOLD, 40));
        g2.setColor(isPlayer1Turn ? Color.GREEN : Color.MAGENTA);
        drawCenteredString(g2, title, width / 2, 60);

        // Calculate card layout
        int gap = 50;
        int totalWidth = (types.length * CARD_WIDTH) + ((types.length - 1) * gap);
        int startX = (width - totalWidth) / 2;
        int startY = (height - CARD_HEIGHT) / 2 + 20;

        for (int i = 0; i < types.length; i++) {
            drawCharacterCard(g2, types[i], startX + (i * (CARD_WIDTH + gap)), startY, i == hoverIndex);
        }
        
        // Bottom instructions
        g2.setFont(new Font("Monospaced", Font.ITALIC, 16));
        g2.setColor(Color.GRAY);
        String instruction = isPlayer1Turn ? "Press ENTER to confirm" : "Select a distinct character";
        drawCenteredString(g2, instruction, width / 2, height - 50);
    }

    private void drawCharacterCard(Graphics2D g2, CharacterType type, int x, int y, boolean isHovered) {
        boolean isTaken = (type == player1Choice);

        // Draw card background
        g2.setColor(isTaken ? new Color(20, 20, 20) : (isHovered ? new Color(45, 45, 45) : new Color(30, 30, 30)));
        g2.fillRoundRect(x, y, CARD_WIDTH, CARD_HEIGHT, 15, 15);

        // Draw border
        if (isHovered) {
            g2.setColor(isPlayer1Turn ? Color.CYAN : Color.MAGENTA);
            g2.setStroke(new BasicStroke(3));
        } else {
            g2.setColor(isTaken ? Color.DARK_GRAY : Color.GRAY);
            g2.setStroke(new BasicStroke(isTaken ? 2 : 1));
        }
        g2.drawRoundRect(x, y, CARD_WIDTH, CARD_HEIGHT, 15, 15);

        // Draw character image
        Image img = characterImages.get(type);
        int imgPadding = 10;
        int imgW = CARD_WIDTH - (imgPadding * 2);
        
        if (img != null) {
            g2.drawImage(img, x + imgPadding, y + imgPadding, imgW, IMAGE_HEIGHT, null);
        } else {
            g2.setColor(Color.BLACK);
            g2.fillRect(x + imgPadding, y + imgPadding, imgW, IMAGE_HEIGHT);
            g2.setColor(Color.GRAY);
            drawCenteredString(g2, "NO IMAGE", x + CARD_WIDTH / 2, y + IMAGE_HEIGHT / 2);
        }

        // Name and Description
        int currentY = y + IMAGE_HEIGHT + 35;
        g2.setColor(isTaken ? Color.GRAY : Color.WHITE);
        g2.setFont(new Font("Monospaced", Font.BOLD, 22));
        drawCenteredString(g2, type.getName(), x + CARD_WIDTH / 2, currentY);
        
        currentY += 20;
        g2.setFont(new Font("Monospaced", Font.ITALIC, 12));
        g2.setColor(Color.LIGHT_GRAY);
        drawCenteredString(g2, type.getDescription(), x + CARD_WIDTH / 2, currentY);

        // Stats section
        currentY += 15;
        g2.setColor(Color.DARK_GRAY);
        g2.drawLine(x + 20, currentY, x + CARD_WIDTH - 20, currentY);

        currentY += 25;
        g2.setFont(new Font("Monospaced", Font.PLAIN, 14));
        g2.setColor(Color.WHITE);
        g2.drawString("Wpn: " + type.getWeaponName(), x + 20, currentY);
        
        currentY += 25;
        drawStatBar(g2, "HP", 19, type.getMaxHP(), Color.RED, x + 20, currentY, CARD_WIDTH - 40);
        
        currentY += 30;
        drawStatBar(g2, "Spd", type.getSpeed(), 10, Color.YELLOW, x + 20, currentY, CARD_WIDTH - 40);

        // Taken overlay
        if (isTaken) {
            renderTakenOverlay(g2, x, y);
        }
        
        // Hover label
        if (isHovered && !isTaken) {
            g2.setColor(isPlayer1Turn ? Color.CYAN : Color.MAGENTA);
            g2.setFont(new Font("Monospaced", Font.BOLD, 14));
            drawCenteredString(g2, "▲ " + (isPlayer1Turn ? "P1 Select" : "P2 Select") + " ▲", x + CARD_WIDTH / 2, y + CARD_HEIGHT + 20);
        }
    }

    private void renderTakenOverlay(Graphics2D g2, int x, int y) {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRoundRect(x, y, CARD_WIDTH, CARD_HEIGHT, 15, 15);
        
        g2.setColor(new Color(255, 50, 50)); 
        g2.setFont(new Font("Monospaced", Font.BOLD, 40));
        
        AffineTransform orig = g2.getTransform();
        g2.rotate(Math.toRadians(-20), x + CARD_WIDTH / 2.0, y + CARD_HEIGHT / 2.0);
        drawCenteredString(g2, "TAKEN", x + CARD_WIDTH / 2, y + CARD_HEIGHT / 2);
        g2.setTransform(orig);
        
        g2.setFont(new Font("Monospaced", Font.BOLD, 16));
        g2.setColor(Color.WHITE);
        drawCenteredString(g2, "by Player 1", x + CARD_WIDTH / 2, y + CARD_HEIGHT / 2 + 35);
    }

    private void drawStatBar(Graphics2D g2, String label, int value, int max, Color color, int x, int y, int width) {
        int height = 8;
        int labelWidth = 30;
        
        g2.setColor(Color.LIGHT_GRAY);
        g2.setFont(new Font("Monospaced", Font.BOLD, 12));
        g2.drawString(label, x, y + height);

        int barStartX = x + labelWidth + 5;
        int actualBarWidth = width - labelWidth - 5;

        g2.setColor(new Color(50, 50, 50));
        g2.fillRect(barStartX, y, actualBarWidth, height);

        int fillWidth = Math.min((int) (((double) value / max) * actualBarWidth), actualBarWidth);
        g2.setColor(color);
        g2.fillRect(barStartX, y, fillWidth, height);
        
        g2.setColor(new Color(100, 100, 100));
        g2.drawRect(barStartX, y, actualBarWidth, height);
    }

    private void drawCenteredString(Graphics g, String text, int x, int y) {
        if (text == null) return;
        FontMetrics metrics = g.getFontMetrics();
        int dX = x - (metrics.stringWidth(text) / 2);
        int dY = y + (metrics.getAscent() - metrics.getDescent()) / 2;
        g.drawString(text, dX, dY);
    }
}