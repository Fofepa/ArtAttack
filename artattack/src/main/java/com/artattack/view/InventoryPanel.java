package com.artattack.view;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.artattack.inputcontroller.InventoryStrategy;
import com.artattack.items.Item;
import com.artattack.level.Maps;
import com.artattack.mapelements.ActiveElement;
import com.artattack.mapelements.Player;

public class InventoryPanel extends JPanel {

    private Player currentPlayer;
    
    private InventoryStrategy inventoryStrategy;

    private List<Item> items;
    private List<ActiveElement> targets;

    private int selectedItemIndex = 0;
    private int selectedTargetIndex = 0;

    private boolean isInTargetSelection = false;

    private final Font FONT = new Font("Monospaced", Font.PLAIN, 12);
    private final Font TITLE_FONT = new Font("Monospaced", Font.BOLD, 14);
    private final Font SELECTED_FONT = new Font("Monospaced", Font.BOLD, 12);
    
    private final int LINE_HEIGHT = 18;
    private final int MARGIN = 10;

    private ItemListPanel itemListPanel;
    private ItemDetailPanel itemDetailPanel;


     public InventoryPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        setFocusable(true);
        
        itemListPanel = new ItemListPanel();
        itemDetailPanel = new ItemDetailPanel();
        
        javax.swing.JSplitPane splitPane = new javax.swing.JSplitPane(
            javax.swing.JSplitPane.HORIZONTAL_SPLIT,
            itemListPanel,
            itemDetailPanel
        );
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerSize(2);
        splitPane.setDividerLocation(0.5);
        whiteLineDivider(splitPane);
        
        add(splitPane, BorderLayout.CENTER);
        
        addFocusBorder();
        
