package com.artattack.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.JPanel;

import com.artattack.mapelements.Player;
import com.artattack.mapelements.skilltree.APNODE;
import com.artattack.mapelements.skilltree.HPNODE;
import com.artattack.mapelements.skilltree.MANODE;
import com.artattack.mapelements.skilltree.MAXMVNODE;
import com.artattack.mapelements.skilltree.MAXWPNODE;
import com.artattack.mapelements.skilltree.Node;
import com.artattack.mapelements.skilltree.RootNode;
import com.artattack.mapelements.skilltree.SPNODE;
import com.artattack.mapelements.skilltree.SkillTree;
import com.artattack.mapelements.skilltree.SpecialMoveNODE;

/**
 * SkillTreePanel - Displays the player's skill tree.
 * Updated with manual layouts for Zappa and Lynch, larger UI elements,
 * and improved description panel positioning.
 */
public class SkillTreePanel extends JPanel {
    private SkillTree skillTree;
    private Player player;
    private Node selectedNode = null;
    private Consumer<Node> onNodeSelected;
    
    // Visual states colors
    private static final Color COLOR_SPENT = new Color(40, 40, 40);
    private static final Color COLOR_AVAILABLE = new Color(0, 255, 100);
    private static final Color COLOR_LOCKED = new Color(80, 80, 80);
    private static final Color COLOR_SELECTED = Color.CYAN;
    private static final Color COLOR_CONNECTION = new Color(100, 100, 100);
    
    // Tree layout settings
    private Map<Node, NodePosition> nodePositions;
    private int nodeRadius = 30; 
    
    // Navigation
    private List<Node> availableNodes;
    private int currentIndex = 0;
    
    public SkillTreePanel(SkillTree skillTree, Player player, Consumer<Node> onNodeSelected) {
        this.skillTree = skillTree;
        this.player = player;
        this.onNodeSelected = onNodeSelected;
        this.nodePositions = new HashMap<>();
        this.availableNodes = new ArrayList<>();
        
        initializePanel();
        updateAvailableNodes();
        setupInput();
    }
    
    private void initializePanel() {
        setBackground(Color.BLACK);
        setFocusable(true);
    }
    
