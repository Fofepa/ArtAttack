package com.artattack.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MenuPanel2 extends JPanel {
    
    private int selectedOption = 0;
    private final String[] menuOptions = {"NUOVA PARTITA", "CONTINUA", "ESCI"};
    private MainFrame mainFrame;
    
    public MenuPanel2(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setBackground(Color.BLACK);
        setFocusable(true);
        
        // Gestione input da tastiera
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        selectedOption = (selectedOption - 1 + menuOptions.length) % menuOptions.length;
                        repaint();
                        break;
                    case KeyEvent.VK_DOWN:
                        selectedOption = (selectedOption + 1) % menuOptions.length;
                        repaint();
                        break;
                    case KeyEvent.VK_ENTER:
                        handleSelection();
                        break;
                }
            }
        });
        
        // Gestione click del mouse
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int option = getOptionAtPosition(e.getY());
                if (option != -1 && option != selectedOption) {
                    selectedOption = option;
                    repaint();
                }
            }
        });
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int option = getOptionAtPosition(e.getY());
                if (option != -1) {
                    selectedOption = option;
                    handleSelection();
                }
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawMenu(g);
    }
    
    private void drawMenu(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
                            RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        
        int width = getWidth();
        int height = getHeight();
        
        // Font monospace per stile retro
        Font titleFont = new Font("Monospaced", Font.BOLD, 20);
        Font menuFont = new Font("Monospaced", Font.BOLD, 32);
        Font instructionFont = new Font("Monospaced", Font.PLAIN, 14);
        
        // Disegna il logo ASCII
        g2d.setColor(new Color(0, 255, 100));
        g2d.setFont(titleFont);
        
        String[] logo = {
            "    ___    ____  ______   ___  ________________ ________ __",
            "   /   |  / __ \\/_  __/  /   |/_  __/_  __/   |/ ____/ //_/",
            "  / /| | / /_/ / / /    / /| | / /   / / / /| / /   / ,<   ",
            " / ___ |/ _, _/ / /    / ___ |/ /   / / / ___ / /___/ /| |  ",
            "/_/  |_/_/ |_| /_/    /_/  |_/_/   /_/ /_/  |_\\____/_/ |_|  "
        };
        
        int logoY = height / 6;
        FontMetrics fm = g2d.getFontMetrics();
        
        for (int i = 0; i < logo.length; i++) {
            int x = (width - fm.stringWidth(logo[i])) / 2;
            g2d.drawString(logo[i], x, logoY + (i * fm.getHeight()));
        }
        
        // Disegna le opzioni del menu
        g2d.setFont(menuFont);
        int menuStartY = height / 2 + 50;
        int menuSpacing = 80;
        
        for (int i = 0; i < menuOptions.length; i++) {
            int y = menuStartY + (i * menuSpacing);
            String prefix = (i == selectedOption) ? "> " : "  ";
            String option = prefix + "[" + menuOptions[i] + "]";
            
            // Colore diverso per l'opzione selezionata
            if (i == selectedOption) {
                g2d.setColor(new Color(100, 255, 100));
            } else {
                g2d.setColor(new Color(0, 150, 50));
            }
            
            int x = (width - g2d.getFontMetrics().stringWidth(option)) / 2;
            g2d.drawString(option, x, y);
        }
        
        // Istruzioni in basso
        g2d.setFont(instructionFont);
        g2d.setColor(new Color(0, 100, 50));
        String instructions = "Usa le frecce ↑↓ per navigare | INVIO per selezionare";
        int instrX = (width - g2d.getFontMetrics().stringWidth(instructions)) / 2;
        g2d.drawString(instructions, instrX, height - 100);
        
        // Cursore lampeggiante
        if ((System.currentTimeMillis() / 500) % 2 == 0) {
            g2d.drawString("█", instrX, height - 60);
        }
    }
    
    private int getOptionAtPosition(int y) {
        int height = getHeight();
        int menuStartY = height / 2 + 50;
        int menuSpacing = 80;
        
        for (int i = 0; i < menuOptions.length; i++) {
            int optionY = menuStartY + (i * menuSpacing);
            if (y >= optionY - 30 && y <= optionY + 30) {
                return i;
            }
        }
        return -1;
    }
    
    private void handleSelection() {
        switch(selectedOption) {
            case 0: // Nuova Partita
                mainFrame.showGame();
                break;
            case 1: // Continua
                JOptionPane.showMessageDialog(this, 
                    "Funzionalità non ancora implementata", 
                    "Continua", 
                    JOptionPane.INFORMATION_MESSAGE);
                break;
            case 2: // Esci
                System.exit(0);
                break;
        }
    }
}