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
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PausePanel extends JPanel {
    private MainGUIFacade mainFacade;
    private JPanel contentPanel;
    private GridBagConstraints gbc;
    
    private static final Dimension BUTTON_SIZE = new Dimension(380, 50);
    
    public PausePanel(MainGUIFacade mainFacade) {
        this.mainFacade = mainFacade;
        initializeBaseLayout();
        showMainPauseMenu(); 
    }
    
    private void initializeBaseLayout() {
        setLayout(new GridBagLayout());
        setOpaque(false); 
        setBackground(Color.BLACK);
        
        contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(new Color(20, 20, 20, 230)); 
        contentPanel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
        contentPanel.setPreferredSize(new Dimension(480, 550)); 
        
        add(contentPanel);
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
    }
    
    private void showMainPauseMenu() {
        contentPanel.removeAll();
        
        JLabel titleLabel = new JLabel("PAUSED", JLabel.CENTER);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 36));
        titleLabel.setForeground(Color.GREEN);
        
        gbc.gridy = 0;
        gbc.insets = new Insets(30, 40, 30, 40);
        contentPanel.add(titleLabel, gbc);
        gbc.insets = new Insets(10, 40, 10, 40);
        
        JButton continueBtn = createPauseButton("Continue");
        JButton loadBtn = createPauseButton("Load Game");
        JButton settingsBtn = createPauseButton("Settings");
        JButton saveBtn = createPauseButton("Save Game");
        JButton exitBtn = createPauseButton("Exit to Menu");
        
        gbc.gridy = 1; contentPanel.add(continueBtn, gbc);
        gbc.gridy = 2; contentPanel.add(loadBtn, gbc);
        gbc.gridy = 3; contentPanel.add(settingsBtn, gbc);
        gbc.gridy = 4; contentPanel.add(saveBtn, gbc);
        gbc.gridy = 5; contentPanel.add(exitBtn, gbc);
        
        JLabel hintLabel = new JLabel("Press ESC to resume", JLabel.CENTER);
        hintLabel.setFont(new Font("Monospaced", Font.ITALIC, 12));
        hintLabel.setForeground(Color.GRAY);
        
        gbc.gridy = 6;
        gbc.insets = new Insets(30, 40, 20, 40);
        contentPanel.add(hintLabel, gbc);
        
        continueBtn.addActionListener(e -> hidePauseMenu());
        settingsBtn.addActionListener(e -> showSettingsMenu());
        exitBtn.addActionListener(e -> mainFacade.showMenu());
        loadBtn.addActionListener(e -> System.out.println("Load not impl. in pause"));
        saveBtn.addActionListener(e -> System.out.println("Save not impl."));
        
        refreshPanel();
    }
    
    private void showSettingsMenu() {
        contentPanel.removeAll();
        
        JLabel titleLabel = new JLabel("SETTINGS", JLabel.CENTER);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 36));
        titleLabel.setForeground(Color.CYAN); 
        
        gbc.gridy = 0;
        gbc.insets = new Insets(30, 40, 40, 40);
        contentPanel.add(titleLabel, gbc);
        gbc.insets = new Insets(10, 40, 10, 40);
        
        GameSettings settings = GameSettings.getInstance();
        JButton speedBtn = createPauseButton("Text Speed: " + settings.getTextSpeed());
        speedBtn.addActionListener(e -> {
            settings.cycleTextSpeed();
            speedBtn.setText("Text Speed: " + settings.getTextSpeed());
        });

        JButton accessBtn = createPauseButton("Accessibility >");
        accessBtn.addActionListener(e -> showAccessibilityMenu());
        
        JButton backBtn = createPauseButton("Back");
        backBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        backBtn.setForeground(Color.LIGHT_GRAY);
        backBtn.addActionListener(e -> showMainPauseMenu());
        
        gbc.gridy = 1; contentPanel.add(speedBtn, gbc);
        gbc.gridy = 2; contentPanel.add(accessBtn, gbc); 
        
        gbc.gridy = 3; 
        gbc.weighty = 1.0; 
        contentPanel.add(javax.swing.Box.createGlue(), gbc);
        gbc.weighty = 0;
        
        gbc.gridy = 4; 
        gbc.insets = new Insets(40, 40, 30, 40);
        contentPanel.add(backBtn, gbc);
        
        refreshPanel();
    }

    private void showAccessibilityMenu() {
        contentPanel.removeAll();
        JLabel titleLabel = new JLabel("ACCESSIBILITY", JLabel.CENTER);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 36));
        titleLabel.setForeground(Color.ORANGE); 
        
        gbc.gridy = 0;
        gbc.insets = new Insets(30, 40, 40, 40);
        contentPanel.add(titleLabel, gbc);
        gbc.insets = new Insets(10, 40, 10, 40);
        
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
        backBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        backBtn.setForeground(Color.LIGHT_GRAY);
        backBtn.addActionListener(e -> showSettingsMenu());

        
        
        gbc.gridy = 1; contentPanel.add(fontBtn, gbc);
        gbc.gridy = 2; contentPanel.add(zoomBtn, gbc);
        
        gbc.gridy = 3; 
        gbc.weighty = 1.0; 
        contentPanel.add(javax.swing.Box.createGlue(), gbc);
        gbc.weighty = 0;
        
        gbc.gridy = 3; 
        gbc.insets = new Insets(40, 40, 30, 40);
        contentPanel.add(backBtn, gbc);
        
        refreshPanel();
    }
    
    private void refreshPanel() {
        contentPanel.revalidate();
        contentPanel.repaint();
        
    }
    
    private JButton createPauseButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Monospaced", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(30, 30, 30));
        button.setFocusPainted(false);
        
        
        button.setFocusable(false);
        
        
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
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(0, 0, 0, 180)); 
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }
    
    public void hidePauseMenu() {
        setVisible(false);
        showMainPauseMenu(); 
        
        // Forza esplicita del focus sul gioco
        if (mainFacade.getGameFacade() != null) {
            mainFacade.getGameFacade().getGamePanel().requestFocusInWindow();
        }
    }
    
    public void showPauseMenu() {
        setVisible(true);
        // Qui il pannello pausa prende focus per i tasti globali, ma i bottoni no
        requestFocusInWindow(); 
    }
}