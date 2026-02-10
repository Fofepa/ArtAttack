package com.artattack.view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class CharacterSelectionPanel extends JPanel {
    private MainGUIFacade mainFacade;
    private CharacterType[] types = CharacterType.values();
    
    // Cache per le immagini
    private Map<CharacterType, Image> characterImages;
    
    // Stato della selezione
    private int hoverIndex = 0;
    private CharacterType player1Choice = null;
    private boolean isPlayer1Turn = true;
    
    // Dimensioni carta
    private final int CARD_WIDTH = 280; // Leggermente più larga per l'immagine
    private final int CARD_HEIGHT = 400; // Più alta per far stare l'immagine
    private final int IMAGE_HEIGHT = 180; // Altezza riservata all'immagine

    public CharacterSelectionPanel(MainGUIFacade mainFacade) {
        this.mainFacade = mainFacade;
        loadCharacterImages(); // Carica le immagini all'avvio
        initializePanel();
        setupInput();
    }

    /**
     * Carica le immagini usando getResource per compatibilità JAR/Linux
     */
    private void loadCharacterImages() {
        characterImages = new HashMap<>();
        
        for (CharacterType type : types) {
            String path = "";
            
            // Definisci i percorsi qui o prendili dall'Enum se hai aggiunto un metodo getImagePath()
            switch (type) {
                case MUSICIAN:
                    path = "/images/frank-zappa-fotor-20260206135640.jpg";
                    break;
                case DIRECTOR: // O MOVIE_DIRECTOR a seconda del tuo enum
                    path = "/images/ozxg45isal6ve56l7tl6-fotor-20260206135846.jpg";
                    break;
                default:
                    // Percorso default o break
                    break;
            }
            
            try {
                if (!path.isEmpty()) {
                    URL imgUrl = getClass().getResource(path);
                    if (imgUrl != null) {
                        Image img = ImageIO.read(imgUrl);
                        characterImages.put(type, img);
                    } else {
                        System.err.println("Immagine non trovata per " + type + ": " + path);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initializePanel() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        setFocusable(true);
    }

    private void setupInput() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT, KeyEvent.VK_A -> {
                        moveSelection(-1);
                    }
                    case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> {
                        moveSelection(1);
                    }
                    case KeyEvent.VK_ENTER, KeyEvent.VK_SPACE -> {
                        CharacterType selected = types[hoverIndex];
                        // Se è il turno P2 e sta scegliendo lo stesso di P1, confermaSelection lo gestirà
                        confirmSelection();
                    }
                    case KeyEvent.VK_ESCAPE -> {
                        if (!isPlayer1Turn) {
                            // Torna alla selezione P1
                            isPlayer1Turn = true;
                            player1Choice = null;
                            repaint();
                        } else {
                            // Torna al menu principale
                            mainFacade.showMenu();
                        }
                    }
                }
            }
        });
    }

    private void moveSelection(int delta) {
        hoverIndex = (hoverIndex + delta + types.length) % types.length;
        repaint();
    }

    private void confirmSelection() {
        CharacterType selected = types[hoverIndex];

        // Se è il turno del P2 e sta provando a scegliere il personaggio di P1
        if (!isPlayer1Turn && selected == player1Choice) {
            // Feedback visivo (es. suono errore)
            System.out.println("Character already taken!");
            return;
        }

        if (isPlayer1Turn) {
            player1Choice = selected;
            isPlayer1Turn = false;
            // Sposta automaticamente la selezione sul prossimo disponibile
            hoverIndex = (hoverIndex + 1) % types.length;
            repaint();
        } else {
            // Selezione finita, avvia il gioco
            mainFacade.finalizeGameSetup(player1Choice, selected);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        // Migliore qualità rendering immagini e testo
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int width = getWidth();
        int height = getHeight();

        // 1. Disegna Titolo
        String title = isPlayer1Turn ? "PLAYER 1: CHOOSE CHARACTER" : "PLAYER 2: CHOOSE CHARACTER";
        g2.setFont(new Font("Monospaced", Font.BOLD, 40));
        g2.setColor(isPlayer1Turn ? Color.GREEN : Color.MAGENTA);
        drawCenteredString(g2, title, width / 2, 60);

        // 2. Calcolo posizione carte
        int gap = 50;
        int totalWidth = (types.length * CARD_WIDTH) + ((types.length - 1) * gap);
        int startX = (width - totalWidth) / 2;
        int startY = (height - CARD_HEIGHT) / 2 + 20;

        for (int i = 0; i < types.length; i++) {
            CharacterType type = types[i];
            int x = startX + (i * (CARD_WIDTH + gap));
            
            drawCharacterCard(g2, type, x, startY, CARD_WIDTH, CARD_HEIGHT, i == hoverIndex);
        }
        
        // 3. Istruzioni
        g2.setFont(new Font("Monospaced", Font.ITALIC, 16));
        g2.setColor(Color.GRAY);
        String instruction = isPlayer1Turn ? "Press ENTER to confirm" : "Select a distinct character";
        drawCenteredString(g2, instruction, width / 2, height - 50);
    }

    private void drawCharacterCard(Graphics2D g2, CharacterType type, int x, int y, int w, int h, boolean isHovered) {
        boolean isTaken = (type == player1Choice);

        // --- 1. SFONDO E BORDO ---
        if (isTaken) {
            g2.setColor(new Color(20, 20, 20)); 
        } else if (isHovered) {
            g2.setColor(new Color(45, 45, 45)); 
        } else {
            g2.setColor(new Color(30, 30, 30)); 
        }
        g2.fillRoundRect(x, y, w, h, 15, 15);

        // Bordo colorato
        if (isHovered) {
            g2.setColor(isPlayer1Turn ? Color.CYAN : Color.MAGENTA);
            g2.setStroke(new BasicStroke(3));
        } else if (isTaken) {
            g2.setColor(Color.DARK_GRAY); // Bordo grigio per preso
            g2.setStroke(new BasicStroke(2));
        } else {
            g2.setColor(Color.GRAY);
            g2.setStroke(new BasicStroke(1));
        }
        g2.drawRoundRect(x, y, w, h, 15, 15);

        // --- 2. IMMAGINE DEL PERSONAGGIO ---
        Image img = characterImages.get(type);
        int imgPadding = 10;
        int imgW = w - (imgPadding * 2);
        int imgH = IMAGE_HEIGHT;
        
        if (img != null) {
            // Disegna immagine (con clip arrotondata se volessimo fare i fighi, ma semplice per ora)
            g2.drawImage(img, x + imgPadding, y + imgPadding, imgW, imgH, null);
            
            // Cornice attorno all'immagine
            g2.setColor(new Color(0,0,0,100));
            g2.drawRect(x + imgPadding, y + imgPadding, imgW, imgH);
        } else {
            // Placeholder se immagine non trovata
            g2.setColor(Color.BLACK);
            g2.fillRect(x + imgPadding, y + imgPadding, imgW, imgH);
            g2.setColor(Color.GRAY);
            drawCenteredString(g2, "NO IMAGE", x + w/2, y + imgH/2);
        }

        // --- 3. INTESTAZIONE (Nome e Descrizione) ---
        // Spostiamo tutto sotto l'immagine
        int currentY = y + IMAGE_HEIGHT + 35;
        
        // Nome
        g2.setColor(isTaken ? Color.GRAY : Color.WHITE);
        g2.setFont(new Font("Monospaced", Font.BOLD, 22));
        drawCenteredString(g2, type.getName(), x + w / 2, currentY);
        
        currentY += 20;
        
        // Descrizione
        g2.setFont(new Font("Monospaced", Font.ITALIC, 12));
        g2.setColor(Color.LIGHT_GRAY);
        drawCenteredString(g2, type.getDescription(), x + w / 2, currentY);

        // Linea divisoria
        currentY += 15;
        g2.setColor(Color.DARK_GRAY);
        g2.drawLine(x + 20, currentY, x + w - 20, currentY);

        // --- 4. STATISTICHE ---
        currentY += 25;
        int barX = x + 20;
        int barWidth = w - 40; // Larghezza barre

        // Arma
        g2.setFont(new Font("Monospaced", Font.PLAIN, 14));
        g2.setColor(Color.WHITE);
        g2.drawString("Wpn: " + type.getWeaponName(), barX, currentY);
        
        currentY += 25;

        // Barra HP 
        drawStatBar(g2, "HP", type.getMaxHP(), 30, Color.RED, barX, currentY, barWidth);
        
        currentY += 30;

        // Barra Velocità
        drawStatBar(g2, "Spd", type.getSpeed(), 10, Color.YELLOW, barX, currentY, barWidth);

        // --- 5. OVERLAY (TAKEN / P1 / P2) ---
        // Se preso, disegna "TAKEN" sopra tutto
        if (isTaken) {
            // Sfondo semi-trasparente sopra l'intera carta
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRoundRect(x, y, w, h, 15, 15);
            
            g2.setColor(new Color(255, 50, 50)); 
            g2.setFont(new Font("Monospaced", Font.BOLD, 40));
            
            AffineTransform orig = g2.getTransform();
            g2.rotate(Math.toRadians(-20), x + w/2, y + h/2);
            drawCenteredString(g2, "TAKEN", x + w / 2, y + h / 2);
            g2.setTransform(orig);
            
            // Mostra chi l'ha preso
            g2.setFont(new Font("Monospaced", Font.BOLD, 16));
            g2.setColor(Color.WHITE);
            drawCenteredString(g2, "by Player 1", x + w/2, y + h/2 + 30);
        }
        
        // Indicatore selezione
        if (isHovered && !isTaken) {
            g2.setColor(isPlayer1Turn ? Color.CYAN : Color.MAGENTA);
            String label = isPlayer1Turn ? "P1 Select" : "P2 Select";
            g2.setFont(new Font("Monospaced", Font.BOLD, 14));
            // Disegna sotto la carta
            drawCenteredString(g2, "▲ " + label + " ▲", x + w/2, y + h + 20);
        }
    }

    private void drawStatBar(Graphics2D g2, String label, int value, int max, Color color, int x, int y, int width) {
        int height = 8;
        int labelWidth = 30;
        
        // Etichetta (es. "HP")
        g2.setColor(Color.LIGHT_GRAY);
        g2.setFont(new Font("Monospaced", Font.BOLD, 12));
        g2.drawString(label, x, y + height);

        int barStartX = x + labelWidth + 5;
        int actualBarWidth = width - labelWidth - 5;

        // Sfondo barra (grigio scuro)
        g2.setColor(new Color(50, 50, 50));
        g2.fillRect(barStartX, y, actualBarWidth, height);

        // Parte piena
        int fillWidth = (int) (((double) value / max) * actualBarWidth);
        if (fillWidth > actualBarWidth) fillWidth = actualBarWidth;
        
        g2.setColor(color);
        g2.fillRect(barStartX, y, fillWidth, height);
        
        // Bordo sottile
        g2.setColor(new Color(100, 100, 100));
        g2.drawRect(barStartX, y, actualBarWidth, height);
    }

    private void drawCenteredString(Graphics g, String text, int x, int y) {
        if (text == null) return;
        FontMetrics metrics = g.getFontMetrics();
        int dX = x - (metrics.stringWidth(text) / 2);
        int dY = y + (metrics.getAscent() - metrics.getDescent()) / 2;
        g.drawString(text, dX, dY);
    }
}