    private void calculateNodePositions() {
        Node root = findRootNode();
        if (root == null) return;
        
        nodePositions.clear();
        int centerX = getWidth() / 2;
        
        int startY = 130; 

        switch (player.getType()) {
            case MUSICIAN -> layoutZappaTree(root, centerX, startY);
            case MOVIE_DIRECTOR -> layoutLynchTree(root, centerX, startY);
        }
    }

    
    private void layoutZappaTree(Node root, int cx, int y) {
        putPos(root, cx, y);

        if (root.getChildren().size() < 2) return;
        Node n2 = root.getChildren().get(0); 
        Node n3 = root.getChildren().get(1); 

        // --- LEFT BRANCH (Zappa) ---
       
        putPos(n2, cx - 140, y + 80); 

        if (n2.getChildren().size() >= 2) {
            Node n4 = n2.getChildren().get(0);
            Node n5 = n2.getChildren().get(1);
            
            putPos(n4, cx - 220, y + 150); 
            putPos(n5, cx - 90,  y + 150);  
            
            if (!n4.getChildren().isEmpty()) {
                Node n6 = n4.getChildren().get(0);
                putPos(n6, cx - 150, y + 230); 
                
                if (!n6.getChildren().isEmpty()) {
                    Node n7 = n6.getChildren().get(0);
                    putPos(n7, cx - 150, y + 310); 
                    
                    if (n7.getChildren().size() >= 2) {
                        Node n8 = n7.getChildren().get(0);
                        Node n9 = n7.getChildren().get(1);
                        
                        putPos(n8, cx - 240, y + 380); 
                        putPos(n9, cx - 100, y + 380);  
                        
                        if (n8.getChildren().size() >= 2) {
                            Node n11 = n8.getChildren().get(0);
                            Node n10 = n8.getChildren().get(1);
                            putPos(n10, cx - 290, y + 460); 
                            putPos(n11, cx - 210, y + 480); 
                            
                            if (!n10.getChildren().isEmpty()) {
                                Node n12 = n10.getChildren().get(0); 
                                putPos(n12, cx - 270, y + 560); 
                            }
                        }
                        
                        if (!n9.getChildren().isEmpty()) {
                            Node n13 = n9.getChildren().get(0);
                            putPos(n13, cx - 100, y + 460);
                            if (!n13.getChildren().isEmpty()) {
                                Node n14 = n13.getChildren().get(0);
                                putPos(n14, cx - 100, y + 540); 
                            }
                        }
                    }
                }
            }
        }

        putPos(n3, cx + 140, y + 80);

        if (!n3.getChildren().isEmpty()) {
            Node n15 = n3.getChildren().get(0);
            putPos(n15, cx + 190, y + 140); 
            
            if (!n15.getChildren().isEmpty()) {
                Node n16 = n15.getChildren().get(0);
                putPos(n16, cx + 240, y + 200);
                
                if (n16.getChildren().size() >= 2) {
                    Node n17 = n16.getChildren().get(0);
                    Node n18 = n16.getChildren().get(1);
                    
                    putPos(n17, cx + 180, y + 270);
                    putPos(n18, cx + 300, y + 270);
                    
                    Node n21 = null; 
                    
                    if (!n17.getChildren().isEmpty()) {
                        Node n19 = n17.getChildren().get(0);
                        putPos(n19, cx + 190, y + 340);
                        if (!n19.getChildren().isEmpty()) n21 = n19.getChildren().get(0);
                    }
                    
                    if (!n18.getChildren().isEmpty()) {
                        Node n20 = n18.getChildren().get(0);
                        putPos(n20, cx + 300, y + 340);
                        if (!n20.getChildren().isEmpty() && n21 == null) n21 = n20.getChildren().get(0);
                    }
                    
                    if (n21 != null) {
                        putPos(n21, cx + 240, y + 410);
                        
                        if (n21.getChildren().size() >= 2) {
                            Node n22 = n21.getChildren().get(0);
                            Node n23 = n21.getChildren().get(1);
                            
                            putPos(n22, cx + 200, y + 490);
                            putPos(n23, cx + 320, y + 470);
                            
                            Node n26 = null;
                            
                            if (!n22.getChildren().isEmpty()) {
                                Node n24 = n22.getChildren().get(0);
                                putPos(n24, cx + 200, y + 570);
                                if (!n24.getChildren().isEmpty()) n26 = n24.getChildren().get(0);
                            }
                            
                            if (!n23.getChildren().isEmpty()) {
                                Node n25 = n23.getChildren().get(0);
                                putPos(n25, cx + 320, y + 550);
                                if (!n25.getChildren().isEmpty() && n26 == null) n26 = n25.getChildren().get(0);
                            }
                            
                            if (n26 != null) {
                                putPos(n26, cx + 260, y + 640);
                            }
                        }
                    }
                }
            }
        }
    }

    
    private void layoutLynchTree(Node root, int cx, int y) {
        putPos(root, cx, y);
        if (root.getChildren().size() < 2) return;
        
        Node n2 = root.getChildren().get(0);
        Node n3 = root.getChildren().get(1);

        // --- LEFT BRANCH (Lynch) ---
        putPos(n2, cx - 100, y + 70); 
        
        if (!n2.getChildren().isEmpty()) {
            Node n4 = n2.getChildren().get(0);
            putPos(n4, cx - 150, y + 130);
            
            if (n4.getChildren().size() >= 2) {
                // Diamond Split
                Node n5 = n4.getChildren().get(0);
                Node n6 = n4.getChildren().get(1);
                
                putPos(n5, cx - 200, y + 200); 
                putPos(n6, cx - 100, y + 200); 
                
                Node n9 = null;
                
                if (!n5.getChildren().isEmpty()) {
                    Node n7 = n5.getChildren().get(0);
                    putPos(n7, cx - 220, y + 280);
                    if (!n7.getChildren().isEmpty()) n9 = n7.getChildren().get(0);
                }
                
                if (!n6.getChildren().isEmpty()) {
                    Node n8 = n6.getChildren().get(0);
                    putPos(n8, cx - 90, y + 280);
                    if (!n8.getChildren().isEmpty() && n9 == null) n9 = n8.getChildren().get(0);
                }
                
                // Merge
                if (n9 != null) {
                    putPos(n9, cx - 150, y + 350);
                    
                    if (n9.getChildren().size() >= 2) {
                        Node n10 = n9.getChildren().get(0);
                        Node n11 = n9.getChildren().get(1);
                        
                        putPos(n10, cx - 220, y + 430);
                        putPos(n11, cx - 80,  y + 430);
                        
                        if (!n10.getChildren().isEmpty()) {
                            Node n12 = n10.getChildren().get(0);
                            putPos(n12, cx - 220, y + 510);
                            if (!n12.getChildren().isEmpty()) {
                                Node n13 = n12.getChildren().get(0);
                                putPos(n13, cx - 220, y + 590); 
                            }
                        }
                        
                        if (!n11.getChildren().isEmpty()) {
                            Node n14 = n11.getChildren().get(0);
                            putPos(n14, cx - 80, y + 510);
                            if (!n14.getChildren().isEmpty()) {
                                Node n15 = n14.getChildren().get(0);
                                putPos(n15, cx - 80, y + 590);
                            }
                        }
                    }
                }
            }
        }

        // --- RIGHT BRANCH (Lynch) ---
        putPos(n3, cx + 100, y + 70);
        
        if (!n3.getChildren().isEmpty()) {
            Node n16 = n3.getChildren().get(0);
            putPos(n16, cx + 140, y + 140);
            
            if (!n16.getChildren().isEmpty()) {
                Node n17 = n16.getChildren().get(0);
                putPos(n17, cx + 190, y + 210);
                
                if (!n17.getChildren().isEmpty()) {
                    Node n18 = n17.getChildren().get(0);
                    putPos(n18, cx + 160, y + 290); 
                    
                    if (n18.getChildren().size() >= 2) {
                        Node n19 = n18.getChildren().get(0);
                        Node n20 = n18.getChildren().get(1);
                        
                        putPos(n19, cx + 100, y + 360);
                        putPos(n20, cx + 220, y + 360);
                        
                        Node n21 = null;
                        
                        if (!n19.getChildren().isEmpty()) n21 = n19.getChildren().get(0);
                        if (n21 == null && !n20.getChildren().isEmpty()) n21 = n20.getChildren().get(0);
                        
                        if (n21 != null) {
                            putPos(n21, cx + 160, y + 440);
                            
                            if (!n21.getChildren().isEmpty()) {
                                Node n22 = n21.getChildren().get(0);
                                putPos(n22, cx + 160, y + 520);
                                
                                // Trident Split (3-way)
                                List<Node> lastChildren = n22.getChildren();
                                if (lastChildren.size() == 3) {
                                    Node n23 = lastChildren.get(0);
                                    Node n24 = lastChildren.get(1);
                                    Node n25 = lastChildren.get(2);
                                    
                                    putPos(n23, cx + 60,  y + 600);
                                    putPos(n24, cx + 160, y + 610);
                                    putPos(n25, cx + 260, y + 600);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void putPos(Node node, int x, int y) {
        nodePositions.put(node, new NodePosition(x, y));
    }
    
    // --- UTILS ---

    private Node findRootNode() {
        for (Node node : skillTree.getSupportList()) {
            if (node instanceof RootNode) return node;
        }
        return skillTree.getSupportList().isEmpty() ? null : skillTree.getSupportList().get(0);
    }

    private void updateAvailableNodes() {
        availableNodes.clear();
        Node root = findRootNode();
        for (Node node : skillTree.getSupportList()) {
            if (isNodeAvailable(node)) availableNodes.add(node);
        }
        
        if (selectedNode == null || !availableNodes.contains(selectedNode)) {
            if (!availableNodes.isEmpty()) {
                selectedNode = availableNodes.get(0);
                currentIndex = 0;
            }
        }
    }

    private boolean isNodeAvailable(Node node) {
        if (node instanceof RootNode) return false;
        if (node.isSpent()) return false;
        if (!node.hasParent()) return false;
        for (Node parent : node.getParents()) {
            if (parent.isSpent()) return true;
        }
        return false;
    }

    private void setupInput() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT, KeyEvent.VK_A -> navigateHorizontal(-1);
                    case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> navigateHorizontal(1);
                    case KeyEvent.VK_UP, KeyEvent.VK_W -> navigateVertical(-1);
                    case KeyEvent.VK_DOWN, KeyEvent.VK_S -> navigateVertical(1);
                    case KeyEvent.VK_ENTER, KeyEvent.VK_SPACE -> confirmSelection();
                }
            }
        });
    }

    private void navigateHorizontal(int direction) { navigate(direction, true); }
    private void navigateVertical(int direction) { navigate(direction, false); }
    
    
    private void navigate(int direction, boolean horizontal) {
        if (selectedNode == null || availableNodes.isEmpty()) return;
        NodePosition currentPos = nodePositions.get(selectedNode);
        if (currentPos == null) return;
        
        Node bestNode = null;
        double minDistance = Double.MAX_VALUE;
        
        for (Node node : availableNodes) {
            if (node == selectedNode) continue;
            NodePosition pos = nodePositions.get(node);
            if (pos == null) continue;
            
            int dx = pos.x - currentPos.x;
            int dy = pos.y - currentPos.y;
            boolean correctDir = horizontal ? ((direction > 0) ? dx > 0 : dx < 0) : ((direction > 0) ? dy > 0 : dy < 0);
            
            if (correctDir) {
                double distance = Math.hypot(dx, dy);
                if (distance < minDistance) {
                    minDistance = distance;
                    bestNode = node;
                }
            }
        }
        if (bestNode != null) {
            selectedNode = bestNode;
            currentIndex = availableNodes.indexOf(selectedNode);
            repaint();
        }
    }
    
    private void confirmSelection() {
    if (selectedNode != null && isNodeAvailable(selectedNode)) {
        if (onNodeSelected != null) onNodeSelected.accept(selectedNode);
    } else if (availableNodes.isEmpty()) {
        System.out.println("Skill tree complete - no more nodes available");
        if (onNodeSelected != null) {
            onNodeSelected.accept(null);
        }
    }
}

    // --- DRAWING ---

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        calculateNodePositions();
        
        drawTitle(g2, width);
        drawConnections(g2);
        drawNodes(g2);
        
        if (selectedNode != null) {
            drawNodeDescription(g2, selectedNode, width, height);
        }
        drawInstructions(g2, width, height);
    }
    
    private void drawTitle(Graphics2D g2, int width) {
        g2.setFont(new Font("Monospaced", Font.BOLD, 42)); 
        g2.setColor(COLOR_SELECTED);
        String title = player.getName() + " TREE";
        drawCenteredString(g2, title, width / 2, 50); 
    }
    
    private void drawConnections(Graphics2D g2) {
        g2.setStroke(new BasicStroke(3)); 
        for (Node node : skillTree.getSupportList()) {
            NodePosition parentPos = nodePositions.get(node);
            if (parentPos == null) continue;
            for (Node child : node.getChildren()) {
                NodePosition childPos = nodePositions.get(child);
                if (childPos == null) continue;
                if (node.isSpent() && child.isSpent()) g2.setColor(COLOR_SPENT.brighter());
                else if (node.isSpent()) g2.setColor(COLOR_AVAILABLE.darker());
                else g2.setColor(COLOR_CONNECTION);
                g2.drawLine(parentPos.x, parentPos.y, childPos.x, childPos.y);
            }
        }
    }
    
    private void drawNodes(Graphics2D g2) {
        for (Node node : skillTree.getSupportList()) {
            NodePosition pos = nodePositions.get(node);
            if (pos == null) continue;
            drawNode(g2, node, pos.x, pos.y, node == selectedNode, isNodeAvailable(node), node.isSpent());
        }
    }
    
    private void drawNode(Graphics2D g2, Node node, int x, int y, boolean isSelected, boolean isAvailable, boolean isSpent) {
        Color fillColor;
        Color borderColor;
        
        if (isSpent) {
            fillColor = COLOR_SPENT;
            borderColor = COLOR_SPENT.brighter();
        } else if (isAvailable) {
            fillColor = isSelected ? COLOR_SELECTED.darker() : COLOR_AVAILABLE.darker().darker();
            borderColor = isSelected ? COLOR_SELECTED : COLOR_AVAILABLE;
        } else {
            fillColor = COLOR_LOCKED;
            borderColor = COLOR_LOCKED.brighter();
        }
        
        g2.setColor(fillColor);
        g2.fillOval(x - nodeRadius, y - nodeRadius, nodeRadius * 2, nodeRadius * 2);
        
        g2.setStroke(new BasicStroke(isSelected ? 4 : 2));
        g2.setColor(borderColor);
        g2.drawOval(x - nodeRadius, y - nodeRadius, nodeRadius * 2, nodeRadius * 2);
        
        
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Monospaced", Font.BOLD, 12));
        String nodeTypeLabel = getNodeShortLabel(node);
        drawCenteredString(g2, nodeTypeLabel, x, y + 4);
        
        
        if (isSpent) {
            g2.setColor(COLOR_AVAILABLE);
            g2.setStroke(new BasicStroke(3));
            g2.drawLine(x-8, y, x-2, y+6);
            g2.drawLine(x-2, y+6, x+8, y-6);
        }
    }
    
    private void drawNodeDescription(Graphics2D g2, Node node, int width, int height) {
        int boxWidth = 360;  
        int boxHeight = 160; 

        
        int boxX = 50; 
        int boxY = (height / 2) - (boxHeight / 2);

        // Background
        g2.setColor(new Color(20, 20, 20, 245));
        g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 15, 15);

        // Border
        g2.setColor(COLOR_SELECTED);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 15, 15);

        // Text Content
        g2.setFont(new Font("Monospaced", Font.BOLD, 18));
        g2.setColor(Color.WHITE);
        drawCenteredString(g2, getNodeTypeName(node), boxX + boxWidth / 2, boxY + 35);

        g2.setFont(new Font("Monospaced", Font.PLAIN, 15));
        g2.setColor(COLOR_AVAILABLE);
        drawCenteredString(g2, getNodeEffect(node), boxX + boxWidth / 2, boxY + 70);
        
        g2.setColor(Color.LIGHT_GRAY);
        g2.setFont(new Font("Monospaced", Font.ITALIC, 13));
        String statusText;
        if (node.isSpent()) statusText = "STATUS: UNLOCKED";
        else if (isNodeAvailable(node)) statusText = "STATUS: AVAILABLE (Press ENTER)";
        else statusText = "STATUS: LOCKED";
        
        drawCenteredString(g2, statusText, boxX + boxWidth / 2, boxY + 115);
    }
    
