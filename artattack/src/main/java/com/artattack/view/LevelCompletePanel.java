package com.artattack.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.artattack.level.Maps;

public class LevelCompletePanel extends JPanel {

    private MainGUIFacade mainFacade;
    private Maps nextMap;

    public LevelCompletePanel(MainGUIFacade mainFacade, Maps nextMap, String timeElapsed) {
        this.mainFacade = mainFacade;
        this.nextMap = nextMap;
        
        initializeUI(timeElapsed);
        setupInput();
    }

    private void initializeUI(String timeString) {
        setLayout(new GridBagLayout());
        setBackground(new Color(0, 0, 0, 230)); // Sfondo nero quasi opaco
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 0, 40, 0); // Spaziatura verticale
        gbc.anchor = GridBagConstraints.CENTER;

        // Titolo
        JLabel titleLabel = new JLabel("LEVEL COMPLETE");
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 50));
        titleLabel.setForeground(Color.GREEN);
        add(titleLabel, gbc);

        // Tempo Impiegato
        gbc.gridy++;
        JLabel timeLabel = new JLabel("Time: " + timeString);
        timeLabel.setFont(new Font("Monospaced", Font.PLAIN, 32));
        timeLabel.setForeground(Color.WHITE);
        add(timeLabel, gbc);

        // Istruzioni
        gbc.gridy++;
        gbc.insets = new Insets(80, 0, 20, 0); // Spazio extra prima del tasto
        JLabel continueLabel = new JLabel("Press ENTER to continue...");
        continueLabel.setFont(new Font("Monospaced", Font.ITALIC, 20));
        continueLabel.setForeground(Color.CYAN);
        add(continueLabel, gbc);
    }

    private void setupInput() {
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Chiude il pannello e cambia mappa
                    mainFacade.finalizeMapSwitch(nextMap);
                }
            }
        });
    }
}