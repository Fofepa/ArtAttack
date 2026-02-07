package com.artattack.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.artattack.mapelements.MapElement;

public class InteractionPanel extends JPanel {
    private List<String> currentDialog;
    private int currentPhraseIndex = 0;
    private boolean dialogActive = false;
    private boolean choiceMode = false;
    
    private boolean textFullyRevealed = false; 
    private Timer textTimer;
    private int charIndex = 0;           
    private String currentFullText = ""; 
    private List<String> wrappedLines;   
    
    private int currentLineStart = 0; 
    
    private GameSettings.FontSize lastUsedFontSize;
    
    // NUOVO: Tiene traccia dell'ultima larghezza per gestire il resize
    private int lastWidth = -1;

    private int selectedOption = 0;
    private List<String> responseOptions;
    private Consumer<Integer> choiceCallback;
    
    // ========== IMAGE SYSTEM ==========
    private Image currentSpeakerImage = null;
    private Image defaultPlayerImage = null;
    private static final int IMAGE_SIZE = 180;
    private static final int IMAGE_PADDING = 10;
    
    public InteractionPanel() {
        setBackground(Color.BLACK);
        setFocusable(true); 
        textTimer = new Timer(GameSettings.getInstance().getTextSpeed().getDelay(), e -> revealNextCharacter());
        lastUsedFontSize = GameSettings.getInstance().getFontSize();
    }
    
    /**
     * Sets the default player image to show when no specific speaker is active
     */
    public void setDefaultPlayerImage(String imagePath) {
        try {
            defaultPlayerImage = ImageIO.read(new File(imagePath));
            currentSpeakerImage = defaultPlayerImage;
            repaint();
        } catch (IOException e) {
            System.err.println("Failed to load default player image: " + imagePath);
            e.printStackTrace();
        }
    }
    
    /**
     * Sets the speaker's image (overrides default player image)
     */
    public void setSpeakerImage(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            // Se non c'è immagine specifica, torna al default
            currentSpeakerImage = defaultPlayerImage;
            repaint();
            return;
        }
        
