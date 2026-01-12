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

import com.artattack.inputcontroller.CombatStrategy;
import com.artattack.level.Coordinates;
import com.artattack.level.Maps;
import com.artattack.mapelements.Player;
import com.artattack.moves.Move;
import com.artattack.moves.Weapon;

public class MovesPanel extends JPanel {
    private Player currentPlayer;
    private MapPanel mapPanel;
    private MainFrame mainFrame;
    private CombatStrategy combatStrategy;
    
    private List<Weapon> weapons;
    private List<Move> moves;
    
    private int selectedWeaponIndex = 0;
    private int selectedMoveIndex = 0;
    private boolean isInMoveSelection = false;
    
    private final Font FONT = new Font("Monospaced", Font.PLAIN, 12);
    private final Font TITLE_FONT = new Font("Monospaced", Font.BOLD, 14);
    private final Font SELECTED_FONT = new Font("Monospaced", Font.BOLD, 12);
    
    private final int LINE_HEIGHT = 18;
    private final int MARGIN = 10;
    
    private WeaponListPanel weaponListPanel;
    private MoveListPanel moveListPanel;
    
    public MovesPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        setFocusable(true);
        
        weaponListPanel = new WeaponListPanel();
        moveListPanel = new MoveListPanel();
        
        javax.swing.JSplitPane splitPane = new javax.swing.JSplitPane(
            javax.swing.JSplitPane.HORIZONTAL_SPLIT,
            weaponListPanel,
            moveListPanel
        );
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerSize(2);
        splitPane.setDividerLocation(0.5);
        whiteLineDivider(splitPane);
        
        add(splitPane, BorderLayout.CENTER);
        
        addFocusBorder();
        
