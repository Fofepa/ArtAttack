package com.artattack.view;

import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.*;

/**
 * InteractionPanel - Displays dialog and interaction text
 */
public class InteractionPanel extends JPanel {
    private List<String> currentDialog;
    private int currentPhraseIndex = 0;
    private boolean dialogActive = false;
    private boolean choiceMode = false;
    private boolean textFullyRevealed = true;
    private int selectedOption = 0;
    private List<String> responseOptions;
    private Consumer<Integer> choiceCallback;
    
    public InteractionPanel() {
        setBackground(Color.BLACK);
        setFocusable(true);
    }
    
    /**
     * Shows a simple dialog with multiple phrases
     */
    public void showDialog(List<String> messages) {
        this.currentDialog = messages;
        this.currentPhraseIndex = 0;
        this.dialogActive = true;
        this.choiceMode = false;
        this.textFullyRevealed = true;
        setVisible(true);
        repaint();
    }
    
    /**
     * Activates the interaction panel and requests focus
     * Called by InteractableElement when interaction starts
     */
    public void activateAndFocus() {
        // Make sure the component is displayable and visible before requesting focus
        this.setVisible(true);
        this.setEnabled(true);
        
        // Requesting focus is essential for the KeyListener to work here
        this.requestFocusInWindow();
        
        // Mark as active so the paintComponent method knows it should draw the dialog box
        this.dialogActive = true; 
        
        this.revalidate();
        this.repaint();
        
        System.out.println("InteractionPanel: activateAndFocus() called");
    }
    
    /**
     * Deactivates the interaction panel
     */
    public void deactivate() {
        setVisible(false);
        dialogActive = false;
        choiceMode = false;
        repaint();
    }
    
    /**
     * Shows a dialog with a question and multiple choice options
     * @param question The question text
     * @param options List of response options
     * @param callback Function to call when choice is confirmed
     */
    public void showDialogWithChoice(String question, List<String> options, Consumer<Integer> callback) {
        this.currentDialog = List.of(question);
        this.currentPhraseIndex = 0;
        this.dialogActive = true;
        this.choiceMode = true;
        this.responseOptions = options;
        this.selectedOption = 0;
        this.choiceCallback = callback;
        this.textFullyRevealed = true;
        repaint();
    }
    
    /**
     * Moves selection up in choice mode
     */
    public void selectUp() {
        if (choiceMode && selectedOption > 0) {
            selectedOption--;
            repaint();
        }
    }
    
    /**
     * Moves selection down in choice mode
     */
    public void selectDown() {
        if (choiceMode && responseOptions != null && selectedOption < responseOptions.size() - 1) {
            selectedOption++;
            repaint();
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (!dialogActive || currentDialog == null || currentDialog.isEmpty()) {
            return;
        }
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        String text = currentDialog.get(currentPhraseIndex);
        int y = 30;
        
        // Word wrap the text
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        
        for (String word : words) {
            if (g.getFontMetrics().stringWidth(line + word + " ") > getWidth() - 20) {
                g.drawString(line.toString(), 10, y);
                y += 15;
                line = new StringBuilder(word + " ");
            } else {
                line.append(word).append(" ");
            }
        }
        
        if (line.length() > 0) {
            g.drawString(line.toString(), 10, y);
        }
        
        // Display "Press ENTER to continue" or options
        if (textFullyRevealed) {
            y += 30;
            if (choiceMode && responseOptions != null) {
                for (int i = 0; i < responseOptions.size(); i++) {
                    if (i == selectedOption) {
                        g.setColor(Color.CYAN);
                        g.drawString("> ", 10, y);
                    } else {
                        g.setColor(Color.GRAY);
                    }
                    g.drawString(responseOptions.get(i), 25, y);
                    y += 20;
                }
            } else {
                g.setColor(Color.GRAY);
                g.drawString("[Press ENTER to continue]", 10, y);
            }
        }
    }
    
    public void advanceDialog() {
        if (currentPhraseIndex < currentDialog.size() - 1) {
            currentPhraseIndex++;
            repaint();
        } else {
            // Dialog is finished
            dialogActive = false;
            setVisible(false); // Hide the panel
            
            // If it's inside a container in the Facade, hide the container too
            if (getParent() != null) {
                getParent().setVisible(false);
            }
            
            repaint();
        }
    }
    
    public void skipTextAnimation() {
        textFullyRevealed = true;
        repaint();
    }
    
    public void confirmChoice() {
        if (choiceMode && choiceCallback != null) {
            // Execute the callback with the selected option
            choiceCallback.accept(selectedOption);
        }
        dialogActive = false;
        choiceMode = false;
        repaint();
    }
    
    public boolean isDialogActive() { return dialogActive; }
    public boolean isChoiceMode() { return choiceMode; }
    public boolean isTextFullyRevealed() { return textFullyRevealed; }
    public int getSelectedOption() { return selectedOption; }
    public List<String> getResponseOptions() { return responseOptions; }
}