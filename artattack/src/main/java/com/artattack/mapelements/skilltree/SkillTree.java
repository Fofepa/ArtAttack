package com.artattack.mapelements.skilltree;

import java.util.*;

import com.artattack.level.AreaBuilder;
import com.artattack.level.Coordinates;
import com.artattack.mapelements.Musician;
import com.artattack.mapelements.Player;
import com.artattack.moves.Move;
import com.artattack.moves.Weapon;


// N-ary tree
public class SkillTree{
    private Player player;
    private Node root;
    private List<Node> supportList;

    public SkillTree(Player player, Node root){
        this.player = player;
        this.root = root;
        this.root.setSpent();
        this.root.setLabel();
        this.supportList = new ArrayList<>();
        this.supportList.add(root);
    }

    public void buildTree(Node root){
        if(root.getChildren().isEmpty())
            return;
        for (Node child : root.getChildren()){
            if(!supportList.contains(child)){
                this.supportList.add(child);
            }
            buildTree(child);
        }
    }

    public Node find(int label){
        for(Node node : this.supportList){
            if(label == node.getLabel())
                return node;
        }
        System.out.println("No node found with that label");
        return null;
    }

    public List<Node> getSupportList(){
        return this.supportList;
    }     
    
}