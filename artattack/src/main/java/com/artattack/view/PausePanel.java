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
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class PausePanel extends JPanel {
    private MainGUIFacade mainFacade;
    private JPanel contentPanel;
    
    private CardLayout cardLayout;
    private static final String CARD_MAIN = "MAIN";
    private static final String CARD_SETTINGS = "SETTINGS";
    private static final String CARD_ACCESS = "ACCESS";
    private static final String CARD_NO_SAVE = "NO_SAVE";
    
    private static final Dimension PANEL_SIZE = new Dimension(400, 500);
    private static final Dimension BUTTON_SIZE = new Dimension(300, 45); 

    private List<JButton> currentButtons = new ArrayList<>();
    private int selectedButtonIndex = 0;
    
    // Store button lists for each card
    private final List<JButton> mainButtons = new ArrayList<>();
    private final List<JButton> settingsButtons = new ArrayList<>();
    private final List<JButton> accessButtons = new ArrayList<>();
    private final List<JButton> noSaveButtons = new ArrayList<>();
    
    public PausePanel(MainGUIFacade mainFacade) {
        this.mainFacade = mainFacade;
        
        setDoubleBuffered(true);
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0)); 
        
        initializeBaseLayout();
        initializeCards();
        setupKeyboardNavigation(); 
        showCard(CARD_MAIN);
    }


    private void setupKeyboardNavigation() {
        InputMap im = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        im.put(KeyStroke.getKeyStroke("UP"), "moveUp");
        im.put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
        im.put(KeyStroke.getKeyStroke("ENTER"), "select");
        im.put(KeyStroke.getKeyStroke("SPACE"), "select");

        am.put("moveUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeSelection(-1);
            }
        });

        am.put("moveDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeSelection(1);
            }
        });

        am.put("select", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isVisible() && !currentButtons.isEmpty()) {
                    currentButtons.get(selectedButtonIndex).doClick();
                }
            }
        });
    }

    private void changeSelection(int direction) {
        if (currentButtons.isEmpty()) return;
        
        updateButtonVisuals(currentButtons.get(selectedButtonIndex), false);
        
        selectedButtonIndex += direction;
        if (selectedButtonIndex < 0) selectedButtonIndex = currentButtons.size() - 1;
        else if (selectedButtonIndex >= currentButtons.size()) selectedButtonIndex = 0;
        
        updateButtonVisuals(currentButtons.get(selectedButtonIndex), true);
    }

    private void updateButtonVisuals(JButton button, boolean isSelected) {
        String text = button.getText();
        if (isSelected) {
            button.setBackground(new Color(50, 50, 50));
            if (text.contains("Back")) {
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
        } else {
            button.setBackground(new Color(30, 30, 30));
            if (text.contains("Back")) {
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
    }
    
    private void initializeBaseLayout() {
        setLayout(new GridBagLayout());
        
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(new Color(20, 20, 20, 230));
                g2.fillRect(0, 0, getWidth(), getHeight());
                
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
        contentPanel.add(createNoSavePanel(), CARD_NO_SAVE);
    }
    
    private void showCard(String cardName) {
        if (!currentButtons.isEmpty()) {
            updateButtonVisuals(currentButtons.get(selectedButtonIndex), false);
        }

        cardLayout.show(contentPanel, cardName);
        
        switch (cardName) {
            case CARD_MAIN -> currentButtons = mainButtons;
            case CARD_SETTINGS -> currentButtons = settingsButtons;
            case CARD_ACCESS -> currentButtons = accessButtons;
            case CARD_NO_SAVE -> currentButtons = noSaveButtons;
        }
        
        selectedButtonIndex = 0;
        if (!currentButtons.isEmpty()) {
            updateButtonVisuals(currentButtons.get(0), true);
        }
        
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
        JButton exitBtn = createPauseButton("Exit to Menu");

        mainButtons.add(continueBtn);
        mainButtons.add(loadBtn);
        mainButtons.add(settingsBtn);
        mainButtons.add(exitBtn);
        
        gbc.gridy = 1; panel.add(continueBtn, gbc);
        gbc.gridy = 2; panel.add(loadBtn, gbc);
        gbc.gridy = 3; panel.add(settingsBtn, gbc);
        gbc.gridy = 5; panel.add(exitBtn, gbc);
        
        JLabel hintLabel = new JLabel("Press ESC to resume", JLabel.CENTER);
        hintLabel.setFont(new Font("Monospaced", Font.ITALIC, 12));
        hintLabel.setForeground(Color.GRAY);
        
        gbc.gridy = 6;
        gbc.insets = new Insets(20, 20, 10, 20);
        panel.add(hintLabel, gbc);
        
        continueBtn.addActionListener(e -> mainFacade.hidePauseMenu());
        
        loadBtn.addActionListener(e -> {
            if (doesSaveFileExist()) {
                mainFacade.loadGame();
                mainFacade.hidePauseMenu();
            } else {
                showCard(CARD_NO_SAVE);
            }
        });

        settingsBtn.addActionListener(e -> showCard(CARD_SETTINGS));
        exitBtn.addActionListener(e -> mainFacade.showMenu());
        
        return panel;
    }


    private boolean doesSaveFileExist() {
        String gameName = "ArtAttack";
        String os = System.getProperty("os.name").toLowerCase();
        String home = System.getProperty("user.home");
        Path folder;

        if (os.contains("win")) {
            String appData = System.getenv("LOCALAPPDATA");
            if (appData == null) {
                folder = Paths.get(home, "AppData", "Local", gameName);
            } else {
                folder = Paths.get(appData, gameName);
            }
        } else if (os.contains("mac")) {
            folder = Paths.get(home, "Library", "Application Support", gameName);
        } else {
            // Linux/Unix
            folder = Paths.get(home, ".local", "share", gameName);
        }

        File saveFile = folder.resolve("save.json").toFile();
        return saveFile.exists() && !saveFile.isDirectory();
    }
    
    private JPanel createNoSavePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titleLabel = new JLabel("NO SAVE FOUND", JLabel.CENTER);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 32));
        titleLabel.setForeground(Color.RED); 
        
        gbc.gridy = 0; gbc.insets = new Insets(30, 20, 30, 20);
        panel.add(titleLabel, gbc);
        
        JButton backBtn = createPauseButton("Back");
        noSaveButtons.add(backBtn);
        backBtn.addActionListener(e -> showCard(CARD_MAIN));
        
        gbc.gridy = 2; gbc.insets = new Insets(20, 20, 20, 20);
        panel.add(backBtn, gbc);
        
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
        
        gbc.gridy = 0; gbc.insets = new Insets(20, 20, 30, 20);
        panel.add(titleLabel, gbc);
        
        GameSettings settings = GameSettings.getInstance();
        JButton speedBtn = createPauseButton("Text Speed: " + settings.getTextSpeed());
        JButton accessBtn = createPauseButton("Accessibility >");
        JButton backBtn = createPauseButton("Back");
        
        settingsButtons.add(speedBtn);
        settingsButtons.add(accessBtn);
        settingsButtons.add(backBtn);

        speedBtn.addActionListener(e -> {
            settings.cycleTextSpeed();
            speedBtn.setText("Text Speed: " + settings.getTextSpeed());
        });
        accessBtn.addActionListener(e -> showCard(CARD_ACCESS));
        backBtn.addActionListener(e -> showCard(CARD_MAIN));
        
        gbc.insets = new Insets(8, 20, 8, 20);
        gbc.gridy = 1; panel.add(speedBtn, gbc);
        gbc.gridy = 2; panel.add(accessBtn, gbc); 
        gbc.gridy = 4; gbc.insets = new Insets(30, 20, 20, 20);
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
        
        gbc.gridy = 0; gbc.insets = new Insets(20, 20, 30, 20);
        panel.add(titleLabel, gbc);
        
        GameSettings settings = GameSettings.getInstance();
        JButton fontBtn = createPauseButton("Dialog Font: " + settings.getFontSize());
        JButton zoomBtn = createPauseButton("Map Zoom: " + settings.getCurrentZoom());
        JButton backBtn = createPauseButton("Back");

        accessButtons.add(fontBtn);
        accessButtons.add(zoomBtn);
        accessButtons.add(backBtn);

        fontBtn.addActionListener(e -> {
            settings.cycleFontSize();
            fontBtn.setText("Dialog Font: " + settings.getFontSize());
        });
        zoomBtn.addActionListener(e -> {
            settings.cycleZoomLevel();
            zoomBtn.setText("Map Zoom: " + settings.getCurrentZoom());
            if (mainFacade.getGameFacade() != null) mainFacade.getGameFacade().getGamePanel().repaint();
        });
        backBtn.addActionListener(e -> showCard(CARD_SETTINGS));
        
        gbc.insets = new Insets(8, 20, 8, 20);
        gbc.gridy = 1; panel.add(fontBtn, gbc);
        gbc.gridy = 2; panel.add(zoomBtn, gbc);
        gbc.gridy = 4; gbc.insets = new Insets(30, 20, 20, 20);
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
        button.setFocusPainted(false);
        button.setFocusable(false); // We handle focus via Key Bindings
        
        button.setMinimumSize(BUTTON_SIZE);
        button.setMaximumSize(BUTTON_SIZE);
        button.setPreferredSize(BUTTON_SIZE);
        
        updateButtonVisuals(button, false);
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                int idx = currentButtons.indexOf(button);
                if (idx != -1) {
                    updateButtonVisuals(currentButtons.get(selectedButtonIndex), false);
                    selectedButtonIndex = idx;
                    updateButtonVisuals(button, true);
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
        showCard(CARD_MAIN);
    }
}