    // --- HELPERS ---
    
    private String getNodeShortLabel(Node node) {
        return switch (node) {
            case RootNode r -> "ROOT";
            case HPNODE h -> "HP";
            case APNODE a -> "AP";
            case SPNODE s -> "SPD";
            case MANODE m -> "AREA";
            case MAXWPNODE w -> "WPN";
            case MAXMVNODE mv -> "MOV";
            case SpecialMoveNODE sm -> "Ult";
            default -> "?";
        };
    }
    
    private String getNodeTypeName(Node node) {
        return switch (node) {
            case RootNode r -> "Start Point";
            case HPNODE h -> "Health Boost";
            case APNODE a -> "Action Points";
            case SPNODE s -> "Speed Boost";
            case MANODE m -> "Area Expansion";
            case MAXWPNODE w -> "Weapon Slot";
            case MAXMVNODE mv -> "Movement Cap";
            case SpecialMoveNODE sm -> "SPECIAL MOVE";
            default -> "Unknown Node";
        };
    }

    private String getNodeEffect(Node node) {
        if (node instanceof HPNODE h) return "Effect: +" + getHPValue(h) + " Max HP";
        if (node instanceof APNODE a) return "Effect: +" + getAPValue(a) + " Max AP";
        if (node instanceof SPNODE s) return "Effect: +" + getSPValue(s) + " Speed";
        if (node instanceof MANODE) return "Effect: Increases Attack Area";
        if (node instanceof MAXWPNODE) return "Effect: Unlocks Weapon Slot";
        if (node instanceof MAXMVNODE) return "Effect: +1 Movement Range";
        if (node instanceof SpecialMoveNODE sm) return "Ability: " + getSpecialMoveName(sm);
        return "Effect: None";
    }
    
