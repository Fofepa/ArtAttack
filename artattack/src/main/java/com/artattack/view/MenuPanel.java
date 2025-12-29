package com.artattack.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MenuPanel extends JPanel {
    
    private int selectedOption = 0;
    private final String[] menuOptions = {"NUOVA PARTITA", "CONTINUA", "ESCI"};
    private MainFrame mainFrame;
    private Image logoImage;
    
    public MenuPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setBackground(Color.BLACK);
        setFocusable(true);
        
        // Carica l'immagine del logo
        loadLogo();
        
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
    
    private void loadLogo() {
        try {
            // Carica l'immagine del logo - modifica il percorso con il tuo
            File logoFile = new File("artattack\\src\\main\\java\\com\\artattack\\view\\assets\\Screenshot 2025-12-26 alle 18.42.45.png");
            logoImage = ImageIO.read(logoFile);
            System.out.println("Logo caricato con successo!");
        } catch (IOException e) {
            System.err.println("Impossibile caricare il logo: " + e.getMessage());
            logoImage = null;
        }
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
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        
        int width = getWidth();
        int height = getHeight();
        
        // Font monospace per stile retro
        Font menuFont = new Font("Monospaced", Font.BOLD, 32);
        Font instructionFont = new Font("Monospaced", Font.PLAIN, 14);
        
        // Disegna il logo PNG
        if (logoImage != null) {
            // Calcola dimensioni mantenendo le proporzioni
            int logoMaxWidth = (int) (width * 0.9);
            int logoMaxHeight = (int) (height * 0.5);
            
            int logoWidth = logoImage.getWidth(null);
            int logoHeight = logoImage.getHeight(null);
            
            // Scala mantenendo le proporzioni
            double scale = Math.min(
                (double) logoMaxWidth / logoWidth,
                (double) logoMaxHeight / logoHeight
            );
            
            int scaledWidth = (int) (logoWidth * scale);
            int scaledHeight = (int) (logoHeight * scale);
            
            // Centra il logo
            int logoX = (width - scaledWidth) / 2+30;
            int logoY = height / 500;
            
            g2d.drawImage(logoImage, logoX, logoY, scaledWidth, scaledHeight, null);
        } else {
            // Fallback: disegna testo se il logo non è caricato
            g2d.setColor(new Color(0, 255, 100));
            g2d.setFont(new Font("Monospaced", Font.BOLD, 48));
            String fallbackText = "ART ATTACK";
            int textWidth = g2d.getFontMetrics().stringWidth(fallbackText);
            g2d.drawString(fallbackText, (width - textWidth) / 2, height / 5);
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