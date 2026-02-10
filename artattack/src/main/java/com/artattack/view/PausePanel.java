package com.artattack.view;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PausePanel extends JPanel {
    private MainGUIFacade mainFacade;
    private JPanel contentPanel;
    
    
    private CardLayout cardLayout;
    private static final String CARD_MAIN = "MAIN";
    private static final String CARD_SETTINGS = "SETTINGS";
    private static final String CARD_ACCESS = "ACCESS";
    
    private static final Dimension PANEL_SIZE = new Dimension(400, 500);
    private static final Dimension BUTTON_SIZE = new Dimension(300, 45); 
    
    public PausePanel(MainGUIFacade mainFacade) {
        this.mainFacade = mainFacade;
        
        setDoubleBuffered(true);
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0)); 
        
        initializeBaseLayout();
        initializeCards(); 
        showCard(CARD_MAIN);
    }
    
    private void initializeBaseLayout() {
        setLayout(new GridBagLayout());
        
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // 1. Background
                g2.setColor(new Color(20, 20, 20, 230));
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                // 2. Border
                g2.setColor(Color.GREEN);
                g2.setStroke(new java.awt.BasicStroke(3));
                g2.drawRect(1, 1, getWidth()-2, getHeight()-2);
                
                g2.dispose();
            }
        };
        
        contentPanel.setOpaque(false);
        contentPanel.setPreferredSize(PANEL_SIZE);
        contentPanel.setMinimumSize(PANEL_SIZE);
        contentPanel.setMaximumSize(PANEL_SIZE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(contentPanel, gbc);
    }

    private void initializeCards() {
        contentPanel.add(createMainPauseMenu(), CARD_MAIN);
        contentPanel.add(createSettingsMenu(), CARD_SETTINGS);
        contentPanel.add(createAccessibilityMenu(), CARD_ACCESS);
    }
    
    private void showCard(String cardName) {
        cardLayout.show(contentPanel, cardName);
        contentPanel.repaint(); 
    }
    

    private JPanel createMainPauseMenu() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false); 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titleLabel = new JLabel("PAUSED", JLabel.CENTER);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 32));
        titleLabel.setForeground(Color.GREEN);
        
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20); 
        panel.add(titleLabel, gbc);
        gbc.insets = new Insets(8, 20, 8, 20); 
        
        JButton continueBtn = createPauseButton("Continue");
        JButton loadBtn = createPauseButton("Load Game");
        JButton settingsBtn = createPauseButton("Settings");
        JButton saveBtn = createPauseButton("Save Game");
        JButton exitBtn = createPauseButton("Exit to Menu");
        
        gbc.gridy = 1; panel.add(continueBtn, gbc);
        gbc.gridy = 2; panel.add(loadBtn, gbc);
        gbc.gridy = 3; panel.add(settingsBtn, gbc);
        gbc.gridy = 4; panel.add(saveBtn, gbc);
        gbc.gridy = 5; panel.add(exitBtn, gbc);
        
        JLabel hintLabel = new JLabel("Press ESC to resume", JLabel.CENTER);
        hintLabel.setFont(new Font("Monospaced", Font.ITALIC, 12));
        hintLabel.setForeground(Color.GRAY);
        
        gbc.gridy = 6;
        gbc.insets = new Insets(20, 20, 10, 20);
        panel.add(hintLabel, gbc);
        
        continueBtn.addActionListener(e -> mainFacade.hidePauseMenu());
        settingsBtn.addActionListener(e -> showCard(CARD_SETTINGS));
        exitBtn.addActionListener(e -> mainFacade.showMenu());
        
        return panel;
    }
    
    private JPanel createSettingsMenu() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titleLabel = new JLabel("SETTINGS", JLabel.CENTER);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 32));
        titleLabel.setForeground(Color.CYAN); 
        
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 30, 20);
        panel.add(titleLabel, gbc);
        gbc.insets = new Insets(8, 20, 8, 20);
        
        GameSettings settings = GameSettings.getInstance();
        JButton speedBtn = createPauseButton("Text Speed: " + settings.getTextSpeed());
        speedBtn.addActionListener(e -> {
            settings.cycleTextSpeed();
            speedBtn.setText("Text Speed: " + settings.getTextSpeed());
        });

        JButton accessBtn = createPauseButton("Accessibility >");
        accessBtn.addActionListener(e -> showCard(CARD_ACCESS));
        
        JButton backBtn = createPauseButton("Back");
        formatBackButton(backBtn);
        backBtn.addActionListener(e -> showCard(CARD_MAIN));
        
        gbc.gridy = 1; panel.add(speedBtn, gbc);
        gbc.gridy = 2; panel.add(accessBtn, gbc); 
        
        gbc.gridy = 3; 
        gbc.weighty = 1.0; 
        panel.add(Box.createGlue(), gbc);
        gbc.weighty = 0;
        
        gbc.gridy = 4; 
        gbc.insets = new Insets(30, 20, 20, 20);
        panel.add(backBtn, gbc);
        
        return panel;
    }

    private JPanel createAccessibilityMenu() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("ACCESSIBILITY", JLabel.CENTER);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 32));
        titleLabel.setForeground(Color.ORANGE); 
        
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 30, 20);
        panel.add(titleLabel, gbc);
        gbc.insets = new Insets(8, 20, 8, 20);
        
        GameSettings settings = GameSettings.getInstance();
        JButton fontBtn = createPauseButton("Dialog Font: " + settings.getFontSize());
        fontBtn.addActionListener(e -> {
            settings.cycleFontSize();
            fontBtn.setText("Dialog Font: " + settings.getFontSize());
        });

        JButton zoomBtn = createPauseButton("Map Zoom: " + settings.getCurrentZoom());
        zoomBtn.addActionListener(e -> {
            settings.cycleZoomLevel();
            zoomBtn.setText("Map Zoom: " + settings.getCurrentZoom());
            if (mainFacade.getGameFacade() != null) {
                mainFacade.getGameFacade().getGamePanel().repaint();
            }
        });
        
        JButton backBtn = createPauseButton("Back");
        formatBackButton(backBtn);
        backBtn.addActionListener(e -> showCard(CARD_SETTINGS));
        
        gbc.gridy = 1; panel.add(fontBtn, gbc);
        gbc.gridy = 2; panel.add(zoomBtn, gbc);
        
        gbc.gridy = 3; 
        gbc.weighty = 1.0; 
        panel.add(Box.createGlue(), gbc);
        gbc.weighty = 0;
        
        gbc.gridy = 4; 
        gbc.insets = new Insets(30, 20, 20, 20);
        panel.add(backBtn, gbc);
        
        return panel;
    }
    
    private void formatBackButton(JButton backBtn) {
        backBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        backBtn.setForeground(Color.LIGHT_GRAY);
    }
    
    private JButton createPauseButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Monospaced", Font.BOLD, 14)); 
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(30, 30, 30));
        button.setFocusPainted(false);
        button.setFocusable(false);
        
        // Uso delle nuove dimensioni ridotte
        button.setMinimumSize(BUTTON_SIZE);
        button.setMaximumSize(BUTTON_SIZE);
        button.setPreferredSize(BUTTON_SIZE);
        
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GREEN, 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(50, 50, 50));
                if(text.equals("Back")) {
                     button.setForeground(Color.WHITE);
                     button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.WHITE, 4),
                        BorderFactory.createEmptyBorder(3, 8, 3, 8)
                     ));
                } else {
                    button.setForeground(Color.GREEN);
                    button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.GREEN, 4),
                        BorderFactory.createEmptyBorder(3, 8, 3, 8)
                     ));
                }
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(new Color(30, 30, 30));
                if(text.equals("Back")) {
                    button.setForeground(Color.LIGHT_GRAY);
                    button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.GRAY, 2),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)
                    ));
                } else {
                    button.setForeground(Color.WHITE);
                    button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.GREEN, 2),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)
                    ));
                }
            }
        });
        
        return button;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(0, 0, 0, 150)); 
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }
    
    public void hidePauseMenu() {
        setVisible(false);
        showCard(CARD_MAIN);
    }
    
    public void showPauseMenu() {
        setVisible(true);
        requestFocusInWindow(); 
    }
}