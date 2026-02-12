package com.artattack.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class DeathPanel extends JPanel {

    private MainGUIFacade mainFacade;
    private boolean hasSaveFile;

    public DeathPanel(MainGUIFacade mainFacade, String timeElapsed) {
        this.mainFacade = mainFacade;
        File saveFile = mainFacade.getGameFacade().getMainFrame().getGameContext().getSaveManager().getSavePath().toFile();
        this.hasSaveFile = saveFile.exists();

        initializeUI(timeElapsed);
        setupInput();
    }

    private void initializeUI(String timeString) {
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK); 
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 20, 0); 
        gbc.anchor = GridBagConstraints.CENTER;

        // Titolo
        JLabel titleLabel = new JLabel("GAME OVER");
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 70));
        titleLabel.setForeground(Color.RED);
        add(titleLabel, gbc);

        // Tempo
        gbc.gridy++;
        JLabel timeLabel = new JLabel("Survival Time: " + timeString);
        timeLabel.setFont(new Font("Monospaced", Font.PLAIN, 24));
        timeLabel.setForeground(Color.WHITE);
        add(timeLabel, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(60, 0, 10, 0); 
        
        JLabel continueLabel = new JLabel("[C] CONTINUE (Load Game)");
        continueLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        if (hasSaveFile) {
            continueLabel.setForeground(Color.GREEN);
        } else {
            continueLabel.setForeground(Color.DARK_GRAY);
            continueLabel.setText("[C] CONTINUE (No Save Found)");
        }
        add(continueLabel, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(10, 0, 10, 0); 
        JLabel restartLabel = new JLabel("[R] RESTART (New Game)");
        restartLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        restartLabel.setForeground(Color.CYAN);
        add(restartLabel, gbc);

        gbc.gridy++;
        JLabel exitLabel = new JLabel("[ESC] EXIT GAME");
        exitLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        exitLabel.setForeground(Color.LIGHT_GRAY);
        add(exitLabel, gbc);
    }

    private void setupInput() {
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_C -> {
                        if (hasSaveFile) {
                            mainFacade.loadGame();
                        }
                    }
                    case KeyEvent.VK_R -> mainFacade.showCharacterSelection();
                    case KeyEvent.VK_ESCAPE -> mainFacade.exitGame();
                }
            }
        });
    }
}