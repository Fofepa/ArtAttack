package com.artattack.mapelements.skilltree;

import java.util.ArrayList;
import java.util.List;

import com.artattack.mapelements.Player;


// N-ary tree
public class SkillTree{
    private Node root;
    private List<Node> supportList;

    public SkillTree(Node root){
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