        weapons = new ArrayList<>();
        moves = new ArrayList<>();
    }
    
    
    public void initializeCombatStrategy(Maps map) {
        this.combatStrategy = new CombatStrategy(map, currentPlayer);
        this.combatStrategy.setMainFrame(this.mainFrame);
        System.out.println("CombatStrategy initialized in MovesPanel");
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
                if (mapPanel != null) {
                    mapPanel.clearAttackArea();
                }
            }
        });
    }
    
    public void updateAttackArea() {
        if (mapPanel == null || moves.isEmpty() || currentPlayer == null) {
            if (mapPanel != null) {
                mapPanel.clearAttackArea();
            }
            return;
        }

        Move selectedMove = moves.get(selectedMoveIndex);
        List<Coordinates> relativeArea = selectedMove.getAttackArea();

        if (relativeArea == null || relativeArea.isEmpty()) {
            mapPanel.clearAttackArea();
            return;
        }

        Coordinates playerPos = currentPlayer.getCoordinates();
        if (playerPos == null) {
            mapPanel.clearAttackArea();
            return;
        }

        List<Coordinates> absoluteArea = new ArrayList<>();
        for (Coordinates relative : relativeArea) {
            Coordinates absolute = Coordinates.sum(playerPos, relative);

            if (isValidCoordinate(absolute)) {
                absoluteArea.add(absolute);
            }
        }

        System.out.println("Attack area for " + selectedMove.getName() + 
                           " from player at (" + playerPos.getX() + "," + playerPos.getY() + 
                           "): " + absoluteArea.size() + " cells");

        mapPanel.showAttackArea(absoluteArea);
    }
    
    private boolean isValidCoordinate(Coordinates coord) {
        if (mapPanel == null) return false;
        
        int rows = mapPanel.getMapRows();
        int cols = mapPanel.getMapColumns();
        
        return coord.getX() >= 0 && coord.getX() < cols &&
               coord.getY() >= 0 && coord.getY() < rows;
    }
    
    public void refreshAttackArea() {
        if (isInMoveSelection && !moves.isEmpty()) {
            updateAttackArea();
        }
    }
    
    
    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
        
        if (combatStrategy != null) {
            combatStrategy.setCurrentPlayer(player);
            System.out.println("CombatStrategy player updated to: " + player.getName());
        }
        
        if (player != null) {
            this.weapons = player.getWeapons();
            System.out.println("MovesPanel: Loaded " + weapons.size() + " weapons for " + player.getName());
        } else {
            this.weapons = new ArrayList<>();
        }
        
        // Reset selezione
        selectedWeaponIndex = 0;
        selectedMoveIndex = 0;
        isInMoveSelection = false;
        moves.clear();
        
        if (mapPanel != null) {
            mapPanel.clearAttackArea();
        }
        
        repaint();
    }

    public void giveFocus(){
        setFocusable(true);
        requestFocusInWindow();
    }
    
    public void setMapPanel(MapPanel mapPanel) {
        this.mapPanel = mapPanel;
    }
    
    public Move getSelectedMove() {
        if (isInMoveSelection && !moves.isEmpty()) {
            return moves.get(selectedMoveIndex);
        }
        return null;
    }
    
    public boolean isInMoveSelection() {
        return isInMoveSelection;
    }
    
    
    public CombatStrategy getCombatStrategy() {
        return combatStrategy;
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

    public void setMainFrame(MainFrame mainFrame){
        this.mainFrame = mainFrame;
    }
    
    // ========== Weapon List Panel ==========
    private class WeaponListPanel extends JPanel {
        public WeaponListPanel() {
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
            String title = "WEAPONS";
            FontMetrics fm = g2d.getFontMetrics();
            int titleWidth = fm.stringWidth(title);
            g2d.drawString(title, (getWidth() - titleWidth) / 2, MARGIN + fm.getAscent());
            
            int y = MARGIN + fm.getHeight() + 5;
            g2d.drawLine(MARGIN, y, getWidth() - MARGIN, y);
            
            y += 15;
            
            if (weapons.isEmpty()) {
                g2d.setFont(FONT);
                g2d.setColor(Color.GRAY);
                String msg = currentPlayer == null ? "No player" : "No weapons";
                int width = g2d.getFontMetrics().stringWidth(msg);
                g2d.drawString(msg, (getWidth() - width) / 2, y + 20);
                return;
            }
            
            g2d.setFont(FONT);
            for (int i = 0; i < weapons.size(); i++) {
                Weapon weapon = weapons.get(i);
                boolean isSelected = (i == selectedWeaponIndex && !isInMoveSelection);
                
                drawWeaponEntry(g2d, weapon, y, isSelected, i);
                y += LINE_HEIGHT;
            }
        }
        
        private void drawWeaponEntry(Graphics2D g2d, Weapon weapon, int y, 
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
            
            String text = weapon.getName();
            g2d.drawString(text, MARGIN + 15, y);
            
            if (weapon.getMoves() != null) {
                g2d.setFont(FONT);
                g2d.setColor(Color.GRAY);
                String info = "(" + weapon.getMoves().size() + " moves)";
                int infoX = getWidth() - g2d.getFontMetrics().stringWidth(info) - MARGIN;
                g2d.drawString(info, infoX, y);
            }
        }
    }
    
    // ========== Moves List Panel ==========
    private class MoveListPanel extends JPanel {
        public MoveListPanel() {
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
            String title = "MOVES";
            FontMetrics fm = g2d.getFontMetrics();
            int titleWidth = fm.stringWidth(title);
            g2d.drawString(title, (getWidth() - titleWidth) / 2, MARGIN + fm.getAscent());
            
            int y = MARGIN + fm.getHeight() + 5;
            g2d.drawLine(MARGIN, y, getWidth() - MARGIN, y);
            
            y += 15;
            
            if (!isInMoveSelection) {
                g2d.setFont(FONT);
                g2d.setColor(Color.GRAY);
                String msg = "Press → to select";
                int width = g2d.getFontMetrics().stringWidth(msg);
                g2d.drawString(msg, (getWidth() - width) / 2, getHeight() / 2);
                return;
            }
            
            if (moves.isEmpty()) {
                g2d.setFont(FONT);
                g2d.setColor(Color.GRAY);
                String msg = "No moves";
                int width = g2d.getFontMetrics().stringWidth(msg);
                g2d.drawString(msg, (getWidth() - width) / 2, y + 20);
                return;
            }
            
            g2d.setFont(FONT);
            for (int i = 0; i < moves.size(); i++) {
                Move move = moves.get(i);
                boolean isSelected = (i == selectedMoveIndex && isInMoveSelection);
                
                drawMoveEntry(g2d, move, y, isSelected, i);
                y += LINE_HEIGHT;
            }
        }
        
        private void drawMoveEntry(Graphics2D g2d, Move move, int y, 
                                  boolean isSelected, int index) {
            FontMetrics fm = g2d.getFontMetrics();
            
            if (isSelected) {
                g2d.setColor(new Color(139, 0, 0));
                g2d.fillRect(0, y - fm.getAscent() + 2, getWidth(), LINE_HEIGHT);
                
                g2d.setColor(Color.RED);
                g2d.drawString(">", MARGIN, y);
            }
            
            g2d.setColor(isSelected ? Color.RED : Color.WHITE);
            g2d.setFont(isSelected ? SELECTED_FONT : FONT);
            
            String text = move.getName();
            g2d.drawString(text, MARGIN + 15, y);
            
            g2d.setFont(FONT);
            g2d.setColor(Color.GRAY);
            String info = String.format("PWR:%d AP:%d", move.getPower(), move.getActionPoints());
            int infoX = getWidth() - g2d.getFontMetrics().stringWidth(info) - MARGIN;
            g2d.drawString(info, infoX, y);
        }
    }
}