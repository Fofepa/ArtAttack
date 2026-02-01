package com.artattack.mapelements.skilltree;

import java.util.ArrayList;
import java.util.List;

import com.artattack.mapelements.Player;

public abstract class Node{
    private NodeType type;
    private boolean spent;  // tells if it was used or not
    private int label;  // defines his number as a child
    private static int counter = 0;
    private List<Node> parents;
    private List<Node> children;
    private Player player;

    public Node(Player player, NodeType type){
        this.type = type;
        this.spent = false;
        this.children = new ArrayList<>();
        this.player = player;
        this.parents = new ArrayList<>();
    }

    public void addChildren(List<Node> children){
        for(Node child : children){
            if(!child.hasParent()){
                child.setLabel();
            }
            child.setParent(this);
        }
        this.children = children;
    }

    

    public Player getPlayer(){
        return this.player;
    }

    public int getLabel(){
        return this.label;
    }

    public List<Node> getChildren(){
        return this.children;
    }

    public Node getChild(int index){
        return this.children.get(index);
    }

    public List<Node> getParents(){
        return this.parents;
    }

    public void setParent(Node parent){
        this.parents.add(parent);
    }

    public boolean isSpent(){
        return this.spent;
    }

    public void setSpent(){
        this.spent = true;
    }
    
    public boolean hasParent(){
        return this.parents.isEmpty() ? false : true;
    }

    public void setLabel(){
        this.label = counter++;
    }

    public abstract void setSkill();

    public NodeType getType() {
        return type;
    }

    public static int getCounter() {
        return counter;
    }

}