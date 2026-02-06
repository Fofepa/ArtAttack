package com.artattack.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SettingsPanel extends JPanel {
    private MainGUIFacade mainFacade;
    private JButton speedBtn;

    public SettingsPanel(MainGUIFacade mainFacade) {
        this.mainFacade = mainFacade;
        initializePanel();
    }

    private void initializePanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.BLACK);

        // Title
        JLabel title = new JLabel("SETTINGS");
        title.setFont(new Font("Monospaced", Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Speed Button
        speedBtn = createButton("Text Speed: " + GameSettings.getInstance().getTextSpeed());
        speedBtn.addActionListener(e -> {
            GameSettings.getInstance().cycleTextSpeed();
            updateButtonText();
        });

        // Back Button
        JButton backBtn = createButton("Back to Menu");
        backBtn.addActionListener(e -> mainFacade.showMenu());

        // Layout
        add(Box.createVerticalGlue());
        add(title);
        add(Box.createRigidArea(new Dimension(0, 50)));
        add(speedBtn);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(backBtn);
        add(Box.createVerticalGlue());
    }

    private void updateButtonText() {
        speedBtn.setText("Text Speed: " + GameSettings.getInstance().getTextSpeed());
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(300, 50));
        button.setFont(new Font("Courier New", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(30, 30, 30));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(60, 60, 60));
            }
            @Override
            public void mouseExited(MouseEvent evt) {
                button.setBackground(new Color(30, 30, 30));
            }
        });

        return button;
    }
}