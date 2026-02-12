package com.artattack.view;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class GameOverPanel extends JPanel {
    private MainGUIFacade mainFacade;

    public GameOverPanel(MainGUIFacade mainFacade, String finalTime) {
        this.mainFacade = mainFacade;
        initializeUI(finalTime);
        setupInput();
    }

    private void initializeUI(String timeString) {
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK); 
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        JLabel victoryLabel = new JLabel("VICTORY - MISSION ACCOMPLISHED");
        victoryLabel.setFont(new Font("Monospaced", Font.BOLD, 60));
        victoryLabel.setForeground(Color.YELLOW);
        add(victoryLabel, gbc);

        gbc.gridy++;
        JLabel infoLabel = new JLabel("You have defeated the final boss and reclaimed the peace.");
        infoLabel.setFont(new Font("Monospaced", Font.PLAIN, 24));
        infoLabel.setForeground(Color.WHITE);
        add(infoLabel, gbc);

        gbc.gridy++;
        JLabel timeLabel = new JLabel("Final Completion Time: " + timeString);
        timeLabel.setFont(new Font("Monospaced", Font.BOLD, 30));
        timeLabel.setForeground(Color.GREEN);
        add(timeLabel, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(100, 0, 0, 0);
        JLabel restartLabel = new JLabel("Press ESC to return to Menu or ENTER to Exit");
        restartLabel.setFont(new Font("Monospaced", Font.ITALIC, 20));
        restartLabel.setForeground(Color.LIGHT_GRAY);
        add(restartLabel, gbc);
    }

    private void setupInput() {
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    mainFacade.exitGame();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    mainFacade.showMenu();
                }
            }
        });
    }
}