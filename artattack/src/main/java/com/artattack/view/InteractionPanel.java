package com.artattack.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import com.artattack.mapelements.Player;

public class InteractionPanel extends JPanel {
    private Player currentPlayer;
    //private InteractableElement currentInteractableElement;
    
    // Dialog state
    private List<String> currentDialog;
    private int currentPhraseIndex;
    private boolean dialogActive;
    
    // For choice dialogs
    private List<String> responseOptions;
    private int selectedOption;
    private boolean choiceMode;
    private DialogCallback callback;
    
    // Text animation
    private String fullText;
    private int revealedCharacters;
    private Timer textTimer;
    private boolean textFullyRevealed;
    private static final int CHAR_DELAY = 30; // milliseconds per character
    
    // Rendering variables
    private static final int PADDING = 20;
    private static final int LINE_HEIGHT = 25;
    

    private Runnable onDialogFinished;

    
    public InteractionPanel() {
        setBackground(Color.BLACK);
        setFocusable(true);
        dialogActive = false;
        choiceMode = false;
        
        addKeyboardInputs();
        addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                setBorder(null);
            }
        });
    }
    
    private void addKeyboardInputs() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!dialogActive) return;
                
                if (choiceMode) {
                    switch (e.getKeyCode()) {
                    
                        case KeyEvent.VK_UP:
                        case KeyEvent.VK_W:
                            if (textFullyRevealed && selectedOption > 0) {
                                selectedOption--;
                                repaint();
                            }
                            break;
                        
                        case KeyEvent.VK_DOWN:
                        case KeyEvent.VK_S:
                            if (textFullyRevealed && selectedOption < responseOptions.size() - 1) {
                                selectedOption++;
                                repaint();
                            }
                            break;
                        
                        case KeyEvent.VK_ENTER:
                        case KeyEvent.VK_SPACE:
                            if (!textFullyRevealed) {
                                skipTextAnimation();   
                            } else {
                                confirmChoice();       
                            }
                            break;
                    }
                }
                else {
                    // Simple dialog mode
                    if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) {
                        if (!textFullyRevealed) {
                            // Skip animation - reveal all text immediately
                            skipTextAnimation();
                        } else {
                            // Text is fully revealed - advance to next phrase
                            advanceDialog();
                        }
                    }
                }
            }
        });
    }
    
    /**
     * Start a simple dialog with a list of phrases
     */
    public void showDialog(List<String> phrases) {
        
        this.currentDialog = phrases;
        this.currentPhraseIndex = 0;
        this.dialogActive = true;
        this.choiceMode = false;
        startTextAnimation(phrases.get(0));
    }
    
    
    public void showDialogWithChoice(String question, List<String> options, DialogCallback callback) {
        this.currentDialog = List.of(question);
        this.responseOptions = options;
        this.selectedOption = 0;
        this.dialogActive = true;
        this.choiceMode = true;
        this.callback = callback;
        startTextAnimation(question);
    }
    
    private void startTextAnimation(String text) {
        fullText = text;
        revealedCharacters = 0;
        textFullyRevealed = false;
        
        // Stop any existing timer
        if (textTimer != null && textTimer.isRunning()) {
            textTimer.stop();
        }
        
        // Start new animation timer
        textTimer = new Timer(CHAR_DELAY, e -> {
            if (revealedCharacters < fullText.length()) {
                revealedCharacters++;
                repaint();
            } else {
                textFullyRevealed = true;
                textTimer.stop();
                repaint();
            }
        });
        textTimer.start();
    }
    
    public void skipTextAnimation() {
        if (textTimer != null && textTimer.isRunning()) {
            textTimer.stop();
        }
        revealedCharacters = fullText.length();
        textFullyRevealed = true;
        repaint();
    }
    
    public void advanceDialog() {
        currentPhraseIndex++;
        if (currentPhraseIndex >= currentDialog.size()) {
            // End of dialog
            closeDialog();
        } else {
            // Start animation for next phrase
            startTextAnimation(currentDialog.get(currentPhraseIndex));
        }
    }
    
    public void confirmChoice() {
        if (callback != null) {
            callback.onChoiceMade(selectedOption);
        }
        closeDialog();
    }
    
    public void closeDialog() {
        dialogActive = false;
        choiceMode = false;
        currentDialog = null;
        responseOptions = null;
        callback = null;

        if (textTimer != null && textTimer.isRunning()) {
            textTimer.stop();
        }

        repaint();

        if (onDialogFinished != null) {
            SwingUtilities.invokeLater(onDialogFinished);
        }
    }

    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (!dialogActive) {
            // No active dialog - empty panel
            return;
        }
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        Font font = new Font("Monospaced", Font.PLAIN, 16);
        g2d.setFont(font);
        
        int y = PADDING + 20;
        
        if (choiceMode) {
            // Draw the question (with animation)
            g2d.setColor(Color.WHITE);
            String displayText = fullText.substring(0, Math.min(revealedCharacters, fullText.length()));
            drawWrappedString(g2d, displayText, PADDING, y, getWidth() - PADDING * 2);
            y += LINE_HEIGHT * 2;
            
            // Only show options when text is fully revealed
            if (textFullyRevealed) {
                // Draw the options
                for (int i = 0; i < responseOptions.size(); i++) {
                    if (i == selectedOption) {
                        // Selected option - highlighted
                        g2d.setColor(Color.YELLOW);
                        g2d.drawString("> " + responseOptions.get(i), PADDING, y);
                    } else {
                        // Normal option
                        g2d.setColor(Color.GRAY);
                        g2d.drawString("  " + responseOptions.get(i), PADDING, y);
                    }
                    y += LINE_HEIGHT;
                }
                
                // Instructions
                g2d.setColor(new Color(100, 100, 100));
                g2d.setFont(new Font("Monospaced", Font.ITALIC, 12));
                g2d.drawString("W/S to choose, ENTER to confirm", PADDING, getHeight() - PADDING);
            } else {
                // Show "skip" instruction while animating
                g2d.setColor(new Color(100, 100, 100));
                g2d.setFont(new Font("Monospaced", Font.ITALIC, 12));
                g2d.drawString("SPACE/ENTER to skip...", PADDING, getHeight() - PADDING);
            }
            
        } else {
            // Simple dialog - show current phrase with animation
            g2d.setColor(Color.WHITE);
            
            // Display only revealed characters
            String displayText = fullText.substring(0, Math.min(revealedCharacters, fullText.length()));
            drawWrappedString(g2d, displayText, PADDING, y, getWidth() - PADDING * 2);
            
            // "Press Enter" indicator
            g2d.setColor(new Color(100, 100, 100));
            g2d.setFont(new Font("Monospaced", Font.ITALIC, 12));
            
            String indicator;
            if (!textFullyRevealed) {
                indicator = "SPACE/ENTER to skip...";
            } else if (currentPhraseIndex < currentDialog.size() - 1) {
                indicator = "ENTER to continue...";
            } else {
                indicator = "ENTER to close";
            }
            g2d.drawString(indicator, PADDING, getHeight() - PADDING);
        }
    }
    
    // Helper method for multi-line text
    private int drawWrappedString(Graphics2D g, String text, int x, int y, int maxWidth) {
        FontMetrics fm = g.getFontMetrics();
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        int linesDrawn = 0;
        
        for (String word : words) {
            String testLine = line.length() == 0 ? word : line + " " + word;
            int width = fm.stringWidth(testLine);
            
            if (width > maxWidth && line.length() > 0) {
                g.drawString(line.toString(), x, y);
                y += LINE_HEIGHT;
                linesDrawn++;
                line = new StringBuilder(word);
            } else {
                line = new StringBuilder(testLine);
            }
        }
        if (line.length() > 0) {
            g.drawString(line.toString(), x, y);
            linesDrawn++;
        }
        
        return linesDrawn;
    }

    public void giveFocus(){
        setFocusable(true);
        requestFocusInWindow();
    }
    
    public boolean isDialogActive() {
        return dialogActive;
    }
    
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    
    /* public InteractableElement getCurrentInteractableElement() {
        return currentInteractableElement;
    } */
    
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
    
    /* public void setCurrentInteractableElement(InteractableElement currentInteractableElement) {
        this.currentInteractableElement = currentInteractableElement;
    } */
    
    // Callback interface
    @FunctionalInterface
    public interface DialogCallback {
        void onChoiceMade(int chosenIndex);
    }


    public void setOnDialogFinished(Runnable r) {
        this.onDialogFinished = r;
    }

    public void activateAndFocus() {
        requestFocusInWindow();
    }

    public List<String> getCurrentDialog(){
        return this.currentDialog;
    }

    public int getCurrentPhraseIndex(){
        return this.currentPhraseIndex;
    }

    public boolean getDialogActive(){
        return this.dialogActive;
    }

    public int getSelectedOption(){
        return this.selectedOption;
    }

    public List<String> getResponseOptions(){
        return this.responseOptions;
    }

    public boolean getChoiceMode(){
        return this.getChoiceMode();
    }

    public DialogCallback getCallback(){
        return this.callback;
    }

    public boolean getTextFullyRevealed(){
        return this.textFullyRevealed;
    }
}