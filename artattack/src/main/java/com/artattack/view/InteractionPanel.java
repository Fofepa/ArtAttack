package com.artattack.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

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
        setDoubleBuffered(true);

        textTimer = new Timer(GameSettings.getInstance().getTextSpeed().getDelay(), e -> revealNextCharacter());
        textTimer.setCoalesce(false);
        
        lastUsedFontSize = GameSettings.getInstance().getFontSize();
    }
    
    /**
     * Carica l'immagine di default del giocatore. 
     * Questa viene usata come fallback se non c'è uno spritePath valido.
     */
    public void setDefaultPlayerImage(String imagePath) {
        try {
            URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl == null) {
                System.err.println("ERRORE: Immagine default non trovata: " + imagePath);
                return;
            }
            defaultPlayerImage = ImageIO.read(imageUrl);
            // Se non c'è nessuno speaker attivo, mostra subito il player
            if (currentSpeakerImage == null) {
                currentSpeakerImage = defaultPlayerImage;
            }
            repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Logica centrale per gestire l'immagine.
     * Se path esiste -> carica NPC.
     * Se path null/errore -> carica Default Player.
     */
    private void updateSpeakerImage(String imagePath) {
        // CASO 1: Nessun path fornito -> Mostra Player di default
        if (imagePath == null || imagePath.isEmpty()) {
            currentSpeakerImage = defaultPlayerImage;
            repaint();
            return;
        }

        // CASO 2: Path fornito -> Provo a caricare
        Image loadedImage = null;
        try {
            // Tentativo 1: Stream (Migliore per risorse compilate)
            InputStream is = getClass().getResourceAsStream(imagePath);
            
            // Tentativo 2: Classloader (senza slash iniziale)
            if (is == null && imagePath.startsWith("/")) {
                String pathNoSlash = imagePath.substring(1);
                is = getClass().getClassLoader().getResourceAsStream(pathNoSlash);
            }

            if (is != null) {
                loadedImage = ImageIO.read(is);
                is.close();
            } else {
                System.err.println("Immagine non trovata nel classpath: " + imagePath);
            }

        } catch (IOException e) {
            System.err.println("Errore lettura immagine: " + imagePath);
            e.printStackTrace();
        }

        // Se il caricamento ha avuto successo usa quella, altrimenti fallback sul player
        if (loadedImage != null) {
            currentSpeakerImage = loadedImage;
        } else {
            currentSpeakerImage = defaultPlayerImage;
        }
        repaint();
    }

    // =================================================================================
    // UNICO METODO SHOW DIALOG (Standard)
    // =================================================================================
    public void showDialog(List<String> messages, String spritePath) {
        this.currentDialog = messages;
        this.currentPhraseIndex = 0;
        this.dialogActive = true;
        this.choiceMode = false;
        
        // Aggiorna l'immagine in base al path (NPC o Default)
        updateSpeakerImage(spritePath);
        
        if (currentDialog != null && !currentDialog.isEmpty()) {
            prepareNewPhrase(currentDialog.get(0));
        }
        
        activateAndFocus();
    }
    
    // =================================================================================
    // UNICO METODO SHOW DIALOG WITH CHOICE
    // =================================================================================
    public void showDialogWithChoice(String question, List<String> options, Consumer<Integer> callback, String spritePath) {
        this.currentDialog = List.of(question);
        this.currentPhraseIndex = 0;
        this.dialogActive = true;
        this.choiceMode = true;
        this.responseOptions = options;
        this.selectedOption = 0;
        this.choiceCallback = callback;
        
        // Aggiorna l'immagine in base al path (NPC o Default)
        updateSpeakerImage(spritePath);
        
        prepareNewPhrase(question);
        
        activateAndFocus();
    }
    
    public void activateAndFocus() {
        this.setVisible(true);
        this.setEnabled(true);
        this.requestFocusInWindow();
        this.revalidate();
        this.repaint();
    }
    
    // ================= LOGICA TESTO E ANIMAZIONE =================

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
            paintImmediately(0, 0, getWidth(), getHeight());
            Toolkit.getDefaultToolkit().sync();
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
        // Quando chiudi, resetta al player di default per sicurezza
        currentSpeakerImage = defaultPlayerImage; 
        repaint();
    }
    
    // ================= INPUT HANDLING =================

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

    // ================= RENDERING =================

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        GameSettings.FontSize currentFontSize = GameSettings.getInstance().getFontSize();
        
        // Controllo resize
        int currentWidth = getWidth();
        if (currentFontSize != lastUsedFontSize || currentWidth != lastWidth) {
            wrappedLines = null;
            lastUsedFontSize = currentFontSize;
            lastWidth = currentWidth;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // 1. DISEGNA IMMAGINE (NPC o Default)
        if (currentSpeakerImage != null) {
            int imageX = getWidth() - IMAGE_SIZE - IMAGE_PADDING;
            int imageY = IMAGE_PADDING;
            g2d.drawImage(currentSpeakerImage, imageX, imageY, IMAGE_SIZE, IMAGE_SIZE, this);
            
            // Bordo bianco semplice
            g2d.setColor(Color.WHITE);
            g2d.drawRect(imageX, imageY, IMAGE_SIZE, IMAGE_SIZE);
        }

        if (!dialogActive || currentDialog == null || currentDialog.isEmpty()) {
            return;
        }
        
        // 2. DISEGNA TESTO
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
        
        // 3. DISEGNA OPZIONI O HINT
        if (textFullyRevealed) {
            boolean isLastPage = (endIndex >= wrappedLines.size());
            
            if (choiceMode && responseOptions != null && isLastPage) {
                y += 10;
                int lineWidth = getWidth() - 40;
                if (currentSpeakerImage != null) lineWidth -= (IMAGE_SIZE + IMAGE_PADDING);

                g.setColor(Color.GRAY);
                g.drawLine(20, y - 5, lineWidth, y - 5);
                
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
            } else {
                String hint = "[Press ENTER]";
                g.setFont(new Font("Monospaced", Font.ITALIC, 14));
                g.setColor(new Color(255, 255, 255, 100)); 
                int hintWidth = g.getFontMetrics().stringWidth(hint);
                
                int hintX = getWidth() - hintWidth - 25;
                if (currentSpeakerImage != null) {
                    hintX -= (IMAGE_SIZE + IMAGE_PADDING);
                }

                g.drawString(hint, hintX, getHeight() - 30);
                
                if (!isLastPage) {
                    g.setColor(Color.CYAN);
                    if ((System.currentTimeMillis() / 500) % 2 == 0) {
                        int arrowX = getWidth() - 30;
                         if (currentSpeakerImage != null) {
                            arrowX -= (IMAGE_SIZE + IMAGE_PADDING);
                        }
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
        
        // Calcoliamo la larghezza disponibile.
        // Poiché usiamo sempre un'immagine (NPC o Default), sottraiamo sempre lo spazio
        int maxWidth = getWidth() - 60; 
        if (currentSpeakerImage != null) {
            maxWidth -= (IMAGE_SIZE + IMAGE_PADDING);
        }

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
    
    // Getters
    public boolean isDialogActive() { return dialogActive; }
    public boolean isChoiceMode() { return choiceMode; }
    public boolean isTextFullyRevealed() { return textFullyRevealed; }
    public int getSelectedOption() { return selectedOption; }
    public List<String> getResponseOptions() { return responseOptions; }
}