package com.artattack.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Pause menu overlay panel with semi-transparent background
 */
public class PausePanel extends JPanel {
    private MainGUIFacade mainFacade;
    private JPanel contentPanel;
    
    public PausePanel(MainGUIFacade mainFacade) {
        this.mainFacade = mainFacade;
        initializePausePanel();
    }
    
    private void initializePausePanel() {
        setLayout(new GridBagLayout());
        setOpaque(false); // Importante per la trasparenza
        setBackground(Color.BLACK);
        
        // Content panel (opaque center box)
        contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(new Color(20, 20, 20, 220)); // Semi-trasparente
        contentPanel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
        contentPanel.setPreferredSize(new Dimension(400, 500));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        JLabel titleLabel = new JLabel("PAUSED", JLabel.CENTER);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 36));
        titleLabel.setForeground(Color.GREEN);
        gbc.gridy = 0;
        gbc.insets = new Insets(30, 20, 30, 20);
        contentPanel.add(titleLabel, gbc);
        
        // Reset insets for buttons
        gbc.insets = new Insets(10, 20, 10, 20);
        
        // Continue button
        JButton continueBtn = createPauseButton("Continue");
        gbc.gridy = 1;
        contentPanel.add(continueBtn, gbc);
        
        // Load button
        JButton loadBtn = createPauseButton("Load Game");
        gbc.gridy = 2;
        contentPanel.add(loadBtn, gbc);
        
        // Settings button
        JButton settingsBtn = createPauseButton("Settings");
        gbc.gridy = 3;
        contentPanel.add(settingsBtn, gbc);
        
        // Save button
        JButton saveBtn = createPauseButton("Save Game");
        gbc.gridy = 4;
        contentPanel.add(saveBtn, gbc);
        
        // Exit button
        JButton exitBtn = createPauseButton("Exit to Menu");
        gbc.gridy = 5;
        contentPanel.add(exitBtn, gbc);
        
        // Add hint label at bottom
        JLabel hintLabel = new JLabel("Press ESC to resume", JLabel.CENTER);
        hintLabel.setFont(new Font("Monospaced", Font.ITALIC, 12));
        hintLabel.setForeground(Color.LIGHT_GRAY);
        gbc.gridy = 6;
        gbc.insets = new Insets(30, 20, 20, 20);
        contentPanel.add(hintLabel, gbc);
        
        // Add content panel to center of pause panel
        add(contentPanel);
        
        // Button actions (placeholder for now)
        continueBtn.addActionListener(e -> {
            System.out.println("Continue game");
            hidePauseMenu();
        });
        
        loadBtn.addActionListener(e -> {
            System.out.println("Load game - not implemented yet");
        });
        
        settingsBtn.addActionListener(e -> {
            System.out.println("Settings - not implemented yet");
        });
        
        saveBtn.addActionListener(e -> {
            System.out.println("Save game - not implemented yet");
        });
        
        exitBtn.addActionListener(e -> {
            System.out.println("Exit to menu");
            mainFacade.showMenu();
        });
    }
    
    private JButton createPauseButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(300, 50));
        button.setFont(new Font("Monospaced", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(30, 30, 30));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(50, 50, 50));
                button.setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(30, 30, 30));
                button.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
            }
        });
        
        return button;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(0, 0, 0, 150)); // NERO SEMI-TRASPARENTE
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }
    
    /**
     * Call this method to hide the pause menu and resume the game
     */
    public void hidePauseMenu() {
        setVisible(false);
        if (mainFacade.getGameFacade() != null) {
            mainFacade.getGameFacade().getGamePanel().requestFocusInWindow();
        }
    }
    
    /**
     * Call this method to show the pause menu
     */
    public void showPauseMenu() {
        setVisible(true);
        requestFocusInWindow();
    }
}