        items = new ArrayList<>();
        targets = new ArrayList<>();
    }


    public void initializeInventoryStrategy(Maps map) {
        this.inventoryStrategy = new InventoryStrategy(map, currentPlayer);
        
        System.out.println("InventoryStrategy initialized in InventoryPanel");
    }


    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
        
        if (inventoryStrategy != null) {
            inventoryStrategy.setCurrentPlayer(player);
            System.out.println("InventoryStrategy player updated to: " + player.getName());
        }
        
        if (player != null) {
            this.items = player.getInventory();
            System.out.println("InventoryPanel: Loaded " + items.size() + " items for " + player.getName());
        } else {
            this.items = new ArrayList<>();
        }
        
        // Reset selezione
        selectedItemIndex = 0;
        selectedTargetIndex = 0;
        isInTargetSelection = false;
        
        repaint();
    }


    private void addFocusBorder() {
        addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                setBorder(null);
            }
        });
    }


    public void giveFocus(){
        setFocusable(true);
        requestFocusInWindow();
    }



    private void whiteLineDivider(javax.swing.JSplitPane splitPane) {
        splitPane.setUI(new javax.swing.plaf.basic.BasicSplitPaneUI() {
            @Override
            public javax.swing.plaf.basic.BasicSplitPaneDivider createDefaultDivider() {
                return new javax.swing.plaf.basic.BasicSplitPaneDivider(this) {
                    @Override
                    public void paint(Graphics g) {
                        g.setColor(Color.WHITE);
                        g.fillRect(0, 0, getWidth(), getHeight());
                    }
                };
            }
        });
    }

    // ========== Item List Panel (Left) ==========
    private class ItemListPanel extends JPanel {
        public ItemListPanel() {
            setBackground(Color.BLACK);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
                                 RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
            
            g2d.setFont(TITLE_FONT);
            g2d.setColor(Color.WHITE);
            String title = "INVENTORY";
            FontMetrics fm = g2d.getFontMetrics();
            int titleWidth = fm.stringWidth(title);
            g2d.drawString(title, (getWidth() - titleWidth) / 2, MARGIN + fm.getAscent());
            
            int y = MARGIN + fm.getHeight() + 5;
            g2d.drawLine(MARGIN, y, getWidth() - MARGIN, y);
            
            y += 15;
            
            if (items.isEmpty()) {
                g2d.setFont(FONT);
                g2d.setColor(Color.GRAY);
                String msg = inventoryStrategy.getPlayer() == null ? "No player" : "Empty inventory";
                int width = g2d.getFontMetrics().stringWidth(msg);
                g2d.drawString(msg, (getWidth() - width) / 2, y + 20);
                return;
            }
            
            g2d.setFont(FONT);
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                boolean isSelected = (i == selectedItemIndex && !isInTargetSelection);
                
                drawItemEntry(g2d, item, y, isSelected, i);
                y += LINE_HEIGHT;
            }
        }
        
        private void drawItemEntry(Graphics2D g2d, Item item, int y, 
                                   boolean isSelected, int index) {
            FontMetrics fm = g2d.getFontMetrics();
            
            if (isSelected) {
                g2d.setColor(new Color(0, 139, 139));
                g2d.fillRect(0, y - fm.getAscent() + 2, getWidth(), LINE_HEIGHT);
                
                g2d.setColor(Color.CYAN);
                g2d.drawString(">", MARGIN, y);
            }
            
            g2d.setColor(isSelected ? Color.CYAN : Color.WHITE);
            g2d.setFont(isSelected ? SELECTED_FONT : FONT);
            
            String text = item.getName();
            g2d.drawString(text, MARGIN + 15, y);
            
            /* // Quantità
            g2d.setFont(FONT);
            g2d.setColor(Color.GRAY);
            String quantity = "x" + item.getQuantity();
            int quantityX = getWidth() - g2d.getFontMetrics().stringWidth(quantity) - MARGIN;
            g2d.drawString(quantity, quantityX, y); */
        }
    }
    
    // ========== Item Detail Panel (Right) ==========
    private class ItemDetailPanel extends JPanel {
        public ItemDetailPanel() {
            setBackground(Color.BLACK);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
                                 RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
            
            g2d.setFont(TITLE_FONT);
            g2d.setColor(Color.WHITE);
            String title = "DETAILS";
            FontMetrics fm = g2d.getFontMetrics();
            int titleWidth = fm.stringWidth(title);
            g2d.drawString(title, (getWidth() - titleWidth) / 2, MARGIN + fm.getAscent());
            
            int y = MARGIN + fm.getHeight() + 5;
            g2d.drawLine(MARGIN, y, getWidth() - MARGIN, y);
            
            y += 15;
            
            if (!isInTargetSelection) {
                // Shows the description of the selected Item
                if (items.isEmpty()) {
                    g2d.setFont(FONT);
                    g2d.setColor(Color.GRAY);
                    String msg = "No item selected";
                    int width = g2d.getFontMetrics().stringWidth(msg);
                    g2d.drawString(msg, (getWidth() - width) / 2, getHeight() / 2);
                } else {
                    Item selectedItem = items.get(selectedItemIndex);
                    
                    g2d.setFont(TITLE_FONT);
                    g2d.setColor(Color.YELLOW);
                    g2d.drawString(selectedItem.getName(), MARGIN, y);
                    y += LINE_HEIGHT + 5;
                    
                    g2d.setFont(FONT);
                    g2d.setColor(Color.WHITE);
                    
                    // Description (wrapped if too long)
                    String description = selectedItem.getDescription();
                    List<String> lines = wrapText(description, getWidth() - 2 * MARGIN, g2d.getFontMetrics());
                    for (String line : lines) {
                        g2d.drawString(line, MARGIN, y);
                        y += LINE_HEIGHT;
                    }
                    
                    y += 10;
                    g2d.setColor(Color.GRAY);
                    g2d.drawString("Press → to select target", MARGIN, y);
                }
            } else {
                // Shows the target selection
                g2d.setFont(FONT);
                g2d.setColor(Color.WHITE);
                g2d.drawString("Select target:", MARGIN, y);
                y += LINE_HEIGHT + 5;
                
                if (targets.isEmpty()) {
                    g2d.setColor(Color.GRAY);
                    g2d.drawString("No targets available", MARGIN, y);
                } else {
                    for (int i = 0; i < targets.size(); i++) {
                        Player target = (Player)targets.get(i); //FIX FIX FIX
                        boolean isSelected = (i == selectedTargetIndex);
                        
                        drawTargetEntry(g2d, target, y, isSelected);
                        y += LINE_HEIGHT;
                    }
                }
            }
        }
        
        private void drawTargetEntry(Graphics2D g2d, Player target, int y, boolean isSelected) {
            FontMetrics fm = g2d.getFontMetrics();
            
            if (isSelected) {
                g2d.setColor(new Color(139, 0, 0));
                g2d.fillRect(0, y - fm.getAscent() + 2, getWidth(), LINE_HEIGHT);
                
                g2d.setColor(Color.RED);
                g2d.drawString(">", MARGIN, y);
            }
            
            g2d.setColor(isSelected ? Color.RED : Color.WHITE);
            g2d.setFont(isSelected ? SELECTED_FONT : FONT);
            
            String text = target.getName();
            g2d.drawString(text, MARGIN + 15, y);
            
            /* // HP del target
            g2d.setFont(FONT);
            g2d.setColor(Color.GRAY);
            String hp = "HP: " + target.getCurrentHP() + "/" + target.getMaxHP();
            int hpX = getWidth() - g2d.getFontMetrics().stringWidth(hp) - MARGIN;
            g2d.drawString(hp, hpX, y); */
        }
        
        private List<String> wrapText(String text, int maxWidth, FontMetrics fm) {
            List<String> lines = new ArrayList<>();
            String[] words = text.split(" ");
            StringBuilder currentLine = new StringBuilder();
            
            for (String word : words) {
                String testLine = currentLine.length() == 0 ? word : currentLine + " " + word;
                if (fm.stringWidth(testLine) <= maxWidth) {
                    if (currentLine.length() > 0) currentLine.append(" ");
                    currentLine.append(word);
                } else {
                    if (currentLine.length() > 0) {
                        lines.add(currentLine.toString());
                        currentLine = new StringBuilder(word);
                    } else {
                        lines.add(word);
                    }
                }
            }
            
            if (currentLine.length() > 0) {
                lines.add(currentLine.toString());
            }
            
            return lines;
        }
    }

}