    // Reflection helpers to access private fields from Node subclasses
    private int getHPValue(HPNODE node) { try { var f = HPNODE.class.getDeclaredField("newHP"); f.setAccessible(true); return (int) f.get(node); } catch (Exception e) { return 0; } }
    private int getAPValue(APNODE node) { try { var f = APNODE.class.getDeclaredField("newAP"); f.setAccessible(true); return (int) f.get(node); } catch (Exception e) { return 0; } }
    private int getSPValue(SPNODE node) { try { var f = SPNODE.class.getDeclaredField("newSP"); f.setAccessible(true); return (int) f.get(node); } catch (Exception e) { return 0; } }
    private String getSpecialMoveName(SpecialMoveNODE node) { try { var f = SpecialMoveNODE.class.getDeclaredField("specialMove"); f.setAccessible(true); Object m = f.get(node); if(m!=null) return (String)m.getClass().getMethod("getName").invoke(m); } catch (Exception e) {} return "Special Ability"; }

    private void drawInstructions(Graphics2D g2, int width, int height) {
        g2.setFont(new Font("Monospaced", Font.PLAIN, 12));
        g2.setColor(Color.DARK_GRAY);
        g2.drawString("ARROWS to Select, ENTER to Confirm", 20, height - 20);
    }
    
    private void drawCenteredString(Graphics g, String text, int x, int y) {
        FontMetrics metrics = g.getFontMetrics();
        int dX = x - (metrics.stringWidth(text) / 2);
        int dY = y + (metrics.getAscent() - metrics.getDescent()) / 2;
        g.drawString(text, dX, dY);
    }
    
    private static class NodePosition {
        int x, y;
        NodePosition(int x, int y) { this.x = x; this.y = y; }
    }
}