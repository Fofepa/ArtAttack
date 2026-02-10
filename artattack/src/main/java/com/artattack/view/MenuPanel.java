package com.artattack.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MenuPanel {
    private JPanel menuPanel;
    private MainGUIFacade mainFacade;
    private JPanel contentContainer; 
    
    private static final Dimension BUTTON_SIZE = new Dimension(380, 50);

    public MenuPanel(MainGUIFacade mainFacade) {
        this.mainFacade = mainFacade;
        initializeMenuPanel();
    }
    
    private void initializeMenuPanel() {
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(Color.BLACK);
        
        contentContainer = new JPanel();
        contentContainer.setLayout(new BoxLayout(contentContainer, BoxLayout.Y_AXIS));
        contentContainer.setBackground(Color.BLACK);
        
        LogoPanel logoPanel = new LogoPanel();
        logoPanel.setPreferredSize(new Dimension(800, 300)); 
        logoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        
        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(logoPanel);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10))); 
        menuPanel.add(contentContainer);
        menuPanel.add(Box.createVerticalGlue());
        
        showMainMenu();
    }
    
    private void showMainMenu() {
        contentContainer.removeAll();
        
        JButton newGameBtn = createMenuButton("New Game");
        JButton loadGameBtn = createMenuButton("Load Game");
        JButton settingsBtn = createMenuButton("Settings");
        JButton exitBtn = createMenuButton("Exit");
        
        contentContainer.add(newGameBtn);
        contentContainer.add(Box.createRigidArea(new Dimension(0, 20)));
        contentContainer.add(loadGameBtn);
        contentContainer.add(Box.createRigidArea(new Dimension(0, 20)));
        contentContainer.add(settingsBtn);
        contentContainer.add(Box.createRigidArea(new Dimension(0, 20)));
        contentContainer.add(exitBtn);
        
        newGameBtn.addActionListener(e -> mainFacade.showCharacterSelection());
        loadGameBtn.addActionListener(e -> mainFacade.loadGame());
        settingsBtn.addActionListener(e -> showSettingsMenu());
        exitBtn.addActionListener(e -> mainFacade.exitGame());
        
        refreshUI();
    }
    
    private void showSettingsMenu() {
        contentContainer.removeAll();
        JLabel titleLabel = new JLabel("SETTINGS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 36));
        titleLabel.setForeground(Color.CYAN);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        GameSettings settings = GameSettings.getInstance();
        JButton speedBtn = createMenuButton("Text Speed: " + settings.getTextSpeed());
        speedBtn.addActionListener(e -> {
            settings.cycleTextSpeed();
            speedBtn.setText("Text Speed: " + settings.getTextSpeed());
        });

        JButton accessBtn = createMenuButton("Accessibility >");
        accessBtn.addActionListener(e -> showAccessibilityMenu());
        
        JButton backBtn = createMenuButton("Back");
        backBtn.addActionListener(e -> showMainMenu());
        
        contentContainer.add(titleLabel);
        contentContainer.add(Box.createRigidArea(new Dimension(0, 30)));
        contentContainer.add(speedBtn);
        contentContainer.add(Box.createRigidArea(new Dimension(0, 20)));
        contentContainer.add(accessBtn); 
        contentContainer.add(Box.createRigidArea(new Dimension(0, 30))); 
        contentContainer.add(backBtn);
        refreshUI();
    }

    private void showAccessibilityMenu() {
        contentContainer.removeAll();
        JLabel titleLabel = new JLabel("ACCESSIBILITY", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 36));
        titleLabel.setForeground(Color.ORANGE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        GameSettings settings = GameSettings.getInstance();
        JButton fontBtn = createMenuButton("Dialog Font: " + settings.getFontSize());
        fontBtn.addActionListener(e -> {
            settings.cycleFontSize();
            fontBtn.setText("Dialog Font: " + settings.getFontSize());
        });

        JButton zoomBtn = createMenuButton("Map Zoom: " + settings.getCurrentZoom());
        zoomBtn.addActionListener(e -> {
            settings.cycleZoomLevel();
            zoomBtn.setText("Map Zoom: " + settings.getCurrentZoom());

            if (mainFacade.getGameFacade() != null) {
                mainFacade.getGameFacade().getGamePanel().repaint();
            }
        });

        JButton backBtn = createMenuButton("Back");
        backBtn.addActionListener(e -> showSettingsMenu());

        contentContainer.add(titleLabel);
        contentContainer.add(Box.createRigidArea(new Dimension(0, 40)));
        contentContainer.add(fontBtn);
        contentContainer.add(Box.createRigidArea(new Dimension(0, 20))); 
        contentContainer.add(zoomBtn);
        contentContainer.add(Box.createRigidArea(new Dimension(0, 40)));
        contentContainer.add(backBtn);
        refreshUI();
    }
    
    private void refreshUI() {
        contentContainer.revalidate();
        contentContainer.repaint();
    }
    
    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Monospaced", Font.BOLD, 18));
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
        
        if (text.equals("Back")) {
             button.setForeground(Color.LIGHT_GRAY);
             button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
        }
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(50, 50, 50));
                if (text.equals("Back")) {
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
                if (text.equals("Back")) {
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
    
    public JPanel getMenuPanel() { return menuPanel; }
}

class LogoPanel extends JPanel {
    private final Image logo;
    public LogoPanel() {
        setOpaque(false); 
        java.net.URL imgURL = getClass().getResource("/images/logo.png");
        if (imgURL != null) logo = new ImageIcon(imgURL).getImage();
        else logo = null;
    }
    @Override
    public Dimension getPreferredSize() { return new Dimension(800, 300); }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (logo == null) return;
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        int panelWidth = getWidth(); int panelHeight = getHeight();
        int imgWidth = logo.getWidth(this); int imgHeight = logo.getHeight(this);
        double widthRatio = (double) panelWidth / imgWidth;
        double heightRatio = (double) panelHeight / imgHeight;
        double scale = Math.min(widthRatio, heightRatio) * 0.7;
        int drawWidth = (int) (imgWidth * scale); int drawHeight = (int) (imgHeight * scale);
        int x = (panelWidth - drawWidth) / 2; int y = (panelHeight - drawHeight) / 2;
        g2.drawImage(logo, x, y, drawWidth, drawHeight, this);
    }
}