        try {
            currentSpeakerImage = ImageIO.read(new File(imagePath));
            repaint();
        } catch (IOException e) {
            System.err.println("Failed to load speaker image: " + imagePath);
            e.printStackTrace();
            // Fallback al default
            currentSpeakerImage = defaultPlayerImage;
        }
    }
    
    /**
     * Resets the speaker image to the default player image
     */
    public void resetToDefaultImage() {
        currentSpeakerImage = defaultPlayerImage;
        repaint();
    }
    
    public void showDialog(List<String> messages) {
        showDialog(messages, (String) null);
    }
    
    /**
     * Shows dialog with optional speaker image from path
     */
    public void showDialog(List<String> messages, String speakerImagePath) {
        this.currentDialog = messages;
        this.currentPhraseIndex = 0;
        this.dialogActive = true;
        this.choiceMode = false;
        
        // Set speaker image if provided, otherwise use default
        if (speakerImagePath != null && !speakerImagePath.isEmpty()) {
            setSpeakerImage(speakerImagePath);
        }else {
           // Ensure we reset to default if no specific image is provided
           resetToDefaultImage(); 
       }
        
        prepareNewPhrase(currentDialog.get(0));
        setVisible(true);
        repaint();
    }
    
    /**
     * Shows dialog with speaker as MapElement (uses element's sprite if available)
     */
    public void showDialog(List<String> messages, com.artattack.mapelements.MapElement speaker) {
        String speakerImagePath = null;
        if (speaker != null && speaker.hasSprite()) {
            speakerImagePath = speaker.getSpritePath();
        }
        showDialog(messages, speakerImagePath);
    }
    
    public void activateAndFocus() {
        this.setVisible(true);
        this.setEnabled(true);
        this.requestFocusInWindow();
        this.dialogActive = true; 
        this.revalidate();
        this.repaint();
    }
    
    public void showDialogWithChoice(String question, List<String> options, Consumer<Integer> callback) {
        showDialogWithChoice(question, options, callback, (String) null);
    }
    
    /**
     * Shows dialog with choice and optional speaker image from path
     */
    public void showDialogWithChoice(String question, List<String> options, Consumer<Integer> callback, String speakerImagePath) {
        this.currentDialog = List.of(question);
        this.currentPhraseIndex = 0;
        this.dialogActive = true;
        this.choiceMode = true;
        this.responseOptions = options;
        this.selectedOption = 0;
        this.choiceCallback = callback;
        
        // Set speaker image if provided
        if (speakerImagePath != null && !speakerImagePath.isEmpty()) {
            setSpeakerImage(speakerImagePath);
        }
        
        prepareNewPhrase(question);
        setVisible(true);
        repaint();
    }
    
    /**
     * Shows dialog with choice and speaker as MapElement (uses element's sprite if available)
     */
    public void showDialogWithChoice(String question, List<String> options, Consumer<Integer> callback, MapElement speaker) {
        String speakerImagePath = null;
        if (speaker != null && speaker.hasSprite()) {
            speakerImagePath = speaker.getSpritePath();
        }else {
           // Ensure we reset to default if no specific image is provided
           resetToDefaultImage(); 
        }
        showDialogWithChoice(question, options, callback, speakerImagePath);
    }
    
    private void prepareNewPhrase(String text) {
        this.currentFullText = text;
        this.currentLineStart = 0; 
        this.charIndex = 0;
        this.textFullyRevealed = false;
        this.wrappedLines = null; 
        startTextAnimation();
    }

    private void startTextAnimation() {
        int delay = GameSettings.getInstance().getTextSpeed().getDelay();
        if (delay == 0) {
            skipTextAnimation();
        } else {
            textTimer.setDelay(delay);
            textTimer.start();
        }
    }
    
    private void revealNextCharacter() {
        if (wrappedLines == null) return; 
        
        int linesPerPage = calculateLinesPerPage(getGraphics());
        int endIndex = Math.min(wrappedLines.size(), currentLineStart + linesPerPage);
        
        int totalCharsInPage = 0;
        for (int i = currentLineStart; i < endIndex; i++) {
            totalCharsInPage += wrappedLines.get(i).length();
        }
        
        if (charIndex < totalCharsInPage) {
            charIndex++;
            repaint();
        } else {
            textFullyRevealed = true;
            textTimer.stop();
            repaint();
        }
    }
    
    public void skipTextAnimation() {
        if (!textFullyRevealed && wrappedLines != null) {
            int linesPerPage = calculateLinesPerPage(getGraphics());
            int endIndex = Math.min(wrappedLines.size(), currentLineStart + linesPerPage);
            
            int totalCharsInPage = 0;
            for (int i = currentLineStart; i < endIndex; i++) {
                totalCharsInPage += wrappedLines.get(i).length();
            }
            
            charIndex = totalCharsInPage;
            textFullyRevealed = true;
            textTimer.stop();
            repaint();
        }
    }

    public void advanceDialog() {
        if (!textFullyRevealed) {
            skipTextAnimation();
            return;
        }

        int linesPerPage = calculateLinesPerPage(getGraphics());
        if (wrappedLines != null && (currentLineStart + linesPerPage) < wrappedLines.size()) {
            currentLineStart += linesPerPage;
            charIndex = 0; 
            textFullyRevealed = false;
            startTextAnimation();
            repaint();
            return;
        }

        if (currentPhraseIndex < currentDialog.size() - 1) {
            currentPhraseIndex++;
            prepareNewPhrase(currentDialog.get(currentPhraseIndex));
        } else {
            deactivate();
        }
        repaint();
    }
    
    public void deactivate() {
        dialogActive = false;
        
        // Reset to default player image when dialog closes
        repaint();
    }
    
    public void selectUp() {
        if (canSelect() && selectedOption > 0) {
            selectedOption--;
            repaint();
        }
    }

    public void selectDown() {
        if (canSelect() && responseOptions != null && selectedOption < responseOptions.size() - 1) {
            selectedOption++;
            repaint();
        }
    }
    
    private boolean canSelect() {
        if (!choiceMode || !textFullyRevealed) return false;
        int linesPerPage = calculateLinesPerPage(getGraphics());
        return wrappedLines != null && (currentLineStart + linesPerPage) >= wrappedLines.size();
    }
    
    public void confirmChoice() {
        if (!textFullyRevealed) {
            skipTextAnimation();
            return;
        }
        
        if (canSelect() && choiceCallback != null) {
            choiceCallback.accept(selectedOption);
            choiceMode = false;
        } else if (!choiceMode) {
            advanceDialog();
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        
        GameSettings.FontSize currentFontSize = GameSettings.getInstance().getFontSize();
        
        // --- CONTROLLO RESIZE E CAMBIO FONT ---
        int currentWidth = getWidth();
        if (currentFontSize != lastUsedFontSize || currentWidth != lastWidth) {
            wrappedLines = null;
            lastUsedFontSize = currentFontSize;
            lastWidth = currentWidth;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // ========== DRAW SPEAKER IMAGE ==========
        if (currentSpeakerImage != null) {
            int imageX = getWidth() - IMAGE_SIZE - IMAGE_PADDING;
            int imageY = IMAGE_PADDING;
            g2d.drawImage(currentSpeakerImage, imageX, imageY, IMAGE_SIZE, IMAGE_SIZE, this);
            
            // Optional: Draw border around image
            g2d.setColor(Color.WHITE);
            g2d.drawRect(imageX, imageY, IMAGE_SIZE, IMAGE_SIZE);
        }

        if (!dialogActive || currentDialog == null || currentDialog.isEmpty()) {
            return; // Se non c'è dialogo, mi fermo qui (ho disegnato solo l'immagine)
        }
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.PLAIN, currentFontSize.getSize()));
        FontMetrics fm = g.getFontMetrics();
        int lineHeight = fm.getHeight() + 5;
        
        if (wrappedLines == null) {
            wrappedLines = calculateWrappedLines(g, currentFullText);
        }
        
        int panelHeight = getHeight() - 40; 
        int linesPerPage = Math.max(1, panelHeight / lineHeight);
        int endIndex = Math.min(wrappedLines.size(), currentLineStart + linesPerPage);
        
        int y = 30;
        int charsToDraw = charIndex;
        
        for (int i = currentLineStart; i < endIndex; i++) {
            String line = wrappedLines.get(i);
            String lineToDraw;
            
            if (charsToDraw >= line.length()) {
                lineToDraw = line;
                charsToDraw -= line.length();
            } else if (charsToDraw > 0) {
                lineToDraw = line.substring(0, charsToDraw);
                charsToDraw = 0;
            } else {
                lineToDraw = "";
            }
            
            g.drawString(lineToDraw, 20, y);
            y += lineHeight;
        }
        
        if (textFullyRevealed) {
            
            boolean isLastPage = (endIndex >= wrappedLines.size());
            
            if (choiceMode && responseOptions != null && isLastPage) {
                y += 10;
                g.setColor(Color.GRAY);
                g.drawLine(20, y - 5, getWidth() - IMAGE_SIZE - IMAGE_PADDING - 40, y - 5);
                
                for (int i = 0; i < responseOptions.size(); i++) {
                    if (y > getHeight() - lineHeight) break; 
                    
                    if (i == selectedOption) {
                        g.setColor(Color.CYAN);
                        g.setFont(new Font("Monospaced", Font.BOLD, currentFontSize.getSize()));
                        g.drawString("> " + responseOptions.get(i), 20, y + fm.getAscent());
                    } else {
                        g.setColor(Color.LIGHT_GRAY);
                        g.setFont(new Font("Monospaced", Font.PLAIN, currentFontSize.getSize()));
                        g.drawString("  " + responseOptions.get(i), 20, y + fm.getAscent());
                    }
                    y += lineHeight;
                }
            } 
            else {
                String hint = "[Press ENTER]";
                g.setFont(new Font("Monospaced", Font.ITALIC, 14));
                g.setColor(new Color(255, 255, 255, 100)); 
                int hintWidth = g.getFontMetrics().stringWidth(hint);
                int hintX = getWidth() - IMAGE_SIZE - IMAGE_PADDING - hintWidth - 25;
                g.drawString(hint, hintX, getHeight() - 30);
                
                if (!isLastPage) {
                    g.setColor(Color.CYAN);
                    if ((System.currentTimeMillis() / 500) % 2 == 0) {
                        int arrowX = getWidth() - IMAGE_SIZE - IMAGE_PADDING - 30;
                        int[] xPoints = {arrowX, arrowX + 10, arrowX + 20};
                        int[] yPoints = {getHeight() - 25, getHeight() - 15, getHeight() - 25}; 
                        g.fillPolygon(xPoints, yPoints, 3);
                    }
                }
            }
        }
    }
    
    private int calculateLinesPerPage(Graphics g) {
        if (g == null) return 5;
        int panelHeight = getHeight() - 40; 
        int lineHeight = g.getFontMetrics().getHeight() + 5;
        return Math.max(1, panelHeight / lineHeight);
    }
    
    private List<String> calculateWrappedLines(Graphics g, String text) {
        List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();
        // Riduciamo la larghezza massima per fare spazio all'immagine
        int maxWidth = getWidth() - IMAGE_SIZE - IMAGE_PADDING - 60;
        FontMetrics fm = g.getFontMetrics();

        for (String word : words) {
            String potentialLine = currentLine + word + " ";
            if (fm.stringWidth(potentialLine) > maxWidth) {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder(word + " ");
            } else {
                currentLine.append(word).append(" ");
            }
        }
        lines.add(currentLine.toString());
        return lines;
    }
    
    public boolean isDialogActive() { return dialogActive; }
    public boolean isChoiceMode() { return choiceMode; }
    public boolean isTextFullyRevealed() { return textFullyRevealed; }
    public int getSelectedOption() { return selectedOption; }
    public List<String> getResponseOptions() { return responseOptions; }
}